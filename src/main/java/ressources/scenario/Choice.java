package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Choice {
    private String name;
    private String description;
    private Effect effects;

    public Choice(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setEffects(Effect effects) {
        this.effects = effects;
    }
}
