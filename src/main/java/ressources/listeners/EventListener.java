package ressources.listeners;

import ressources.republic.factions.Faction;

public interface EventListener {
    void update(String eventName, Faction bribedFaction);
}