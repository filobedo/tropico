package gameplay;

import parser.ParsingKeys;

import java.util.Map;

public class Effect {
    private final Map<String, Map<String, Integer>> effectsByFaction;
    private final Map<String, Integer> effectsByFactor;

    public Effect(Map<String, Map<String, Integer>> effectsByFaction, Map<String, Integer> effectsByFactor) {
        this.effectsByFaction = effectsByFaction;
        this.effectsByFactor = effectsByFactor;
    }

    public void displayFactionEffects() {
        if(this.effectsByFaction.size() != 0) {
            for(Map.Entry<String, Map<String, Integer>> effectsByFactionSet : effectsByFaction.entrySet()) {
                StringBuilder factionEffects = new StringBuilder("\t");
                String factionName = effectsByFactionSet.getKey();
                factionEffects.append(String.format("%s : %n\t\t", factionName));

                Map<String, Integer> effectsOnFaction = effectsByFactionSet.getValue();
                for(Map.Entry<String, Integer> effectByFactionSet : effectsOnFaction.entrySet()) {
                    String factionFactorName = effectByFactionSet.getKey();
                    int factionFactorEffect = effectByFactionSet.getValue();
                    if(factionFactorName.equals(ParsingKeys.satisfactionRate)) {
                        factionEffects.append(getEffectNameAndValue("Satisfaction", factionFactorEffect, "%"));
                    }
                    if(factionFactorName.equals(ParsingKeys.nbSupporters)) {
                        factionEffects.append(getEffectNameAndValue("Partisans", factionFactorEffect, ""));
                    }
                }
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
                if(factorName.equals(ParsingKeys.industryRate)) {
                    factorEffects.append(getEffectNameAndValue("Industrialisation", factorEffect, "%"));
                }
                if(factorName.equals(ParsingKeys.farmRate)) {
                    factorEffects.append(getEffectNameAndValue("Agriculture", factorEffect, "%"));
                }
                if(factorName.equals(ParsingKeys.foodUnits)) {
                    factorEffects.append(getEffectNameAndValue("Nourriture", factorEffect, " unité(s)"));
                }
                if(factorName.equals(ParsingKeys.money)) {
                    factorEffects.append(getEffectNameAndValue("Argent", factorEffect, "$"));
                }
                if(factorName.equals(ParsingKeys.population)) {
                    factorEffects.append(getEffectNameAndValue("Population", factorEffect, ""));
                }
                if(factorName.equals(ParsingKeys.satisfactionRate)) {
                    factorEffects.append(getEffectNameAndValue("Satisfaction globale", factorEffect, "%"));
                }
            }
            System.out.println(deleteLastHyphen(factorEffects.toString()));
        }
    }

    private String getEffectNameAndValue(String effectName, int value, String unit) {
        return String.format("%s : %s%d%s | ", effectName, intSign(value), Math.abs(value), unit);
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
