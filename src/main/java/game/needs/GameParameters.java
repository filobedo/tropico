package game.needs;

import game.Game;
import game.SandboxGame;
import game.ScenarioGame;
import org.json.JSONObject;
import org.json.JSONTokener;
import parser.ParsingKeys;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GameParameters {
    private String playerName;
    private GameDifficulty gameDifficulty;
    private String gameModeClass;
    private String filePath;

    private final String or = "ou";
    private final String cancel = "Annuler";
    private final String quit = "Quitter";
    private final String chooseCancel = String.format("Tapez '0' pour %s", cancel);
    private final String chooseSandboxGame = "Tapez '1' pour le mode bac à sable";
    private final String chooseScenarioGame = "Tapez '2' pour le mode scenario";

    private final String chooseEasyDifficulty = "Tapez '1' pour le mode facile";
    private final String chooseMediumDifficulty = "Tapez '2' pour le mode normal";
    private final String chooseHardDifficulty = "Tapez '3' pour le mode dur";

//    public final String resourcesPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
    public final String scenariosResourcePath = "scenarios/";
    public final String sandboxFilePath = "sandbox/sandboxProperties.json";

    public void askPlayerGameModeAndDifficulty() {
        this.playerName = askPlayerName();

        displayGameModeInstructions();
        int gameModeIndex = chooseGameMode();
        if(wantsToCancel(gameModeIndex)) {
            askPlayerGameModeAndDifficulty();
            return;
        }
        else {
            String chosenGameMode = getGameModeClass(gameModeIndex);
            setGameModeClass(chosenGameMode);
        }
        displayGameDifficultyInstructions();
        int gameDifficultyIndex = chooseGameDifficulty();
        if(wantsToCancel(gameDifficultyIndex)) {
            askPlayerGameModeAndDifficulty();
        }
        else {
            GameDifficulty chosenGameDifficulty = GameDifficulty.values()[gameDifficultyIndex - 1];
            setGameDifficulty(chosenGameDifficulty);
        }
    }

    public static String askPlayerName() {
        Scanner playerInput = new Scanner(System.in);
        String warning = "Attention vous devez entrer des caractères alphanumériques !";
        System.out.printf("%n%nEntrez votre pseudo ou votre prénom : %n");
        try {
            String playerChoice = playerInput.nextLine();
            if(playerChoice.length() > 1) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return askPlayerName();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return askPlayerName();
        }
    }

    public boolean wantsToCancel(int choice) {
        return choice == GameInputOptions.CANCEL;
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public void setGameModeClass(String gameModeClass) {
        this.gameModeClass = gameModeClass;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public String getFilePath() {
        return filePath;
    }

    public void displayGameModeInstructions() {
        String instructions = String.format("%nChoisissez votre mode de jeu :%n%s%n%s%n%s%n%s", chooseCancel, chooseSandboxGame, or, chooseScenarioGame);
        System.out.println(instructions);
    }

    public int chooseGameMode() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Ce mode de jeu n'existe pas :%n%s%n%s%n%s%n%s", chooseCancel, chooseSandboxGame, or, chooseScenarioGame);
        try {
            int playerChoice = playerInput.nextInt();
            if(isPlayerGameModeChoiceCorrect(playerChoice)) {
                return playerChoice;
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
        return choice == GameInputOptions.CANCEL || choice == GameInputOptions.GAME_MODE_SCENARIO || choice == GameInputOptions.GAME_MODE_SANDBOX;
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
        String instructions = String.format("%nChoisissez votre difficulté de jeu :%n%s%n%s%n%s%n%s%n%s", chooseCancel, chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
        System.out.println(instructions);
    }

    public int chooseGameDifficulty() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Cette difficulté n'existe pas :%n%s%n%s%n%s%n%s%n%s", chooseCancel, chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
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
        return choice == GameInputOptions.CANCEL || choice == GameDifficulty.EASY.ordinal() + 1 || choice == GameDifficulty.NORMAL.ordinal() + 1 || choice == GameDifficulty.HARD.ordinal() + 1;
    }

    public String getScenarioListInstructions(List<File> scenarios) {
        int countScenario = 0;
        StringBuilder instructions = new StringBuilder(String.format("%nVeuillez choisir parmis ces %d scénarios :%n", scenarios.size()));
        instructions.append(String.format("%d. %s%n", countScenario, quit));
        for (File scenario: scenarios) {
            if(scenario.isFile()) {
                countScenario += 1;
                String currentScenario = String.format("%d. %s%n",countScenario, getScenarioName(scenario));
                instructions.append(currentScenario);
            }
        }
        return instructions.toString();
    }

    public String getScenarioName(File file) {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JSONTokener token = new JSONTokener(reader);
            JSONObject scenario = new JSONObject(token);
            return scenario.get(ParsingKeys.name).toString();
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + file.getPath());
        }
    }

    public String chooseScenario(List<File> scenarios) {
        int nbScenario = scenarios.size();
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Ce scénario n'existe pas !%s", getScenarioListInstructions(scenarios));
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice == GameInputOptions.QUIT) {
                Game.shutDown();
            }
            if(isPlayerScenarioChoiceCorrect(playerChoice, nbScenario)) {
                return scenarios.get(playerChoice - 1).getPath();
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
        return playerChoice >= 0 && playerChoice <= nbScenario;
    }

    public Game getGameAccordingToChosenGameParameters() throws ClassNotFoundException, IOException, URISyntaxException {
        if(isGameModeSandbox()) {
            setFilePath(sandboxFilePath);
            return new SandboxGame(this.gameDifficulty, this.playerName);
        }
        else if(isGameModeScenario()) {
            List<File> scenarioList = getScenarioList();
            displayScenarioListInstructions(scenarioList);

            setFilePath(chooseScenario(scenarioList));
            return new ScenarioGame(this.gameDifficulty, this.playerName);
        }
        else {
            throw new ClassNotFoundException("Game mode not found.");
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<File> getScenarioList() throws IOException, URISyntaxException {
        String path = this.scenariosResourcePath;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        List<File> scenarios = new ArrayList<>();

        // Run with JAR file
        if(jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> jarEntries = jar.entries(); //gives ALL entries in jar

            while(jarEntries.hasMoreElements()) {
                final String name = jarEntries.nextElement().getName();
                if(name.startsWith(path) && !name.endsWith(path)) {
                    URI fullPath = this.getClass().getResource("/" + name).toURI();
                    scenarios.add(new File(fullPath.toString()));
                }
            }
            jar.close();
        }

        // Run with IDE
        else {
            final URL url = this.getClass().getResource("/" + path);
            if (url != null) {
                try {
                    final File scenarioFiles = new File(url.toURI());
                    Collections.addAll(scenarios, Objects.requireNonNull(scenarioFiles.listFiles()));
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return scenarios;
    }

    public void displayScenarioListInstructions(List<File> scenarios) {
        System.out.print(getScenarioListInstructions(scenarios));
    }

    public boolean isGameModeSandbox() {
        return this.gameModeClass.equals(SandboxGame.class.getSimpleName());
    }

    public boolean isGameModeScenario() {
        return this.gameModeClass.equals(ScenarioGame.class.getSimpleName());
    }
}
