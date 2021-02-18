package ressources.listeners;

import ressources.republic.factions.Faction;
import ressources.game.GameRules;

public class BriberyListener implements EventListener {
    private final Faction faction;

    public BriberyListener(Faction faction) {
        this.faction = faction;
    }

    @Override
    public void update(String eventName, Object faction) {
        Faction bribedFaction = (Faction)faction;
        System.out.println("\"Message aux " + this.faction.getName() + ": El Presidente a versé un pot-de-vin aux " + bribedFaction.getName() + "\"");
        this.faction.updateSatisfactionRate(bribedFaction.getBribePrice() / GameRules.BRIBING_DECREASE_LOYALISTS_SATISFACTION);
    }
}