package ressources.scenario;

import java.util.HashMap;
import java.util.Map;

public class Effect {
    private Map<String, Map<String, Integer>> effectsByFaction = new HashMap<>();
    private Map<String, Integer> effectsByFactor = new HashMap<>();

    public Effect(Map<String, Map<String, Integer>> effectsByFaction, Map<String, Integer> effectsByFactor) {
        this.effectsByFaction = effectsByFaction;
        this.effectsByFactor = effectsByFactor;
    }
}
