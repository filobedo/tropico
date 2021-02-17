package ressources.republic;

import ressources.game.GameRules;
import ressources.game.PlayerInput;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Faction;
import ressources.republic.factions.Population;
import ressources.scenario.Effect;
import ressources.scenario.Event;

import java.util.Map;

public class Republic {
    private Population population;
    private Resources resources;

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
            for(Map.Entry<String, Integer> factionEffects: effectsOnFaction.entrySet()) {
                String factorName = factionEffects.getKey();
                int factorEffect = factionEffects.getValue();
                if(factorName.equals("nbSupporters")) {
                    this.population.updateNbSupportersByFaction(factorEffect, factionName);
                }
                if(factorName.equals("satisfactionRate")) {
                    this.population.updateSatisfactionRateByFaction(factorEffect, factionName);
                }
            }
        }
    }
    public void factorImpacts(Effect effect) {
        Map<String, Integer> factorImpacts = effect.getEffectsByFactor();
        // Each factor
        for(Map.Entry<String,Integer> factorEffectsSet: factorImpacts.entrySet()) {
            String factorName = factorEffectsSet.getKey();
            int factorEffect = factorEffectsSet.getValue();
            if(factorName.equals("industryRate")) {
                this.resources.updateIndustryRate(factorEffect);
            }
            if(factorName.equals("farmRate")) {
                this.resources.updateFarmRate(factorEffect);
            }
            if(factorName.equals("foodUnits")) {
                this.resources.addFood(factorEffect);
            }
            if(factorName.equals("treasury")) {
                this.resources.earnMoney(factorEffect);
            }
            if(factorName.equals("population")) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
            if(factorName.equals("satisfactionRate")) {
                this.population.updateNbSupportersOnAllFactions(factorEffect);
            }
        }
    }

    public boolean playerYearEndChoiceImpacts(int choice) {
        if(choice == GameRules.YEAR_END_DO_NOTHING_CHOICE) {
            System.out.printf("%nVous avez décidé de ne rien faire pour sauver votre république." +
                    " Elle doit se porter à merveille%n");
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
            this.resources.useMoney(foodPrice);
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
