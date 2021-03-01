package game;

import exceptions.MissingEventsException;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    /**
     * While the scenario is not finished and the player's score is above a certain amount
     * and his global satisfaction rate is above a certain percentage
     * The player keeps playing
     */
    @Override
    public void launchGame() throws MissingEventsException {
        super.launchGame();
        if(this.gamePlay.canPlayEvents()) {
            this.gamePlay.nextEvent();
            while(!isScenarioFinished()) {
                if(isPlayerWinning()) {
                    playGame();
                }
                else {
                    System.out.printf("%n%n%nLe scénario n'est pas fini mais vous avez perdu...%n");
                    if(!didPlayerSucceedCatchingUp()) {
                        break;
                    }
                }
            }
            finalSummary();
            handlePlayerEndGame();
        }
        else {
            throw new MissingEventsException("Scenario cannot played fully.");
        }
    }

    /**
     * According to the scenario, his score and his global satisfaction
     * This function will display if he won, he lost, he finished the scenario etc...
     * Then display that the party is finished
     */
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
            System.out.printf("%nVotre partie en scénario est terminée.%n");
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
    public String toString() {
        return "scénario";
    }
}