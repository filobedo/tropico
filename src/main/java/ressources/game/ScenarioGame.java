package ressources.game;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void launchGame() {
        super.launchGame();
        System.out.printf("%nNom du scénario : %s%n", this.gamePlay.getName());
        System.out.printf("Histoire : %s%n",this.gamePlay.getStory());
        this.gamePlay.nextEvent();
        while(!isScenarioFinished()) {
            if(isPlayerWinning()) {
                playGame();
            }
            else {
                System.out.printf("%n%n%nLe scénario n'est pas fini mais vous avez perdu...%n");
                if(!canCatchUp()) {
                    displayPlayerLostAndCannotCatchUp();
                    PlayerInput.pressAnyKeyToContinue();
                    break;
                }
                else {
                    displayPlayerLostButCanCatchUp();
                    PlayerInput.pressAnyKeyToContinue();
                    endOfYearConsequencesAndChoices();
                    if(!isPlayerWinning()) {
                        System.out.printf("%n%nDommage, malgré ce dernier effort, vous avez perdu la partie...%n");
                        PlayerInput.pressAnyKeyToContinue();
                        break;
                    }
                }
            }
        }
        System.out.printf("%n==========================%n");
        System.out.printf("%n- Voici votre bilan final : -%n");
        displaySummary();
        if(isScenarioFinished()) {
            if(!isPlayerWinning()) {
                displayPlayerFinishedScenarioButLost();
            }
            else {
                displayPlayerWon();
            }
        }
    }

    public boolean isScenarioFinished() {
        return this.gamePlay.isScenarioFinished();
    }

    public boolean canCatchUp() {
        // You cannot get score to positive value by bribing faction
        // You cannot get global satisfaction rising because no population
        return isScorePositive() && this.republic.isThereAnyPopulation();
    }

    public void displayPlayerLostAndCannotCatchUp() {
        System.out.printf("%nUn dernier bilan vous sera affiché mais le jeu est fini pour vous. %nGAME OVER.%n");
    }

    public void displayPlayerLostButCanCatchUp() {
        System.out.printf("%nUn dernier bilan vous sera affiché.%nCependant, peut-être" +
                " que vous pouvez encore sauver votre république en essayant" +
                " d'améliorer la satisfaction globale de votre république...");
    }

    public void displayPlayerFinishedScenarioButLost() {
        System.out.println("Le scénario est fini. Mais vous avez perdu la partie.");
        System.out.println("Dommage, les derniers évènements vous ont mis dans le rouge.");
        System.out.println("Réessayez une prochaine fois !");
    }

    public void displayPlayerWon() {
        System.out.println("Le scénario est fini. Vous avez gagné la partie.");
        System.out.println("Félicitations !");
        // TODO
        System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
    }
}