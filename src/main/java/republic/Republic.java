package republic;

import game.needs.GameInputOptions;
import game.needs.GameRules;
import game.needs.GamePlayerInput;
import parser.ParsingKeys;
import publisher.EventManager;
import republic.economy.Resources;
import republic.factions.Faction;
import republic.factions.Population;
import gameplay.Effect;
import gameplay.Event;

import java.util.Map;

public class Republic {
    private final Population population;
    private final Resources resources;
    public EventManager events;

    public Republic(Population population, Resources resources) {
        this.population = population;
        this.resources = resources;
    }

    public boolean isSet() {
        return this.population != null && this.resources != null;
    }

    public Population getPopulation() {
        return population;
    }

    public Resources getResources() {
        return resources;
    }

    public boolean isGlobalSatisfactionRateOkay(double gameDifficultyCoefficient) {
        if(isThereAnyPopulation()) {
            return getPopulation().getGlobalSatisfactionRate() >= GameRules.MINIMUM_GLOBAL_SATISFACTION_RATE * gameDifficultyCoefficient;
        }
        return false;
    }

    public boolean isThereAnyPopulation() {
        return getTotalPopulation() > 0;
    }

    public int getTotalPopulation() {
        return this.population.getTotalPopulation();
    }

    public void irreversibleEventEffects(Event currentEvent) {
        Effect eventEffects = currentEvent.getIrreversibleEffects();
        if(eventEffects != null) {
            applyEffects(eventEffects);
        }
    }

    public void applyEffects(Effect effect) {
        factionEffects(effect);
        factorEffects(effect);
    }

    public void factionEffects(Effect effect) {
        Map<String, Map<String, Integer>> effectsByFaction = effect.getEffectsByFaction();
        // Each faction
        for(Map.Entry<String, Map<String, Integer>> factionEffectsSet: effectsByFaction.entrySet()) {
            String factionName = factionEffectsSet.getKey();
            Map<String, Integer> effectsOnFaction = factionEffectsSet.getValue();
            // Each effect on the faction
            for(Map.Entry<String, Integer> factionEffects : effectsOnFaction.entrySet()) {
                String factorName = factionEffects.getKey();
                int factorEffect = factionEffects.getValue();
                if(factorName.equals(ParsingKeys.nbSupporters)) {
                    this.population.updateNbSupportersByFaction(factorEffect, factionName);
                }
                if(factorName.equals(ParsingKeys.satisfactionRate)) {
                    if(factorEffect > 0) {
                         events.notify("satisfaction_increased", getRealSatisfactionVariation(factorEffect, factionName));
                    }
                    if(factorEffect < 0) {
                         events.notify("satisfaction_decreased", getRealSatisfactionVariation(factorEffect, factionName));
                    }
                    this.population.updateSatisfactionRateByFaction(factorEffect, factionName);
                }
            }
        }
    }

    public int getRealSatisfactionVariation(int percentagePoints, String factionName) {
        Faction targetFaction = this.population.getFactionByName().get(factionName);
        int targetFactionSatisfactionRate = targetFaction.getSatisfactionRate();
        if(percentagePoints >= 0) {
            if(percentagePoints > 100 - targetFactionSatisfactionRate) {
                return 100 - targetFactionSatisfactionRate;
            }
        }
        else {
            if(Math.abs(percentagePoints) > targetFactionSatisfactionRate) {
                return -(targetFactionSatisfactionRate);
            }
        }
        return percentagePoints;
    }

    public void factorEffects(Effect effect) {
        Map<String, Integer> effectsByFactor = effect.getEffectsByFactor();
        // Each factor
        for(Map.Entry<String,Integer> factorEffectsSet : effectsByFactor.entrySet()) {
            String factorName = factorEffectsSet.getKey();
            int factorEffect = factorEffectsSet.getValue();
            if(factorName.equals(ParsingKeys.industryRate)) {
                this.resources.updateIndustryRate(factorEffect);
            }
            if(factorName.equals(ParsingKeys.farmRate)) {
                this.resources.updateFarmRate(factorEffect);
            }
            if(factorName.equals(ParsingKeys.foodUnits)) {
                this.resources.addFood(factorEffect);
            }
            if(factorName.equals(ParsingKeys.money)) {
                this.resources.earnMoney(factorEffect);
            }
            if(factorName.equals(ParsingKeys.population)) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
            if(factorName.equals(ParsingKeys.satisfactionRate)) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
        }
    }


