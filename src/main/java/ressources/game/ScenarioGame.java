package ressources.game;

public class ScenarioGame extends Game {

    public ScenarioGame(GameDifficulty gameDifficulty) {
        super(gameDifficulty);
    }

    @Override
    public void play() {
        super.play();
        // Check nb events in seasons
        // si la premiere saison qui commence a moins d'év que les autres c'est mort
        // si la saison avant la premiere
        System.out.printf("%nNom du scénario : %s%n", this.gamePlay.getName());
        System.out.printf("Histoire : %s%n",this.gamePlay.getStory());
        System.out.printf("%nVous commencez avec ces paramètres de jeu : %n");
        displayYearEndSummary(0);
        int seasonCount = 0;
        int eventCount = 1;
        this.gamePlay.nextEvent();
        while(isPlayerWinning()) {
            if(!isScenarioFinished()) {
                System.out.printf("%n%n-- Nous sommes en %s de la %de année --%n", this.gamePlay.getCurrentSeason().capitalize(), year);

                this.gamePlay.displayCurrentEvent(eventCount);
                this.republic.irreversibleEventImpacts(getCurrentEvent());

                int playerSolutionChoice = PlayerInput.getPlayerEventSolutionChoice(getCurrentEvent().getNbChoices());
                playerChoiceImpacts(playerSolutionChoice);
                addScore(getSeasonEndScore());

                seasonCount += 1;
                eventCount += 1;
                this.gamePlay.nextSeason();
                this.gamePlay.nextEvent();
                if(isEndOfYear(seasonCount)) {
                    // TODO à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini
                    handleEndOfYear();
                    // ^à mettre dans une fonction handle end year pour pouvoir l'utiliser quand le scénario est fini

                    this.year += 1;
                    PlayerInput.pressAnyKeyToContinue();
                }
            }
            else {
                handleEndOfYear();
                System.out.println("Le scénario est fini. Vous avez gagné la partie.");
                System.out.println("Voulez-vous continuer en mode bac à sable, ou arrêter de jouer ?");
                break;
            }
        }
        System.out.printf("%n%nVotre score est de %f", getEndGameScore(year));

    }

    public boolean isScenarioFinished() {
        return this.gamePlay.isScenarioFinished();
    }

    @Override
    public String toString() {
        return "scénario";
    }
}