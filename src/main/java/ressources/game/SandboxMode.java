package ressources.game;

public class SandboxMode implements GameMode {

    @Override
    public void play() {
        // TODO
        System.out.println("Une partie en mode bac à sable est lancée !");
    }

    @Override
    public String toString() {
        return "bac à sable";
    }
}