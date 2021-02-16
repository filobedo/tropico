package ressources.factions;

public class Liberals extends Faction{
    public Liberals(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    public String getName() {
        return "Libéraux";
    }
}
