package ressources;
import ressources.factions.Faction;
import ressources.publisher.EventManager;

// Cette classe est une proposition, j'en avais besoin pour mon premier tests du observer pattern
public class Republic {
    public EventManager events;
    private int treasury; // TODO Where to place it ? see with other members
//    private Faction faction;

    public Republic(int treasury) {
        this.events = new EventManager("bribe");
        this.treasury = treasury;
    }

    public int getTreasury() {
        return this.treasury;
    }

    public void bribe(Faction faction) {
        if(faction.getBribePrice() <= this.treasury) {
            faction.bribe();
            useTreasury(faction.getBribePrice());
            events.notify("bribe", faction);
        }
        else {
            System.out.println("You don't have enough money to bribe " + faction.getName() + " faction!");
        }
    }

    public void useTreasury(int amount) {
        this.treasury -= amount;
    }
//    public void openFile(String filePath) {
//        this.file = new File(filePath);
//        events.notify("open", file);
//    }
//
//    public void saveFile() throws Exception {
//        if (this.file != null) {
//            events.notify("save", file);
//        } else {
//            throw new Exception("Please open a file first.");
//        }
//    }
}