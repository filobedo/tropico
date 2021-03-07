package game;

import game.needs.GameDifficulty;

import java.net.URL;
import java.util.Objects;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty, String playerName) {
        super(gameDifficulty, playerName);
    }

    /**
     * While the scenario is not finished and the player's score is above a certain amount
     * and his global satisfaction rate is above a certain percentage
     * The player keeps playing
     */
    @Override
    public void playsGame() {
        while(keepsPlaying()) {
            if(isPlayerWinning()) {
                playCurrentGameTurn();
            }
            else {
                System.out.printf("%n%n%nLe scénario n'est pas fini mais vous avez perdu...%n");
                if(didPlayerFailedCatchingUp()) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean keepsPlaying() {
        return !isScenarioFinished();
    }

    /**
     * According to the scenario, his score and his global satisfaction
     * This function will display if he won, he lost, he finished the scenario etc...
     * Then display that the party is finished
     */
    @Override
    public void handlePlayerEndGame() {
        if(isScenarioFinished()) {
            if(isPlayerWinning()) {
                displayPlayerWon();
            }
            else {
                displayPlayerFinishedScenarioButLost();
            }
            System.out.printf("%nVous avez fini le scénario : %s%n", gamePlay.getName());
        }
        else {
            displayGameIsFinished();
        }
    }

    public boolean isScenarioFinished() {
        return this.gamePlay.isScenarioFinished();
    }

    public void displayPlayerFinishedScenarioButLost() {
        System.out.printf("%n%nLe scénario est fini. Mais vous avez perdu la partie.%n");
        System.out.println("Dommage, les derniers évènements vous ont mis dans le rouge.");
        System.out.println("Réessayez une prochaine fois !");
    }

    public void displayPlayerWon() {
        System.out.println("Le scénario est fini. Vous avez gagné la partie.");
        System.out.println("Félicitations !");
    }

    @Override
    public String getSavePath() {
        URL resourceUrl = this.getClass().getClassLoader().getResource("");
        String resourcePath = Objects.requireNonNull(resourceUrl).getPath();
        return String.format("%s/scenarios/saves/player_%s/%s/%s", resourcePath, getPlayerName(), getGameDifficulty().name(), getFileName());
    }

    @Override
    public String toString() {
        return "scénario";
    }
}