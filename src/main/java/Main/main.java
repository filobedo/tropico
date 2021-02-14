package Main;

import org.json.JSONObject;
import org.json.JSONTokener;
import ressources.game.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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


        // LET'S GO
//        JSONObject scenario = gameParameters.openScenario(gamePropertiesFilePath);
        game.load(gamePropertiesFilePath);
        game.play();
    }
}
