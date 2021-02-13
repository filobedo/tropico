package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Choice {
    private String name;
    private String description;
    private Effect effects;
    private Event relatedEvent;

    public Choice(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setEffects(Effect effects) {
        this.effects = effects;
    }

    public void setRelatedEvent(Event relatedEvent) {
        this.relatedEvent = relatedEvent;
    }

    public void display() {
        System.out.printf("%s : %s%n", this.name, this.description);
        effects.displayFactionEffects();
        effects.displayFactorEffects();
    }

    public Effect getEffects() {
        return this.effects;
    }
}
