package Main;

import ressources.game.*;

import java.io.File;

public class main {

    public static void main(String[] args) {
        Game.displayIntroduction();
        GameParameters gameParameters = new GameParameters();
        gameParameters.askPlayerGameDifficultyAndMode();

        String gamePropertiesFilePath = "";
        Game game = null;

        if(gameParameters.isGameModeSandbox()) {
            game = new SandboxGame(gameParameters.getGameDifficulty());
            gamePropertiesFilePath = gameParameters.sandboxFilePath;
        }
        else if(gameParameters.isGameModeScenario()) {
            game = new ScenarioGame(gameParameters.getGameDifficulty());
            File[] scenarioList = gameParameters.getScenarioList();
            gameParameters.displayScenarioListInstructions(scenarioList);
            gamePropertiesFilePath = gameParameters.chooseScenario(scenarioList);
        }
        else {
            System.exit(0);
        }
        System.out.println(gamePropertiesFilePath);


        // LET'S GO
//        JSONObject scenario = gameParameters.openScenario(gamePropertiesFilePath);
        game.load(gamePropertiesFilePath);
        game.play();
    }
}
