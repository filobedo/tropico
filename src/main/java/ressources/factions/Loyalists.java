package ressources.factions;

public class Loyalists extends Faction{
    public Loyalists(int nbSupporters, int satisfactionRate) {
        super(nbSupporters, satisfactionRate);
    }

    @Override
    public void bribe() { // TODO Decrease the treasury by bribePrice
        System.out.printf("You cannot bribe %s!%n", getName());
    }
}
