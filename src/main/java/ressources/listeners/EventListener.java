package ressources.listeners;

import ressources.factions.Faction;

public interface EventListener {
    void update(String eventType, Faction faction);
}