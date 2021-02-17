package ressources.game;

public class SandboxGame extends Game {

    public SandboxGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void play() {
        super.play();
        /*TODO
        *  Chaque année
        *  => 4 tours <=> 4 évènements aléatoires respectant les saisons
        *  while hasNotLost
        */
        int year = 0;
        int seasonCount = 0;
        while(isPlayerWinning()) {
            year += 1;
            while(seasonCount < 4) {
                // Event event = getEvent();
                // displayEvent(event);
                // int playerSolutionChoice = getPlayerChoice();
                // eventImpacts(playerSolutionChoice);
                seasonCount += 1;
            }
            // displayYearEndSummary();
            // displayPlayerYearEndChoices();
            // int playerSaveRepublicChoice = getPlayerChoice();
            // yearEndChoiceImpacts();
            // displayYearEndSummary();
            // foodImpactOnPopulation(checkEnoughFood());
            // addScore(calculateYearEndScore());
        }
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}