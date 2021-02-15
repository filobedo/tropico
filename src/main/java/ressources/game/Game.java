package ressources.game;
import ressources.economy.Tresory;
import ressources.factions.Faction;
import ressources.factions.Population;
import ressources.parser.IParser;
import ressources.parser.JSONParser;
import ressources.scenario.Choice;
import ressources.scenario.Effect;
import ressources.scenario.Event;
import ressources.scenario.Scenario;

import java.util.Map;
import java.util.Scanner;

public abstract class Game {
    private double score;
    private Tresory treasury;
    private Population population;
    private final GameDifficulty gameDifficulty;
    private Scenario scenario;
    private IParser parser;

    public Game(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public double getScore() {
        return score;
    }

    public Tresory getTreasury() {
        return treasury;
    }

    public Population getPopulation() {
        return population;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
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
                this.population = parser.parsePopulation();
                this.treasury = parser.parseResources();
                this.scenario = parser.parseScenario();
            } catch (Exception ex) {
                ex.printStackTrace();
                gameShutDown();
            }
        }
        else {
            System.out.println("Cannot load game");
            gameShutDown();
        }
    }

    public void gameShutDown() {
        System.out.println("Le jeu est terminé.");
        System.exit(0);
    }

    public boolean canLoadGame() {
        if(parser.canParseFile()) {
            if(parser.isGameStartParameterDifficultyInJson(this.gameDifficulty)) {
                return true;
            }
        }
        return false;
    }

    public void setParserType(String filePath) {
        if(filePath.toLowerCase().endsWith(".json")) {
            this.parser = new JSONParser();
        }
    }

    public void play() throws NullPointerException{
        if(getPopulation() != null && getTreasury() != null) {
            System.out.printf("%nVous avez lancé une partie en mode \"%s\" ", this.toString());
            System.out.printf("en difficulté \"%s\".%n", getGameDifficulty());
            System.out.printf("%nÊtes-vous prêt à commencer la partie ?%n");
            UserInput.pressAnyKeyToContinue();
            System.out.printf("%nLancement du jeu...%n");
        }
        else {
            System.out.println("Arrêt du jeu...");
            throw new NullPointerException("Game properties are not set.");
        }
    }

    public boolean hasPlayerLost() {
        if(getPopulation().getTotalPopulation() > 0) {
            return getPopulation().getGlobalSatisfactionRate() < 25;
        }
        return true;
    }

    public boolean isTimeToYearEndSummary(int seasonCount) {
        return seasonCount % 4 == 0 && seasonCount != 0;
    }

    public void displayCurrentEvent(int eventCount) {
        getScenario().displayCurrentEvent(eventCount);
    }

    public Event getCurrentEvent() {
        return getScenario().getCurrentEvent();
    }

    public int getPlayerChoice(int nbChoice) {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= nbChoice) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return getPlayerChoice(nbChoice);
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return getPlayerChoice(nbChoice);
        }
    }

    public void irreversibleEventImpacts() {
        Event currentEvent = getCurrentEvent();
        Effect eventEffects = currentEvent.getIrreversibleEffects();
        if(eventEffects != null) {
            factionImpacts(eventEffects);
            factorImpacts(eventEffects);
        }
    }
    public void playerChoiceImpacts(int choice) {
        Choice playerChoice = getCurrentEvent().getChoiceByPlayerChoice(choice);
        Effect choiceEffects = playerChoice.getEffects();
        factionImpacts(choiceEffects);
        factorImpacts(choiceEffects);
    }

    public void factionImpacts(Effect effect) {
        Map<String, Map<String, Integer>> factionImpacts = effect.getEffectsByFaction();
        // Each faction
        for(Map.Entry<String, Map<String, Integer>> factionEffectsSet: factionImpacts.entrySet()) {
            String factionName = factionEffectsSet.getKey();
            Map<String, Integer> effectsOnFaction = factionEffectsSet.getValue();
            // Each effect on the faction
            for(Map.Entry<String, Integer> factionEffects: effectsOnFaction.entrySet()) {
                String factorName = factionEffects.getKey();
                int factorEffect = factionEffects.getValue();
                if(factorName.equals("nbSupporters")) {
                    this.population.updateNbSupportersByFaction(factorEffect, factionName);
                }
                if(factorName.equals("satisfactionRate")) {
                    this.population.updateSatisfactionRateByFaction(factorEffect, factionName);
                }
            }
        }
    }
    public void factorImpacts(Effect effect) {
        Map<String, Integer> factorImpacts = effect.getEffectsByFactor();
        // Each factor
        for(Map.Entry<String,Integer> factorEffectsSet: factorImpacts.entrySet()) {
            String factorName = factorEffectsSet.getKey();
            int factorEffect = factorEffectsSet.getValue();
            if(factorName.equals("industryRate")) {
                this.treasury.updateIndustryRate(factorEffect);
            }
            if(factorName.equals("farmRate")) {
                this.treasury.updateFarmRate(factorEffect);
            }
            if(factorName.equals("foodUnits")) {
                this.treasury.addBonusFarm(factorEffect);
            }
            if(factorName.equals("treasury")) {
                this.treasury.addMoney(factorEffect);
            }
            if(factorName.equals("population")) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
            if(factorName.equals("satisfactionRate")) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
        }
    }

    public void displayYearEndSummary(int year) {
        System.out.printf("%n%n- Bilan de cette %de année -%n", year);
        // Population
        this.population.displaySummary();
        System.out.println();
        // Treasury
        this.treasury.displaySummary();
        UserInput.pressAnyKeyToContinue();
    }

    public void handlePlayerYearEndChoices() {
        displayPlayerYearEndChoices();
        int playerYearEndChoice = chooseEndYearOption();
        playerYearEndChoiceImpacts(playerYearEndChoice);
    }

    public void displayPlayerYearEndChoices() {
        System.out.printf("%n%nEn cette fin d'année, vous avez plusieurs options qui se présente à vous pour tenter de sauver votre république de l'insurrection.");
        System.out.println("Option 1 : Ne rien faire");
        System.out.println("Option 2 : Pot-de-vin à une faction (coût par partisan : 15$)");
        System.out.println("\t=> Possible sur toute faction sauf les Loyalistes");
        System.out.println("\t=> +10 points de pourcentage de satisfaction sur la faction choisie");
        System.out.println("\t=> Diminution de la satisfaction des Loyalistes à hauteur du prix du pot-de-vin");
        System.out.println("Option 3 : Marché alimentaire (coût par unité : 8$)");
        System.out.println("\t=> Rappel : 4 unités de nourriture par citoyen sont nécessaires");
        System.out.println("Entrez votre choix :");
    }

    public int chooseEndYearOption() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= 3) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseEndYearOption();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseEndYearOption();
        }
    }

    public void playerYearEndChoiceImpacts(int choice) {
        if(choice == GameRules.YEAR_END_DO_NOTHING_CHOICE) {
            System.out.println("Vous avez décidé de ne rien faire pour sauver votre république." +
                    " Elle doit se porter à merveille%n");
        }
        if(choice == GameRules.YEAR_END_BRIBE_CHOICE) {
            // Bribe
            this.population.displayAvailableFactions();
            int indexFactionToBribe = chooseFactionToBribe();
            String factionToBribe = this.population.getFactionNameByIndex(indexFactionToBribe);
            bribe(factionToBribe);
        }
        if(choice == GameRules.YEAR_END_BUY_FOOD_CHOICE) {
            // Buy food
            int foodUnitsToBuy = chooseFoodUnitsQuantityToBuy();
            buyFood(foodUnitsToBuy);
        }
    }

    public int chooseFactionToBribe() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= this.population.getNbFactions()) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseFactionToBribe();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseFactionToBribe();
        }
    }

    public int chooseFoodUnitsQuantityToBuy() {
        Scanner playerInput = new Scanner(System.in);
        System.out.println("%nEntrez le nombre d'unité de nourriture que vous voulez acheter :");
        String warning = String.format("%nAttention ! Votre entrée est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseFoodUnitsQuantityToBuy();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseFoodUnitsQuantityToBuy();
        }
    }

    public void bribe(String factionName) {
        Faction factionToBribe = this.population.getFaction(factionName);
        if(factionToBribe.canBeBribed()) {
            if(haveEnoughMoney(factionToBribe.getBribePrice())) {
                factionToBribe.bribe();
                this.treasury.useMoney(factionToBribe.getBribePrice());
            }
            else {
                System.out.println("Vous n'avez pas assez d'argent pour verser un pot-de-vin aux" + factionToBribe.getName());
                handlePlayerYearEndChoices();
            }
        }
    }

    public void buyFood(int foodUnit) {
        int foodPrice = this.treasury.simulatePriceBuyingFood(foodUnit);
        if(haveEnoughMoney(foodPrice)) {
            this.treasury.buyFood(foodUnit);
            this.treasury.useMoney(foodPrice);
        }
        else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter autant de nourriture !");
            handlePlayerYearEndChoices();
        }
    }

    public boolean haveEnoughMoney(int price) {
        return this.treasury.getMoney() >= price;
    }
    public int getFoodUnits() {
        return getTreasury().getFood();
    }

    public void addScore(double points) {
        setScore(getScore() + points);
    }
}