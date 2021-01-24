package ressources;
import ressources.factions.Faction;
import ressources.publisher.EventManager;

public class Republic {
    public EventManager events;
    private Faction faction;

    public Republic() {
        this.events = new EventManager("bribe", "save");
    }

    public void bribe(Faction faction) {
        faction.bribe();
        events.notify("bribe", faction);
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