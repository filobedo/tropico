package ressources.game;
import ressources.economy.Tresory;
import ressources.event.Event;
import ressources.factions.Population;

public class Game {
    private Tresory treasury;
    private Population population;
    private Event event;
    private GameParameters gameParameters;

    public Game(GameParameters gameParameters, String file, int difficulty) {
        this.treasury = new Tresory();
        this.gameParameters = gameParameters;
        try {
//            this.population = new Population("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTreasury(Tresory treasury) {
        this.treasury = treasury;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public static void introduction() {
        String welcome = "\"Bonjour et bienvenue dans un jeu vidéo à la croisée entre Tropico et Reigns !\"";
        String gameRole = "\"Vous incarnerez un jeune dictateur en herbe sur une île tropicale, fraîchement élu comme Président.";
        String gameGoal = "Vous aurez la lourde tâche de faire prospérer cette nouvelle mini-république.\"";
        System.out.printf("\n%s%n", welcome);
        System.out.println("===================");
        System.out.printf("%s\n%s%n", gameRole, gameGoal);
        System.out.println("===================");
    }

    public void play() {
        System.out.printf("\nVous avez lancé une partie en mode \"%s\" ", gameParameters.getGameMode().toString());
        System.out.printf("en difficulté \"%s\".\n", gameParameters.getGameDifficulty());
        System.out.println("\nLancement du jeu...");
        gameParameters.getGameMode().play();
    }

    /*public void bribe(Faction faction) {
        if(faction.getBribePrice() <= this.treasury) {
            faction.bribe();
            useTreasury(faction.getBribePrice());
            events.notify("bribe", faction);
        }
        else {
            System.out.println("You don't have enough money to bribe " + faction.getName() + " faction!");
        }
    }*/
}