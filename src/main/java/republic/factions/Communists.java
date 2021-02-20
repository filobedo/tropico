package republic.factions;

public class Communists extends Faction {
    public Communists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Communistes";
    }
}
