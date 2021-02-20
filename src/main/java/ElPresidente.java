import game.*;

public class ElPresidente {
    public static void main(String[] args) {
        boolean wantsToPlayAgain = true;
        while(wantsToPlayAgain) {
            Game.displayIntroduction();
            GameParameters gameParameters = new GameParameters();
            gameParameters.askPlayerGameModeAndDifficulty();
            try {
                Game game = gameParameters.getGameAccordingToChosenGameParameters();
                game.load(gameParameters);
                game.launchGame();
                wantsToPlayAgain = GamePlayerInput.wantsToPlayAgain();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        Game.shutDown();
    }
}
