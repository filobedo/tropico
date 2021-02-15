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

    public void displayFactionEffects() {
        if(this.effectsByFaction.size() != 0) {
            for(Map.Entry<String, Map<String, Integer>> effectsByFactionSet : effectsByFaction.entrySet()) {
                StringBuilder factionEffects = new StringBuilder("\t");
                String factionName = effectsByFactionSet.getKey();
                Map<String, Integer> effectsOnFaction = effectsByFactionSet.getValue();
                factionEffects.append(String.format("%s : ", factionName));
                for(Map.Entry<String, Integer> effectByFactionSet : effectsOnFaction.entrySet()) {
                    String factionFactorName = effectByFactionSet.getKey();
                    int factionFactorEffect = effectByFactionSet.getValue();
                    if(factionFactorName.equals("satisfactionRate")) {
                        factionEffects.append(getEffectNameAndValue("Satisfaction", factionFactorEffect, true));
                    }
                    if(factionFactorName.equals("nbSupporters")) {
                        factionEffects.append(getEffectNameAndValue("Partisans", factionFactorEffect, false));
                    }
                }
                // factionEffects = new StringBuilder((factionEffects.toString()));
                System.out.printf("%s%n", (deleteLastHyphen(factionEffects.toString())));
            }
        }
    }

    public void displayFactorEffects() {
        if(this.effectsByFactor.size() != 0) {
            StringBuilder factorEffects = new StringBuilder("\t");
            for(Map.Entry<String, Integer> effectsByFactorSet : effectsByFactor.entrySet()) {
                String factorName = effectsByFactorSet.getKey();
                int factorEffect = effectsByFactorSet.getValue();
                if(factorName.equals("industryRate")) {
                    factorEffects.append(getEffectNameAndValue("Industrialisation", factorEffect, true));
                }
                if(factorName.equals("farmRate")) {
                    factorEffects.append(getEffectNameAndValue("Agriculture", factorEffect, true));
                }
                if(factorName.equals("foodUnits")) {
                    factorEffects.append(getEffectNameAndValue("Nourriture", factorEffect, false));
                }
                if(factorName.equals("treasury")) {
                    factorEffects.append(getEffectNameAndValue("Argent", factorEffect, false));
                }
                if(factorName.equals("population")) {
                    factorEffects.append(getEffectNameAndValue("Population", factorEffect, true));
                }
                if(factorName.equals("satisfactionRate")) {
                    factorEffects.append(getEffectNameAndValue("Satisfaction globale", factorEffect, true));
                }
            }
            System.out.println(deleteLastHyphen(factorEffects.toString()));
        }
    }

    private String getEffectNameAndValue(String effectName, int value, boolean isRate) {
        if(effectName.equals("Argent")) {
            return String.format("%s : %s%d$ | ", effectName, intSign(value), Math.abs(value));
        }
        if(isRate) {
            return String.format("%s : %s%d%% | ", effectName, intSign(value), Math.abs(value));
        }
        return String.format("%s : %s%d | ", effectName, intSign(value), Math.abs(value));
    }

    private String intSign(int number) {
        if(number >= 0) {
            return "+";
        }
        return "-";
    }

    private String deleteLastHyphen(String toShorten) {
        return toShorten.substring(0, toShorten.length() - 2);
    }

    public Map<String, Map<String, Integer>> getEffectsByFaction() {
        return effectsByFaction;
    }

    public Map<String, Integer> getEffectsByFactor() {
        return effectsByFactor;
    }
}
