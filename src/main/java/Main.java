import ressources.Republic;
import ressources.factions.Faction;
import ressources.listeners.BriberyListener;

public class Main {
    public static void main(String[] args) {
        System.out.println("Test main()");
        Republic elRepublico = new Republic();
        Faction factionToBribe = new Faction("The ecologists", 214, 80);
        elRepublico.events.subscribe("bribe", new BriberyListener("The loyalists"));

        elRepublico.bribe(factionToBribe);
    }
}
