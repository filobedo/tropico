package republic.factions;

public class Ecologists extends Faction {
    public Ecologists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Écologistes";
    }
}
