package ressources.game;
import ressources.republic.Republic;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Faction;
import ressources.republic.factions.Population;
import ressources.parser.IParser;
import ressources.parser.JSONParser;
import ressources.scenario.Choice;
import ressources.scenario.Effect;
import ressources.scenario.Event;
import ressources.scenario.Scenario;

public abstract class Game {
    protected Republic republic;
    protected final GameDifficulty gameDifficulty;
    protected Scenario scenario;
    protected double score;
    private IParser parser;

    public Game(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public void setScore(double score) {
        // TODO score négatif possible ?
        this.score = score;
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

    public void load(String filePath) {
        try {
            setParserType(filePath);
            parser.openFile(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            gameShutDown();
        }
        if(canLoadGame()) {
            try {
                Population population = parser.parsePopulation();
                Resources resources = parser.parseResources();
                this.republic = new Republic(population, resources);
                this.scenario = parser.parseScenario();
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
            return parser.isGameStartParameterDifficultyInJson(this.gameDifficulty);
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

    public void play() throws NullPointerException{
        if(this.republic.isSet()) {
            System.out.printf("%nVous avez lancé une partie en mode \"%s\" ", this.toString());
            System.out.printf("en difficulté \"%s\".%n", this.gameDifficulty);
            System.out.printf("%nÊtes-vous prêt à commencer la partie ?%n");
            PlayerInput.pressAnyKeyToContinue();
            System.out.printf("%nLancement du jeu...%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");
        }
        else {
            System.out.println("Arrêt du jeu...");
            throw new NullPointerException("Game properties are not set.");
        }
    }

    public boolean isPlayerWinning() {
        if(this.republic.getTotalPopulation() > 0) {
            return this.republic.getPopulation().getGlobalSatisfactionRate() >= GameRules.MINIMUM_GLOBAL_SATISFACTION_RATE;
        }
        return true;
    }

    public Event getCurrentEvent() {
        return this.scenario.getCurrentEvent();
    }


    public void playerChoiceImpacts(int choice) {
        Choice playerChoice = getCurrentEvent().getChoiceByPlayerChoice(choice);
        Effect choiceEffects = playerChoice.getEffects();
        this.republic.impacts(choiceEffects);
    }

    public boolean isTimeToYearEndSummary(int seasonCount) {
        return seasonCount % 4 == 0 && seasonCount != 0;
    }

    public void displayYearEndSummary(int year) {
        System.out.printf("%n%n- Bilan de cette %de année -%n", year);
        this.republic.getPopulation().displaySummary();
        System.out.println();
        this.republic.getResources().displaySummary();
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
        System.out.println("\t=> Diminution de la satisfaction des Loyalistes à hauteur du prix du pot-de-vin");
        System.out.println("Option 3 : Marché alimentaire (coût par unité : 8$)");
        System.out.println("\t=> Rappel : 4 unités de nourriture par citoyen sont nécessaires");
        System.out.println("Entrez votre choix :");
    }

    public void addScore(double scoreToAdd) {
        setScore(this.score + scoreToAdd);
    }

    public double getSeasonEndScore() {
        double seasonEndScore = 0;
        seasonEndScore += this.republic.getPopulation().getTotalSatisfactionRate() * GameRules.SCORE_POINTS_PER_SATISFACTION_WON;
        return seasonEndScore;
    }

    public double getEndGameScore(int nbYear) {
        double endGameScore = this.score;
        // +10 points per year
        endGameScore += nbYear * GameRules.SCORE_POINTS_PER_YEAR;
        endGameScore += this.republic.getResources().getMoney() * GameRules.SCORE_POINTS_PER_DOLLAR;
        return endGameScore;
    }
}