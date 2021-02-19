package ressources.game;

import exceptions.MissingEventsException;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void launchGame() throws MissingEventsException {
        super.launchGame();
        this.gamePlay.canPlayEvents();
        System.out.printf("%nNom du scénario : %s%n", this.gamePlay.getName());
        System.out.printf("Histoire : %s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(!isScenarioFinished()) {
            if(isPlayerWinning()) {
                playGame();
            }
            else {
                System.out.printf("%n%n%nLe scénario n'est pas fini mais vous avez perdu...%n");
                if(!handlePlayerCatchingUp()) {
                    break;
                }
            }
        }
        finalSummary();
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
}