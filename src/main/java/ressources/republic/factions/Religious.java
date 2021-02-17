package ressources.republic.factions;

public class Religious extends Faction {
    public Religious(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Religieux";
    }
}
