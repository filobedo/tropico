package ressources.game;

import org.json.JSONObject;
import ressources.scenario.Event;

public class ScenarioGame extends Game {
    private String name;
    private String scenarioFile;

    public ScenarioGame(GameDifficulty gameDifficulty, String name) {
        super(gameDifficulty);
        this.name = name;
    }

    public void setScenarioFile(String scenarioFile) {
        this.scenarioFile = scenarioFile;
    }


    @Override
    public void play() {
        super.play();
        /*TODO
         *  Chaque année
         *  => 4 tours <=> 4 évènements suivant l'ordre du scénario
         *  hasNotLost and scenario not finished
         *  + voir si le joueur peut arrêter le jeu à la fin du bilan
         */
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

                seasonCount += 1;
                eventCount += 1;
                getScenario().nextEvent(seasonCount);
                getScenario().nextSeason();

                if(isTimeToYearEndSummary(seasonCount)) {
                    // Industry and Farm generate money and food
                    this.getTreasury().generateFarmIncome();
                    this.getTreasury().generateIndustryIncome();
                    // Year End Summary
                    displayYearEndSummary(year);
                    handlePlayerYearEndChoices();
                    UserInput.pressAnyKeyToContinue();
                    displayYearEndSummary(year);
                    // TODO Vérifier le calcul de "eliminateSupportersUntilEnoughFood()" dans population
                    boolean hasEliminatedSupporters = this.getPopulation().eliminateSupportersUntilEnoughFood(getFoodUnits());
                    if(hasEliminatedSupporters) {
                        // TODO retour à l'utilisateur que sa population a diminué
                    }
                    else {
                        // TODO retour à l'utilisateur que sa population a grandit
                        this.getPopulation().increasePopulationRandomly();
                    }
                    // TODO addScore(calculateYearEndScore());
                    year += 1;

                }
            }
            else {
                System.out.println("Le scénario est fini. Vous avez gagné la partie.");
                System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
                break;
            }
        }
    }

    public boolean isScenarioFinished() {
        return getScenario().isScenarioFinished();
    }

    @Override
    public String toString() {
        return "scénario";
    }
}