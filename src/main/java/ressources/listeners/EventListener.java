package ressources.listeners;

import ressources.factions.Faction;

public interface EventListener {
    void update(String eventName, Faction bribedFaction);
}