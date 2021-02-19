package ressources.game;

import exceptions.MissingEventsException;

public class SandboxGame extends Game {

    public SandboxGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void launchGame() throws MissingEventsException {
        super.launchGame();
        System.out.printf("%n%s%n", this.gamePlay.getName());
        System.out.printf("%s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(true) {
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