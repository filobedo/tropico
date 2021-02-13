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
            String factionEffects = "\t";
            for(Map.Entry<String, Map<String, Integer>> effectsByFactionSet : effectsByFaction.entrySet()) {
                String factionName = effectsByFactionSet.getKey();
                Map<String, Integer> effectsOnFaction = effectsByFactionSet.getValue();
                factionEffects += String.format("%s : ", factionName);
                for(Map.Entry<String, Integer> effectByFactionSet : effectsOnFaction.entrySet()) {
                    String factionFactorName = effectByFactionSet.getKey();
                    int factionFactorEffect = effectByFactionSet.getValue();
                    if(factionFactorName.equals("satisfactionRate")) {
                        factionEffects += getEffectNameAndValue("Satisfaction", factionFactorEffect, true);
                        Math.signum(factionFactorEffect);
                    }
                    if(factionFactorName.equals("nbSupporters")) {
                        factionEffects += getEffectNameAndValue("Partisans", factionFactorEffect, true);
                    }
                    factionEffects = deleteLastHyphen(factionEffects);
                }
                factionEffects += "\n\t";
            }
            System.out.println(deleteLastHyphen(factionEffects));
        }
    }

    public void displayFactorEffects() {
        if(this.effectsByFactor.size() != 0) {
            String factorEffects = "\t";
            for(Map.Entry<String, Integer> effectsByFactorSet : effectsByFactor.entrySet()) {
                String factorName = effectsByFactorSet.getKey();
                int factorEffect = effectsByFactorSet.getValue();
                if(factorName.equals("industryRate")) {
                    factorEffects += getEffectNameAndValue("Industrialisation", factorEffect, true);
                }
                if(factorName.equals("farmRate")) {
                    factorEffects += getEffectNameAndValue("Agriculture", factorEffect, true);
                }
                if(factorName.equals("foodUnits")) {
                    factorEffects += getEffectNameAndValue("Nourriture", factorEffect, false);
                }
                if(factorName.equals("treasury")) {
                    factorEffects += getEffectNameAndValue("Trésor", factorEffect, false);
                }
                if(factorName.equals("population")) {
                    factorEffects += getEffectNameAndValue("Population", factorEffect, true);
                }
                if(factorName.equals("satisfactionRate")) {
                    factorEffects += getEffectNameAndValue("Satisfaction globale", factorEffect, true);
                }
            }
            System.out.println(deleteLastHyphen(factorEffects));
        }
    }

    private String getEffectNameAndValue(String effectName, int value, boolean isRate) {
        if(isRate) {
            return String.format("%s : %s%d%% - ", effectName, intSign(value), Math.abs(value));
        }
        return String.format("%s : %s%d - ", effectName, intSign(value), Math.abs(value));
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
