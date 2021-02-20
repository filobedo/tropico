package game;

import exceptions.MissingEventsException;

public class SandboxGame extends Game {

    public SandboxGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    /**
     * While the player's score is above a certain amount
     * and his global satisfaction rate is above a certain percentage
     * The player keeps playing
     */
    @Override
    public void launchGame() throws MissingEventsException {
        super.launchGame();
        System.out.printf("%n%s%n", this.gamePlay.getName());
        System.out.printf("%s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(isPlayerWinning()) {
            playGame();
            if(!isPlayerWinning()) {
                if(!handlePlayerCatchingUp()) {
                    break;
                }
            }
        }
        finalSummary();
        System.out.printf("%nVotre partie en mode bac à sable est terminée.%n");
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}