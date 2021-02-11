package Main;

import ressources.game.*;

import java.io.File;

public class main {
    public static void main(String[] args) {
        Game game = null;
        game.displayIntroduction();
        GameParameters gameParameters = new GameParameters();
        gameParameters.askPlayerGameDifficultyAndMode();

        String gamePropertiesFilePath = "";
        if(gameParameters.isGameModeSandbox()) {
            game = new SandboxGame(gameParameters.getGameDifficulty());
            gamePropertiesFilePath = gameParameters.sandboxFilePath;
        }
        else if(gameParameters.isGameModeScenario()) {
            game = new ScenarioGame(gameParameters.getGameDifficulty(), "");
            File[] scenarioList = gameParameters.getScenarioList();
            gameParameters.displayScenarioListInstructions(scenarioList);
            gamePropertiesFilePath = gameParameters.chooseScenario(scenarioList);
        }
        else {
            System.exit(0);
        }
        System.out.println(gamePropertiesFilePath);

        game.loadGameProperties(gamePropertiesFilePath);

        // LET'S GO
        game.play();
    }
}
