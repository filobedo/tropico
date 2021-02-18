package ressources.game;
import exceptions.MissingEventsException;
import ressources.listeners.SatisfactionDecreasedListener;
import ressources.listeners.SatisfactionIncreasedListener;
import ressources.publisher.EventManager;
import ressources.republic.Republic;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Population;
import ressources.parser.Parser;
import ressources.parser.JSONParser;
import ressources.scenario.Choice;
import ressources.scenario.Effect;
import ressources.scenario.Event;
import ressources.scenario.GamePlay;

public abstract class Game {
    protected Republic republic;
    protected GamePlay gamePlay;
    protected final GameDifficulty gameDifficulty;
    protected double score;
    protected int eventCount = 1;
    public EventManager events;
    private Parser parser;

    public Game(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
        this.score = GameRules.INITIAL_SCORE * (1 / gameDifficulty.getDifficultyCoefficient());
        this.events = new EventManager("satisfaction_increased", "satisfaction_decreased");
        this.events.subscribe("satisfaction_decreased", new SatisfactionDecreasedListener(this));
        this.events.subscribe("satisfaction_increased", new SatisfactionIncreasedListener(this));
    }

    public static void displayIntroduction() {
        String welcome = "\"Bonjour et bienvenue dans un jeu vidéo à la croisée entre Tropico et Reigns !\"";
        String gameRole = "\"Vous incarnerez un jeune dictateur en herbe sur une île tropicale, fraîchement élu comme Président.";
        String gameGoal = "Vous aurez la lourde tâche de faire prospérer cette nouvelle mini-république.\"";
        System.out.printf("%n%s%n", welcome);
        System.out.println("===================");
        System.out.printf("%s%n%s%n", gameRole, gameGoal);
        System.out.println("===================");
    }

