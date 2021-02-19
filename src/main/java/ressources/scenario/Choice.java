package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Choice {
    private String name;
    private String description;
    private Effect effects;
    private List<Event> relatedEvents;

    public Choice(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setEffects(Effect effects) {
        this.effects = effects;
    }

    public void setRelatedEvent(List<Event> relatedEvents) {
        this.relatedEvents = relatedEvents;
    }

    public Effect getEffects() {
        return this.effects;
    }

    public void display() {
        System.out.printf("%s : %s%n", this.name, this.description);
        effects.displayFactionEffects();
        effects.displayFactorEffects();
    }

    public boolean hasRelatedEvents() {
        return this.relatedEvents != null;
    }

    public List<Event> getRelatedEvents() {
        return relatedEvents;
    }
}
