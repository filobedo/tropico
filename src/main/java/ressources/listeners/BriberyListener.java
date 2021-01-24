package ressources.listeners;

import ressources.factions.Faction;

public class BriberyListener implements EventListener {
    private String faction;

    public BriberyListener(String faction) {
        this.faction = faction;
    }

    @Override
    public void update(String eventType, Faction bribedFaction) {
//        System.out.println("Email to " + email + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());
        System.out.println("Message to " + this.faction + ": Someone has performed " + eventType + " operation with the following faction: " + bribedFaction.getName());
        // TODO : loyalists satisfaction -= bribe amount / 10
    }
}