package game.needs;

import java.util.Scanner;

public class GamePlayerInput {
    public static final String incorrectInputWarning = "Attention ! Votre choix est incorrect";

    public static void pressAnyKeyToContinue()
    {
        System.out.printf("%n%nAppuyez sur la touche 'Entrer' pour continuer...%n");
        try {
            System.in.read();
        } catch(Exception ignored) {}
    }

    public static boolean doesPlayerWantsToUseGameSave() {
        System.out.printf("%n%nVoulez-vous reprendre votre dernière sauvegarde ?%n");
        System.out.printf("%n1. Oui");
        System.out.printf("%n2. Non%n");
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice == GameInputOptions.YES) {
                return true;
            }
            else if(playerChoice == GameInputOptions.NO) {
                return false;
            }
            else {
                System.out.println(incorrectInputWarning);
                return doesPlayerWantsToUseGameSave();
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return doesPlayerWantsToUseGameSave();
        }
    }

    public static int getPlayerEventSolutionChoice(int nbChoice) {
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= nbChoice) {
                return playerChoice;
            }
            else {
                System.out.println(incorrectInputWarning);
                return getPlayerEventSolutionChoice(nbChoice);
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return getPlayerEventSolutionChoice(nbChoice);
        }
    }

    public static void displayScaredOrNotScared(String firstOption) {
        System.out.printf("%nÊtes-vous prêt à perdre ?%n");
        System.out.println("Entrez votre choix :");
        System.out.printf("%n1. %s", firstOption);
        System.out.printf("%n2. Fuir%n");
    }

    public static void displayOptionOrQuit(String firstOption) {
        System.out.printf("%nVoulez-vous continuer de jouer ?%n");
        System.out.println("Entrez votre choix :");
        System.out.printf("%n1. %s", firstOption);
        System.out.printf("%n2. Quitter%n");
    }

    public static boolean wantsToQuitGame() {
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice == 1) {
                return false;
            }
            if(playerChoice == 2) {
                return true;
            }
            System.out.println(incorrectInputWarning);
            return wantsToQuitGame();
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return wantsToQuitGame();
        }
    }

    public static void displayContinueOrSaveAndOrQuit() {
        System.out.printf("%nVoulez-vous continuer de jouer ?%n");
        System.out.println("Entrez votre choix :");
        System.out.printf("%n1. Continuer");
        System.out.printf("%n2. Sauvegarder et Quitter");
        System.out.printf("%n3. Quitter%n");
    }

    public static int makeContinueOrSaveAndOrQuitChoice() {
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(isEndYearContinueOrSaveAndOrQuitChoiceCorrect(playerChoice)) {
                return playerChoice;
            }
            System.out.println(incorrectInputWarning);
            return makeContinueOrSaveAndOrQuitChoice();
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return makeContinueOrSaveAndOrQuitChoice();
        }
    }

    public static boolean isEndYearContinueOrSaveAndOrQuitChoiceCorrect(int playerChoice) {
        return playerChoice == GameInputOptions.END_YEAR_CONTINUE
                || playerChoice == GameInputOptions.END_YEAR_SAVE_AND_QUIT
                || playerChoice == GameInputOptions.END_YEAR_QUIT;
    }

    public static int chooseEndYearOption() {
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= GameInputOptions.NB_YEAR_END_OPTIONS) {
                return playerChoice;
            }
            else {
                System.out.println(incorrectInputWarning);
                return chooseEndYearOption();
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return chooseEndYearOption();
        }
    }

    public static int chooseFactionToBribe(int nbFactions) {
        Scanner playerInput = new Scanner(System.in);
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= nbFactions) {
                return playerChoice;
            }
            else {
                System.out.println(incorrectInputWarning);
                return chooseFactionToBribe(nbFactions);
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return chooseFactionToBribe(nbFactions);
        }
    }

    public static int chooseFoodUnitsToBuy(int foodUnitPossibleToBuy) {
        Scanner playerInput = new Scanner(System.in);
        System.out.printf("%nVous pouvez acheter %d unité(s) de nourriture.%n", foodUnitPossibleToBuy);
        System.out.printf("%nEntrez le nombre d'unité de nourriture que vous voulez acheter :%n");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 0) {
                return playerChoice;
            }
            else {
                System.out.println(incorrectInputWarning);
                return chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
        }
    }

    public static boolean wantsToPlayAgain() {
        Scanner playerInput = new Scanner(System.in);
        GamePlayerInput.displayOptionOrQuit("Rejouer");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice == GameInputOptions.END_GAME_CHOICE_START_NEW_GAME) {
                return true;
            }
            if(playerChoice == GameInputOptions.END_GAME_CHOICE_STOP_PLAYING) {
                return false;
            }
            else {
                System.out.println(incorrectInputWarning);
                return wantsToPlayAgain();
            }
        } catch (Exception ex) {
            System.out.println(incorrectInputWarning);
            return wantsToPlayAgain();
        }
    }
}
