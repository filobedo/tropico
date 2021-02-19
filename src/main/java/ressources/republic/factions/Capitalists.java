package ressources.republic.factions;

public class Capitalists extends Faction {
    public Capitalists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Capitalistes";
    }
}
