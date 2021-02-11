package ressources.game;

import java.util.Scanner;

public class GameParameters {
    private GameMode gameMode;
    private GameDifficulty gameDifficulty;

    private final String or = "ou";
    private final String chooseSandboxGame = "Tapez '1' pour le mode bac à sable";
    private final String chooseScenarioGame = "Tapez '2' pour le mode scenario";

    private final String chooseEasyDifficulty = "Tapez '1' pour le mode facile";
    private final String chooseMediumDifficulty = "Tapez '2' pour le mode normal";
    private final String chooseHardDifficulty = "Tapez '3' pour le mode dur";

    public GameMode getGameMode() {
        return gameMode;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameMode(int indexGameMode) {
        if(indexGameMode == 1) {
            this.gameMode = new SandboxMode();
        }
        if(indexGameMode == 2) {
            this.gameMode = new ScenarioMode();
        }
    }

    public void setGameDifficulty(int indexGameDifficulty) {
        this.gameDifficulty = GameDifficulty.values()[indexGameDifficulty - 1];
    }

    public void displayGameModeInstructions() {
        String instructions = String.format("\nChoisissez votre mode de jeu :\n%s\n%s\n%s", chooseSandboxGame, or, chooseScenarioGame);
        System.out.println(instructions);
    }

    public int chooseGameMode() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("Attention ! Ce mode de jeu n'existe pas :\n%s\n%s\n%s", chooseSandboxGame, or, chooseScenarioGame);
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

    public void displayGameDifficultyInstructions() {
        String instructions = String.format("\nChoisissez votre mode de jeu :\n%s\n%s\n%s\n%s", chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
        System.out.println(instructions);
    }

    public int chooseGameDifficulty() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("Attention ! Cette difficulté n'existe pas :\n%s\n%s\n%s\n%s", chooseEasyDifficulty, chooseMediumDifficulty, or, chooseHardDifficulty);
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
}
