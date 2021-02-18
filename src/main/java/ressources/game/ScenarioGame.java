package ressources.game;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void launchGame() {
        super.launchGame();
        System.out.printf("%nNom du scénario : %s%n", this.gamePlay.getName());
        System.out.printf("Histoire : %s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(isPlayerWinning()) {
            if(!isScenarioFinished()) {
                playGame();
            }
            else {
                handleEndOfYear();
                System.out.println("Le scénario est fini. Vous avez gagné la partie.");
                System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
                break;
            }
        }
        // TODO Win or lose
        handleEndOfYear();
        System.out.printf("%n%nVotre score est de %f", getEndGameScore(year));
    }

    public boolean isScenarioFinished() {
        return this.gamePlay.isScenarioFinished();
    }

    @Override
    public String toString() {
        return "scénario";
    }
}