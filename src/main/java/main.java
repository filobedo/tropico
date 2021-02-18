import ressources.game.*;

public class main {

    public static void main(String[] args) {
        Game.displayIntroduction();
        GameParameters gameParameters = new GameParameters();
        gameParameters.askPlayerGameModeAndDifficulty();

        Game game = null;
        try {
            game = gameParameters.getGameAccordingToChosenGameParameters();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        game.load(gameParameters);
        try {
            game.launchGame();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
