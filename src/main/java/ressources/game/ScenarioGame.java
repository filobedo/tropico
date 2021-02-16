package ressources.game;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void play() {
        super.play();
        // Check nb events in seasons
        // si la premiere saison qui commence a moins d'év que les autres c'est mort
        // si la saison avant la premiere
        System.out.printf("%nNom du scénario : %s%n", this.getScenario().getName());
        System.out.println(this.getScenario().getStory());

        int year = 1;
        int seasonCount = 0;
        int eventCount = 1;
        getScenario().nextEvent(seasonCount);
        while(!hasPlayerLost()) {
            if(!isScenarioFinished()) {
                System.out.printf("%n%n-- Nous sommes en %s de la %de année --%n", this.getScenario().getCurrentSeason().capitalize(), year);

                displayCurrentEvent(eventCount);
                irreversibleEventImpacts();
                int playerSolutionChoice = getPlayerChoice(getCurrentEvent().getNbChoices());
                playerChoiceImpacts(playerSolutionChoice);
                addScore(getSeasonEndScore());

                seasonCount += 1;
                eventCount += 1;
                getScenario().nextEvent(seasonCount);
                getScenario().nextSeason();

                if(isTimeToYearEndSummary(seasonCount)) {
                    // TODO à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini
                    // Industry and Farm generate money and food
                    getTreasury().generateFarmIncome();
                    getTreasury().generateIndustryIncome();
                    // Year End Summary
                    displayYearEndSummary(year);
                    handlePlayerYearEndChoices();
                    UserInput.pressAnyKeyToContinue();
                    displayYearEndSummary(year);
                    // TODO Vérifier le calcul de "eliminateSupportersUntilEnoughFood()" dans population
                    int nbCitizensEliminated = getPopulation().getNbSupportersToEliminateToHaveEnoughFood(getFoodUnits());
                    boolean hasEliminatedSupporters = getPopulation().eliminateSupportersUntilEnoughFood(nbCitizensEliminated);
                    if(hasEliminatedSupporters) {
                        System.out.println("La population a diminué car vous n'aviez pas assez de nourriture.");
                        System.out.printf("%nVous avez perdu %d citoyens.%n", nbCitizensEliminated);
                    }
                    else {
                        int nbNewCitizens = this.getPopulation().increasePopulationRandomly();
                        System.out.println("Félicitation, vous aviez assez de nourriture pour nourriture toute votre population.");
                        System.out.println("Vous avez même du surplus.");
                        System.out.printf("%nAinsi, la population a augmenté de %d citoyens.%n", nbNewCitizens);
                    }
                    // ^à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini

                    year += 1;
                    UserInput.pressAnyKeyToContinue();
                }
            }
            else {
                System.out.println("Le scénario est fini. Vous avez gagné la partie.");
                System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
                break;
            }
        }
        System.out.printf("%n%nVotre score est de %f", getEndGameScore(year));

    }

    public boolean isScenarioFinished() {
        return getScenario().isScenarioFinished();
    }

    @Override
    public String toString() {
        return "scénario";
    }
}