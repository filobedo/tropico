package ressources.game;

import org.json.JSONObject;
import org.json.JSONTokener;
import ressources.parser.ParsingKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class GameParameters {
    private GameDifficulty gameDifficulty;
    private String gameModeClass;
    private String filePath;

    private final String or = "ou";
    private final String chooseSandboxGame = "Tapez '1' pour le mode bac à sable";
    private final String chooseScenarioGame = "Tapez '2' pour le mode scenario";

    private final String chooseEasyDifficulty = "Tapez '1' pour le mode facile";
    private final String chooseMediumDifficulty = "Tapez '2' pour le mode normal";
    private final String chooseHardDifficulty = "Tapez '3' pour le mode dur";

    public final String scenariosResourcePath = "src/main/resources/scenarios";
    public final String sandboxFilePath = "src/main/resources/sandBoxProperties.json";


    public void askPlayerGameDifficultyAndMode() {
        displayGameModeInstructions();
        int gameModeIndex = chooseGameMode();
        String chosenGameMode = getGameModeClass(gameModeIndex);
        setGameModeClass(chosenGameMode);

        displayGameDifficultyInstructions();
        int gameDifficultyIndex = chooseGameDifficulty();
        GameDifficulty chosenGameDifficulty = GameDifficulty.values()[gameDifficultyIndex - 1];
        setGameDifficulty(chosenGameDifficulty);
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public void setGameModeClass(String gameModeClass) {
        this.gameModeClass = gameModeClass;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public String getGameModeClass() {
        return gameModeClass;
    }

    public String getFilePath() {
        return filePath;
    }

    public void displayGameModeInstructions() {
        String instructions = String.format("%nChoisissez votre mode de jeu :%n%s%n%s%n%s", chooseSandboxGame, or, chooseScenarioGame);
        System.out.println(instructions);
    }

    public int chooseGameMode() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Ce mode de jeu n'existe pas :%n%s%n%s%n%s", chooseSandboxGame, or, chooseScenarioGame);
        try {
            int playerChoice = playerInput.nextInt();
            if(isPlayerGameModeChoiceCorrect(playerChoice)) {
                return playerChoice; // TODO ENUM ?
            }
            else {
                System.out.println(warning);
                return chooseGameMode();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseGameMode();
        }
    }

    public boolean isPlayerGameModeChoiceCorrect(int choice) {
        return choice == 1 || choice == 2;
    }

    public String getGameModeClass(int playerGameModeChoice) {
        if(playerGameModeChoice == 1) {
            return SandboxGame.class.getSimpleName();
        }
        if(playerGameModeChoice == 2) {
            return ScenarioGame.class.getSimpleName();
        }
        return "";
    }

    public void displayGameDifficultyInstructions() {
        String instructions = String.format("%nChoisissez votre difficulté de jeu :%n%s%n%s%n%s%n%s", chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
        System.out.println(instructions);
    }

    public int chooseGameDifficulty() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Cette difficulté n'existe pas :%n%s%n%s%n%s%n%s", chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
        try {
            int playerChoice = playerInput.nextInt();
            if(isPlayerGameDifficultyChoiceCorrect(playerChoice)) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseGameDifficulty();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseGameDifficulty();
        }
    }

    public boolean isPlayerGameDifficultyChoiceCorrect(int choice) {
        return choice == 1 || choice == 2 || choice == 3;
    }

    public File[] getScenarioList() {
        File directory = new File(scenariosResourcePath);
        return directory.listFiles();
    }

    public void displayScenarioListInstructions(File[] scenarios) {
        System.out.print(getScenarioListInstructions(scenarios));
    }

    public String getScenarioListInstructions(File[] scenarios) {
        int nbScenario = scenarios.length;
        int countScenario = 1;
        StringBuilder instructions = new StringBuilder(String.format("%nVeuillez choisir parmis ces %d scénarios :%n", nbScenario));
        for (File scenario: scenarios) {
            String currentScenario = String.format("%d. %s%n",countScenario, getScenarioName(scenario));
            instructions.append(currentScenario);
            countScenario += 1;
        }
        return instructions.toString();
    }

    public String getScenarioName(File file) {
        try (InputStream is = new FileInputStream(file)) {
            JSONTokener token = new JSONTokener(is);
            JSONObject scenario = new JSONObject(token);
            return scenario.get(ParsingKeys.name).toString();
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + file.getPath());
        }
    }

    public String chooseScenario(File[] scenarios) {
        int nbScenario = scenarios.length;
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Ce scénario n'existe pas !%s", getScenarioListInstructions(scenarios));
        try {
            int playerChoice = playerInput.nextInt();
            if(isPlayerScenarioChoiceCorrect(playerChoice, nbScenario)) {
                return scenarios[playerChoice - 1].getPath();
            }
            else {
                System.out.println(warning);
                return chooseScenario(scenarios);
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseScenario(scenarios);
        }
    }

    public boolean isPlayerScenarioChoiceCorrect(int playerChoice, int nbScenario) {
        return playerChoice >= 1 && playerChoice <= nbScenario;
    }

    public boolean isGameModeSandbox() {
        return this.gameModeClass.equals(SandboxGame.class.getSimpleName());
    }

    public boolean isGameModeScenario() {
        return this.gameModeClass.equals(ScenarioGame.class.getSimpleName());
    }
}
