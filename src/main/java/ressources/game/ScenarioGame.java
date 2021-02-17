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
        System.out.printf("%nNom du scénario : %s%n", this.scenario.getName());
        System.out.printf("Histoire : %s%n",this.scenario.getStory());
        System.out.printf("%nVous commencez avec ces paramètres de jeu : %n");
        displayYearEndSummary(0);
        int year = 1;
        int seasonCount = 0;
        int eventCount = 1;
        this.scenario.nextEvent(seasonCount);
        while(isPlayerWinning()) {
            if(!isScenarioFinished()) {
                System.out.printf("%n%n-- Nous sommes en %s de la %de année --%n", this.scenario.getCurrentSeason().capitalize(), year);

                this.scenario.displayCurrentEvent(eventCount);
                this.republic.irreversibleEventImpacts(getCurrentEvent());

                int playerSolutionChoice = PlayerInput.getPlayerEventSolutionChoice(getCurrentEvent().getNbChoices());
                playerChoiceImpacts(playerSolutionChoice);
                addScore(getSeasonEndScore());

                seasonCount += 1;
                eventCount += 1;
                this.scenario.nextSeason();
                this.scenario.nextEvent(year);
                this.republic.getResources().updateFarmRate(90);
                if(isEndOfYear(seasonCount)) {
                    // TODO à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini
                    // Industry and Farm generate money and food
                    this.republic.getResources().generateFarmIncome();
                    this.republic.getResources().generateIndustryIncome();
                    // Year End Summary
                    displayYearEndSummary(year);
                    handlePlayerYearEndChoices();
                    PlayerInput.pressAnyKeyToContinue();
                    displayYearEndSummary(year);

                    int nbCitizensEliminated = this.republic.getPopulation().getNbSupportersToEliminateToHaveEnoughFood(this.republic.getFoodUnits());
                    boolean hasEliminatedSupporters = this.republic.getPopulation().eliminateSupportersUntilEnoughFood(nbCitizensEliminated);
                    this.republic.feedPopulation();
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
                    // ^à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini

                    year += 1;
                    PlayerInput.pressAnyKeyToContinue();
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
        return this.scenario.isScenarioFinished();
    }

    @Override
    public String toString() {
        return "scénario";
    }
}