package ressources.listeners;

import ressources.factions.Faction;

public class BriberyListener implements EventListener {
    private final Faction faction;

    public BriberyListener(Faction faction) {
        this.faction = faction;
    }

    @Override
    public void update(String eventName, Faction bribedFaction) {
        System.out.println("\"Message aux " + this.faction.getName() + ": El Presidente a versé un pot-de-vin aux : " + bribedFaction.getName() + "\"");
        this.faction.updateSatisfactionRate(-(bribedFaction.getBribePrice() / 10)); // TODO 10 constante ?
    }
}