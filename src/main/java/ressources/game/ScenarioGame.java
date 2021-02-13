package ressources.game;

import org.json.JSONObject;

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
        int year = 0;
        int seasonCount = 0;
        System.out.println(this.getScenario().getName());
//        for(int i = 0; i < scenario.getJSONArray("scenario").length(); i++) {
//            if (i % 4 == 0 && i != 0) {
//                year++;
//                System.out.println("\n--- année suivante ! " + year + " ---\n");
//            }
//
//            System.out.println(scenario.getJSONArray("scenario")
//                    .getJSONObject(i)
//                    .getJSONArray("event")
//                    .getJSONObject(0).getString("name"));
//        }
//
//        System.out.println("\n--- 1ere année --- \n");



//        while(!isScenarioFinished() && !hasPlayerLost()) {
//            year += 1;
//            while(seasonCount < 4) {
//                 Event event = getEvent();
//                // displayEvent(event);
//                // int playerSolutionChoice = getPlayerChoice();
//                // eventImpacts(playerSolutionChoice);
//                seasonCount += 1;
//            }
//            // displayYearEndSummary();
//            // displayPlayerYearEndChoices();
//            // int playerSaveRepublicChoice = getPlayerChoice();
//            // yearEndChoiceImpacts();
//            // displayYearEndSummary();
//            // foodImpactOnPopulation(checkEnoughFood());
//            // addScore(calculateYearEndScore());
//        }
    }

    public boolean isScenarioFinished() {
        return getEvent() == null;
    }

    @Override
    public String toString() {
        return "scénario";
    }
}