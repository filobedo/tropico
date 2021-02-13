package ressources.factions;

public class Loyalists extends Faction{
    public Loyalists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    @Override
    public void bribe() {
        System.out.printf("Vous ne pouvez pas verser un pot de vin aux %s!%n", getName());
    }
}
