package ressources.game;

public class ScenarioMode implements GameMode {
    private String name;

    @Override
    public void play() {
        // TODO
        System.out.println("Une partie en mode scénario est lancée !");
    }

    @Override
    public String toString() {
        return "scénario";
    }
}