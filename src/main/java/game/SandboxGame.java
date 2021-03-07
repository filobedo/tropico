package game;

import game.needs.GameDifficulty;

import java.net.URL;
import java.util.Objects;

public class SandboxGame extends Game {

    public SandboxGame(GameDifficulty gameDifficulty, String playerName) {
        super(gameDifficulty, playerName);
    }

    /**
     * While the player's score is above a certain amount
     * and his global satisfaction rate is above a certain percentage
     * The player keeps playing
     */
    @Override
    public void playsGame() {
        while(keepsPlaying()) {
            playCurrentGameTurn();
            if(!isPlayerWinning()) {
                if(didPlayerFailedCatchingUp()) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean keepsPlaying() {
        return isPlayerWinning();
    }

    /**
     * Display that the party is finished
     */
    @Override
    public void handlePlayerEndGame() {
        displayGameIsFinished();
    }

    @Override
    public String getSavePath() {
        URL resourceUrl = this.getClass().getClassLoader().getResource("");
        String resourcePath = Objects.requireNonNull(resourceUrl).getPath();
        return String.format("%s/sandbox/saves/player_%s/%s.json", resourcePath, getPlayerName(), getGameDifficulty().name());
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}