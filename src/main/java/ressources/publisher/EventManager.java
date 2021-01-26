package ressources.publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ressources.factions.Faction;
import ressources.listeners.EventListener;

public class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventName, EventListener listener) {
        List<EventListener> users = listeners.get(eventName);
        users.add(listener);
    }

    public void unsubscribe(String eventName, EventListener listener) {
        List<EventListener> users = listeners.get(eventName);
        users.remove(listener);
    }

    public void notify(String eventName, Faction faction) {
        List<EventListener> users = listeners.get(eventName);
        for(EventListener listener : users) {
            listener.update(eventName, faction);
        }
    }
}