import ressources.game.*;

public class ElPresidente {

    public static void main(String[] args) {
        Game.displayIntroduction();
        GameParameters gameParameters = new GameParameters();
        gameParameters.askPlayerGameModeAndDifficulty();

        try {
            Game game = gameParameters.getGameAccordingToChosenGameParameters();
            game.load(gameParameters);
            game.launchGame();
        } catch (Exception ex) {
            ex.printStackTrace();
            Game.shutDown();
            System.exit(0);
        }
    }
}
