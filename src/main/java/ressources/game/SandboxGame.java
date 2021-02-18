package ressources.game;

public class SandboxGame extends Game {

    public SandboxGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void launchGame() {
        super.launchGame();
        System.out.printf("%n%s%n", this.gamePlay.getName());
        System.out.printf("%s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(isPlayerWinning()) {
            playGame();
        }
        handleEndOfYear();
        System.out.println("Le scénario est fini. Vous avez gagné la partie.");
        System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
        System.out.printf("%n%nVotre score est de %f", getEndGameScore());
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}