package listeners;

import republic.factions.Faction;
import game.GameRules;

public class BriberyListener implements EventListener {
    private final Faction faction;

    public BriberyListener(Faction faction) {
        this.faction = faction;
    }

    /**
     * Happens when a faction is bribed
     * @param eventName name of the event, 'bribe' here
     * @param faction the faction that is being bribed
     */
    @Override
    public void update(String eventName, Object faction) {
        Faction bribedFaction = (Faction)faction;
        System.out.println("\"Message aux " + this.faction.getName() + ": El Presidente a versé un pot-de-vin aux " + bribedFaction.getName() + "\"");
        this.faction.updateSatisfactionRate(bribedFaction.getBribePrice() / GameRules.BRIBE_FACTION_DECREASE_LOYALISTS_SATISFACTION);
    }
}