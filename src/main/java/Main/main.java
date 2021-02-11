package Main;

import ressources.game.Game;
import ressources.game.GameParameters;
import ressources.game.SandboxMode;
import ressources.game.ScenarioMode;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner playerInput = new Scanner(System.in);
        GameParameters gameParameters = new GameParameters();
        Game.introduction();

        gameParameters.displayGameModeInstructions();
        int gameModeChosen = gameParameters.chooseGameMode();
        gameParameters.setGameMode(gameModeChosen);

        gameParameters.displayGameDifficultyInstructions();
        int gameDifficultyChosen = gameParameters.chooseGameDifficulty();
        gameParameters.setGameDifficulty(gameDifficultyChosen);

        // TODO all comments

        // SANDBOX
        if(gameModeChosen == 1) {
            Game game = new Game(gameParameters, "SandboxEventFileName", gameDifficultyChosen);
            // LET'S GO
            game.play(); // polymorphism
        }
        // SCENARIO
        else {
            // Choose scenario
            String scenarioFile = "getTheCorrespondingScenario";
            Game game = new Game(gameParameters, scenarioFile, gameDifficultyChosen);

            // LET'S GO
            game.play(); // polymorphism
        }

    }
}
