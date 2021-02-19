package ressources.game;

import java.util.Scanner;

public class GamePlayerInput {
    public static void pressAnyKeyToContinue()
    {
        System.out.printf("%n%nAppuyez sur la touche 'Entrer' pour continuer...%n");
        try {
            System.in.read();
        } catch(Exception ignored) {}
    }

    public static int getPlayerEventSolutionChoice(int nbChoice) {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= nbChoice) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return getPlayerEventSolutionChoice(nbChoice);
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return getPlayerEventSolutionChoice(nbChoice);
        }
    }

    public static int chooseEndYearOption() {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= GameRules.NB_YEAR_END_OPTIONS) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseEndYearOption();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseEndYearOption();
        }
    }

    public static int chooseFactionToBribe(int nbFactions) {
        Scanner playerInput = new Scanner(System.in);
        String warning = String.format("%nAttention ! Votre choix est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1 && playerChoice <= nbFactions) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseFactionToBribe(nbFactions);
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseFactionToBribe(nbFactions);
        }
    }

    public static int chooseFoodUnitsToBuy(int foodUnitPossibleToBuy) {
        Scanner playerInput = new Scanner(System.in);
        System.out.printf("%nVous pouvez acheter %d unité(s) de nourriture.%n", foodUnitPossibleToBuy);
        System.out.printf("%nEntrez le nombre d'unité de nourriture que vous voulez acheter :%n");
        String warning = String.format("%nAttention ! Votre entrée est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice >= 1) {
                return playerChoice;
            }
            else {
                System.out.println(warning);
                return chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
        }
    }

    public static boolean wantsToPlayAgain() {
        Scanner playerInput = new Scanner(System.in);
        System.out.printf("%nVoulez-vous continuer de jouer ?%n");
        System.out.println("Entrez votre choix :");
        System.out.printf("%n1. Rejouer");
        System.out.printf("%n2. Quitter%n");
        String warning = String.format("%nAttention ! Votre entrée est incorrect");
        try {
            int playerChoice = playerInput.nextInt();
            if(playerChoice == GameRules.END_GAME_CHOICE_START_NEW_GAME) {
                return true;
            }
            if(playerChoice == GameRules.END_GAME_CHOICE_STOP_PLAYING) {
                return false;
            }
            else {
                System.out.println(warning);
                return wantsToPlayAgain();
            }
        } catch (Exception ex) {
            System.out.println(warning);
            return wantsToPlayAgain();
        }
    }
}