    public void load(GameParameters gameParameters) {
        try {
            setParserType(gameParameters.getFilePath());
            this.parser.openFile(gameParameters.getFilePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            gameShutDown();
        }
        this.parser.setGameParametersChosen(gameParameters);
        if(canLoadGame()) {
            try {
                Population population = this.parser.parsePopulation();
                Resources resources = this.parser.parseResources();
                this.republic = new Republic(population, resources);
                this.republic.events = this.events;
                this.gamePlay = this.parser.parseScenario();
            } catch (Exception ex) {
                ex.printStackTrace();
                gameShutDown();
            }
        }
        else {
            System.out.println("Cannot load game. Something is missing in the JSON file.");
            gameShutDown();
        }
    }

    public boolean canLoadGame() {
        if(parser.canParseFile()) {
            return parser.isGameStartParameterDifficultyInJson();
        }
        return false;
    }

    public void gameShutDown() {
        System.out.println("Le jeu est terminé.");
        System.exit(0);
    }

    public void setParserType(String filePath) {
        if(filePath.toLowerCase().endsWith(".json")) {
            this.parser = new JSONParser();
        }
    }

    public int getYear() {
        return gamePlay.getYear();
    }

    public void launchGame() throws NullPointerException, MissingEventsException {
        if(this.republic.isSet()) {
            if(this.gamePlay.canPlayEvents()) {
                System.out.printf("%nVous avez lancé une partie en mode \"%s\" ", this.toString());
                System.out.printf("en difficulté \"%s\".%n", this.gameDifficulty);
                System.out.printf("%nÊtes-vous prêt à commencer la partie ?%n");
                System.out.printf("%nLancement du jeu...%n");
                PlayerInput.pressAnyKeyToContinue();
                System.out.printf("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%nVous commencez avec ces paramètres de jeu : %n");
                displaySummary();
            }
            else {
                throw new MissingEventsException("There is not enough events in one of the seasons.");
            }
        }
        else {
            System.out.printf("%n%nArrêt du jeu...%n");
            throw new NullPointerException("Republic properties are not set.");
        }
    }

    public boolean isPlayerWinning() {
        return isScorePositive() && this.republic.isGlobalSatisfactionRateOkay(this.gameDifficulty.getDifficultyCoefficient());
    }

    public boolean isScorePositive() {
        return this.score >= 0;
    }

    public Event getCurrentEvent() {
        return this.gamePlay.getCurrentEvent();
    }

    public void playGame() {
        System.out.printf("%n%n-- Nous sommes en %s de la %de année --%n", this.gamePlay.getCurrentSeason().capitalize(), getYear() + 1);
        handleCurrentSeason(this.eventCount);

        this.gamePlay.nextSeason();
        this.gamePlay.nextEvent();

        if(isEndOfYear(this.eventCount)) {
            handleEndOfYear();
            this.gamePlay.nextYear();
        }
        this.eventCount += 1;
    }

    public void handleCurrentSeason(int nbEvents) {
        this.gamePlay.displayCurrentEvent(nbEvents);
        this.republic.irreversibleEventImpacts(getCurrentEvent());

        int playerSolutionChoice = PlayerInput.getPlayerEventSolutionChoice(getCurrentEvent().getNbChoices());
        playerChoiceImpacts(playerSolutionChoice);
    }

    public void playerChoiceImpacts(int choice) {
        Choice playerChoice = getCurrentEvent().getChoiceByPlayerChoice(choice);
        Effect choiceEffects = playerChoice.getEffects();
        this.republic.impacts(choiceEffects);
        // TODO RELATED EVENTS
    }

    public boolean isEndOfYear(int seasonCount) {
        return seasonCount % 4 == 0 && seasonCount != 0;
    }

    public void handleEndOfYear() {
        endOfYearConsequencesAndChoices();
        displaySummary();
    }

    public void endOfYearConsequencesAndChoices() {
        generateIncomes();
        PlayerInput.pressAnyKeyToContinue();

        displaySummary();

        handlePlayerYearEndChoices();
        PlayerInput.pressAnyKeyToContinue();

        killAndOrFeedCitizen();
    }

    public void generateIncomes() {
        this.republic.getResources().generateFarmIncome();
        this.republic.getResources().generateIndustryIncome();
        System.out.printf("%n%nL'agriculture a généré cette année %d unité(s) de nourriture.", this.republic.getResources().getFoodIncomeFromFarm());
        System.out.printf("%nL'industrie, quant à elle, a généré cette année %d$.", this.republic.getResources().getMoneyIncomeFromIndustry());
    }

    public void killAndOrFeedCitizen() {
        int nbCitizensEliminated = this.republic.getPopulation().getNbSupportersToEliminateToHaveEnoughFood(this.republic.getFoodUnits());
        boolean hasEliminatedSupporters = this.republic.getPopulation().eliminateSupportersUntilEnoughFood(nbCitizensEliminated);
        this.republic.feedPopulation();
        applyFoodConsequences(hasEliminatedSupporters, nbCitizensEliminated);
    }

    public void applyFoodConsequences(boolean hasEliminatedSupporters, int nbCitizensEliminated) {
        if(hasEliminatedSupporters) {
            System.out.println("La population a diminué car vous n'aviez pas assez de nourriture.");
            System.out.printf("%nVous avez perdu %d citoyens.%n", nbCitizensEliminated);
        }
        else {
            int nbNewCitizens = this.republic.getPopulation().increasePopulationRandomly();
            System.out.println("Félicitation, vous avez assez de nourriture pour nourriture toute votre population.");
            System.out.println("Vous avez même du surplus.");
            System.out.printf("%nAinsi, la population a augmenté de %d citoyens.%n", nbNewCitizens);
        }
    }

    public void displaySummary() {
        if(getYear() > 0) {
            System.out.printf("%n%n- Bilan de cette %de année -%n", getYear());
        }
        this.republic.getPopulation().displaySummary();
        System.out.println();
        this.republic.getResources().displaySummary();
        System.out.printf("%n%n- Score : %.2f -%n", this.score);
        PlayerInput.pressAnyKeyToContinue();
    }

    public void handlePlayerYearEndChoices() {
        displayPlayerYearEndChoices();
        int playerYearEndChoice = PlayerInput.chooseEndYearOption();
        if(!this.republic.playerYearEndChoiceImpacts(playerYearEndChoice)) {
            handlePlayerYearEndChoices();
        }
    }

    public void displayPlayerYearEndChoices() {
        System.out.printf("%n%nEn cette fin d'année, vous avez plusieurs options qui se présente à vous pour tenter de sauver votre république de l'insurrection.%n");
        System.out.println("Option 1 : Ne rien faire");
        System.out.println("Option 2 : Pot-de-vin à une faction (coût par partisan : 15$)");
        System.out.println("\t=> Possible sur toute faction sauf les Loyalistes");
        System.out.println("\t=> +10 points de pourcentage de satisfaction sur la faction choisie");
        System.out.printf("%n\t=> Diminution de la satisfaction des Loyalistes à hauteur du prix du pot-de-vin (prix / %d)%n", GameRules.BRIBE_FACTION_DECREASE_LOYALISTS_SATISFACTION);
        System.out.println("Option 3 : Marché alimentaire (coût par unité : 8$)");
        System.out.println("\t=> Rappel : 4 unités de nourriture par citoyen sont nécessaires");
        System.out.println("Entrez votre choix :");
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore(double scoreToAdd) {
        setScore(this.score + scoreToAdd);
    }

    public double getEndGameScore() {
        double endGameScore = this.score;
        endGameScore += getYear() * GameRules.END_SCORE_POINTS_PER_YEAR;
        int money = this.republic.getResources().getMoney();
        if(money >= 0) {
            endGameScore += this.republic.getResources().getMoney() * GameRules.END_SCORE_POINTS_PER_DOLLAR_POSITIVE;
        }
        else {
            endGameScore += this.republic.getResources().getMoney() * GameRules.END_SCORE_POINTS_PER_DOLLAR_NEGATIVE;
        }
        return endGameScore;
    }
}