    public void playerEndYearChoiceImpacts(int choice, int nbChoicesDone) {
        if(choice == GameInputOptions.YEAR_END_DO_NOTHING_CHOICE) {
            if(nbChoicesDone == 0) {
                System.out.printf("%nVous avez décidé de ne rien faire en cette fin d'année.%n");
                System.out.printf("%nVotre République doit se porter à merveille !%n");
            }
            else if(nbChoicesDone == 1){
                System.out.printf("%nNous espérons que ce choix n'aura pas été vain.%n");
            }
            else {
                System.out.printf("%nNous espérons que ces choix n'auront pas été vains.%n");
            }
        }
        if(choice == GameInputOptions.YEAR_END_BRIBE_CHOICE) {
            this.population.displayAvailableFactions();
            int indexFactionToBribe = GamePlayerInput.chooseFactionToBribe(this.population.getNbFactions());
            String factionToBribe = this.population.getFactionNameByIndex(indexFactionToBribe);
            bribeIfPossible(factionToBribe);
        }
        if(choice == GameInputOptions.YEAR_END_BUY_FOOD_CHOICE) {
            int foodUnitPossibleToBuy = this.resources.buyingFoodUnitsPossible();
            int foodUnitsToBuy = GamePlayerInput.chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
            buyFoodIfPossible(foodUnitsToBuy);
        }
    }

    public boolean bribeIfPossible(String factionName) {
        Faction factionToBribe = this.population.getFaction(factionName);
        if(factionToBribe.canBeBribed()) {
            if(haveEnoughMoney(factionToBribe.getBribePrice())) {
                factionToBribe.bribe();
                this.resources.useMoney(factionToBribe.getBribePrice());
                return true;
            }
            else {
                System.out.printf("%nVous n'avez pas assez d'argent pour verser un pot-de-vin aux %s.%n", factionToBribe.getName());
                return false;
            }
        }
        else {
            System.out.printf("%nIl n'est pas possible de verser un pot de vin à cette faction.%n");
            return false;
        }
    }

    public boolean buyFoodIfPossible(int foodUnits) {
        int foodPrice = this.resources.getFoodPrice(foodUnits);
        if(haveEnoughMoney(foodPrice)) {
            this.resources.buyFood(foodUnits);
            System.out.printf("%nVous avez acheté %d unité(s) de nourriture au prix de %d$.%n", foodUnits, foodPrice);
            return true;
        }
        else {
            System.out.println("Vous n'avez pas assez d'argent pour acheter autant de nourriture !");
            return false;
        }
    }

    public boolean haveEnoughMoney(int price) {
        return this.resources.getMoney() >= price;
    }

    public void feedPopulation() {
        this.resources.feed(getTotalPopulation());
    }

    public int getFoodUnits() {
        return this.resources.getFoodUnits();
    }

    public int getPopulationScore() {
        return this.resources.getFarmRate() * GameRules.END_SCORE_POINTS_PER_CITIZEN;
    }

    public int getIndustryRateScore() {
        return this.resources.getIndustryRate() * GameRules.END_SCORE_POINTS_PER_INDUSTRY_RATE;
    }

    public double getMoneyScore() {
        double money = this.resources.getMoney();
        if(money >= 0) {
            return this.resources.getMoney() * GameRules.END_SCORE_POINTS_PER_DOLLAR_POSITIVE;
        }
        else {
            return Math.abs(this.resources.getMoney()) * GameRules.END_SCORE_POINTS_PER_DOLLAR_NEGATIVE;
        }
    }

    public int getFarmRateScore() {
        return this.resources.getFarmRate() * GameRules.END_SCORE_POINTS_PER_FARM_RATE;
    }

    public double getFoodScore() {
        return this.resources.getFoodUnits() * GameRules.END_SCORE_POINTS_PER_FOOD_UNITS;
    }
}
