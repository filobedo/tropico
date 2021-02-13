package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String name;
    private String description;
    private List<Choice> choices = new ArrayList<>();
    private List<Effect> irreversibleEffects = new ArrayList<>();

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void setIrreversibleEffects(List<Effect> irreversibleEffects) {
        this.irreversibleEffects = irreversibleEffects;
    }
}
