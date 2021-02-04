package ressources.listeners;

import ressources.factions.Faction;

public class BriberyListener implements EventListener {
    private final Faction faction;

    public BriberyListener(Faction faction) {
        this.faction = faction;
    }

    @Override
    public void update(String eventName, Faction bribedFaction) {
        System.out.println("Message to " + this.faction.getName() + ": El Presidente has performed " + eventName + " operation with the following faction: " + bribedFaction.getName());
        this.faction.updateSatisfactionRate(-(bribedFaction.getBribePrice() / 10)); // TODO 10 constante ?
    }
}