package republic.factions;

public class Nationalists extends Faction {
    public Nationalists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Nationalistes";
    }
}
