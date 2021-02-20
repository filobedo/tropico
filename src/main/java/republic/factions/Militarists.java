package republic.factions;

public class Militarists extends Faction {
    public Militarists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Militaristes";
    }
}
