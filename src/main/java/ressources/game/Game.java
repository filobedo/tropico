package ressources.game;
import ressources.economy.Tresory;
import ressources.event.Event;
import ressources.factions.Faction;
import ressources.factions.Population;

public abstract class Game {
    private double score;
    private Tresory treasury;
    private Population population;
    private Event event; // EventS ?
    private final GameDifficulty gameDifficulty;

    public Game(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public double getScore() {
        return score;
    }

    public Tresory getTreasury() {
        return treasury;
    }

    public Population getPopulation() {
        return population;
    }

    public Event getEvent() {
        return event;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setScore(double score) {
        // TODO score négatif possible ?
        this.score = score;
    }

    public static void displayIntroduction() {
        String welcome = "\"Bonjour et bienvenue dans un jeu vidéo à la croisée entre Tropico et Reigns !\"";
        String gameRole = "\"Vous incarnerez un jeune dictateur en herbe sur une île tropicale, fraîchement élu comme Président.";
        String gameGoal = "Vous aurez la lourde tâche de faire prospérer cette nouvelle mini-république.\"";
        System.out.printf("\n%s%n", welcome);
        System.out.println("===================");
        System.out.printf("%s\n%s%n", gameRole, gameGoal);
        System.out.println("===================");
    }

    public void loadGameProperties(String file) {

    }

    public void play() throws NullPointerException{
        if(getPopulation() != null && getTreasury() != null) {
            System.out.printf("\nVous avez lancé une partie en mode \"%s\" ", this.toString());
            System.out.printf("en difficulté \"%s\".\n", getGameDifficulty());
            System.out.println("\nLancement du jeu...");
        }
        else {
            System.out.println("Arrêt du jeu...");
            throw new NullPointerException("Game properties are not set.");
        }
    }

    public boolean hasPlayerLost() {
        if(getPopulation().getTotalPopulation() > 0) {
            return getPopulation().getGlobalSatisfactionRate() < 50.0;
        }
        return false;
    }

    public void bribe(String factionName) {
        Faction faction = this.population.getFaction(factionName);
        if(faction.getBribePrice() <= this.treasury.getMoney()) {
            faction.bribe();
            treasury.useMoney(faction.getBribePrice());
        }
        else {
            System.out.println("Vous n'avez pas assez d'argent pour faire un pot de vin aux " + faction.getName() + " !");
        }
    }

    public void addScore(double points) {
        setScore(getScore() + points);
    }
}