package ressources.republic.factions;

public class Environmentalists extends Faction {
    public Environmentalists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Écologistes";
    }
}
