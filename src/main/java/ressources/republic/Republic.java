package ressources.republic;

import ressources.game.GameRules;
import ressources.game.PlayerInput;
import ressources.parser.ParsingKeys;
import ressources.publisher.EventManager;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Faction;
import ressources.republic.factions.Population;
import ressources.scenario.Effect;
import ressources.scenario.Event;

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

    public void irreversibleEventImpacts(Event currentEvent) {
        Effect eventEffects = currentEvent.getIrreversibleEffects();
        if(eventEffects != null) {
            impacts(eventEffects);
        }
    }

    public void impacts(Effect effect) {
        factionImpacts(effect);
        factorImpacts(effect);
    }

    public void factionImpacts(Effect effect) {
        Map<String, Map<String, Integer>> factionImpacts = effect.getEffectsByFaction();
        // Each faction
        for(Map.Entry<String, Map<String, Integer>> factionEffectsSet: factionImpacts.entrySet()) {
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

    public void factorImpacts(Effect effect) {
        Map<String, Integer> factorImpacts = effect.getEffectsByFactor();
        // Each factor
        for(Map.Entry<String,Integer> factorEffectsSet : factorImpacts.entrySet()) {
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

    public boolean playerYearEndChoiceImpacts(int choice) {
        if(choice == GameRules.YEAR_END_DO_NOTHING_CHOICE) {
            System.out.printf("%nVous avez décidé de ne rien faire pour sauver votre république." +
                    " Elle doit se porter à merveille%n");
            return true;
        }
        if(choice == GameRules.YEAR_END_BRIBE_CHOICE) {
            this.population.displayAvailableFactions();
            int indexFactionToBribe = PlayerInput.chooseFactionToBribe(this.population.getNbFactions());
            String factionToBribe = this.population.getFactionNameByIndex(indexFactionToBribe);
            return bribeIfPossible(factionToBribe);
        }
        if(choice == GameRules.YEAR_END_BUY_FOOD_CHOICE) {
            int foodUnitPossibleToBuy = this.resources.buyingFoodUnitsPossible();
            int foodUnitsToBuy = PlayerInput.chooseFoodUnitsToBuy(foodUnitPossibleToBuy);
            return buyFoodIfPossible(foodUnitsToBuy);
        }
        return false;
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

    public boolean buyFoodIfPossible(int foodUnit) {
        int foodPrice = this.resources.getFoodPrice(foodUnit);
        if(haveEnoughMoney(foodPrice)) {
            this.resources.buyFood(foodUnit);
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

}
