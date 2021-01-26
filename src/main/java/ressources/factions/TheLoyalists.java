package ressources.factions;

public class TheLoyalists extends Faction{
    public TheLoyalists(int nbSupporters, int satisfactionRate) {
        super("The loyalists", nbSupporters, satisfactionRate);
    }

    public void bribe() { // TODO Decrease the treasury by bribePrice
        System.out.println(String.format("You cannot bribe %s!", getName()));
    }
}
