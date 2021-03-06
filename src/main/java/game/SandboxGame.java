package game;

import exceptions.MissingEventsException;
import game.needs.GameDifficulty;

import java.net.URL;
import java.util.Objects;

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
        if(gamePlay.canPlayEvents()) {
            this.gamePlay.nextEvent();
            while(isPlayerWinning()) {
                playGame();
                if(!isPlayerWinning()) {
                    if(didPlayerFailedCatchingUp()) {
                        break;
                    }
                }
            }
            finalSummary();
            System.out.printf("%nVotre partie en mode bac à sable est terminée.%n");
        }
        else {
            throw new MissingEventsException("At least one season has no event.");
        }
    }

    @Override
    public String getSavePath() {
        URL resourceUrl = this.getClass().getClassLoader().getResource("");
        String resourcePath = Objects.requireNonNull(resourceUrl).getPath();
        return String.format("%s/sandbox/saves/player_%s.json", resourcePath, getPlayerName());
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}