package ressources.economy;

import org.json.JSONObject;
import ressources.factions.Faction;

import java.util.Map;

public class Tresory {
    Industry myIndustry;
    Farm myFarm;

    public Tresory() {
        this.myIndustry = new Industry();
        this.myFarm = new Farm();
    }

    public Tresory(int startFarmRessource, int startIndustryRessource, int startRateFarm, int startRateIndustry) throws IllegalArgumentException {
        if(!checkRateStart(startRateFarm, startRateIndustry)) {
            throw new IllegalArgumentException("error rate");
        }

        this.myIndustry = new Industry(startIndustryRessource, startRateIndustry);
        this.myFarm = new Farm(startFarmRessource, startRateFarm);
    }

    public Industry getMyIndustry() {
        return myIndustry;
    }

    public Farm getMyFarm() {
        return myFarm;
    }

    public Boolean updateFarmRate(int newFarmRate) {
        int updateValue = checkRate(newFarmRate, this.myIndustry.getRate());
        if(updateValue != newFarmRate) {
            this.myFarm.setRate(updateValue);
        } else {
            this.myFarm.setRate(updateValue);
        }
        return true;
    }

    // TODO Valeur negative acceptée
    // TODO les ifs peuvent être simplifiés regarder l'onglet "Problems"
    //  de Intellij en bas à gauche de la fenêtre
    public Boolean updateIndustryRate(int newIndustryRate) {
        int updateValue = checkRate(newIndustryRate, this.myFarm.getRate());
        if(updateValue != newIndustryRate) {
            this.myIndustry.setRate(updateValue);
        } else {
            this.myIndustry.setRate(updateValue);
        }
        return true;
    }

    private int checkRate(int newMarker, int otherOneMarker) {
        if (newMarker + otherOneMarker > 100) {
            return 100 - otherOneMarker;
        }
        if (newMarker + otherOneMarker < 0) {
            return 0;
        }
        return newMarker;
    }

    private Boolean checkRateStart(int newMarker, int otherOneMarker) {
        if (newMarker + otherOneMarker > 100 || newMarker + otherOneMarker < 0) {
            return false;
        }
        return true;
    }

    public int getFarmRate() {
        return this.myFarm.getRate();
    }

    public int getIndustryRate() {
        return this.myIndustry.getRate();
    }

    //get ressources this year
    private int getIncomeIndustry() {
        return this.getIndustryRate() * 10;
    }

    //get ressources this year
    private int getIncomeFarm() {
        return this.getFarmRate() * 40;
    }

    public void generateIndustryIncome() {
        addMoney(getIncomeIndustry());
    }

    public void generateFarmIncome() {
        addBonusFarm(getIncomeFarm());
    }

    public int getFood() {
        return this.myFarm.getRessource();
    }

    public int getMoney() {
        return this.myIndustry.getRessource();
    }

    public Tresory updateFoodByYear() {
        this.myFarm.setRessource(getIncomeFarm() + getFood());
        return this;
    }

    public Tresory updateIndustryByYear() {
        this.myIndustry.setRessource(getIncomeIndustry() + getMoney());
        return this;
    }

    public Tresory addBonusFarm(int bonus) {
        this.myFarm.setRessource(bonus + getFood());
        return this;
    }


    public Tresory eat(int people) {
        int res = getFood() - (people * 4);
        this.myFarm.setRessource(res);
        return this; // Quoi retourner en fonction de la consommation de la bouffe ?
    }

    public Tresory useMoney(int amount) {
        this.myIndustry.setRessource(this.getMoney() - amount); //changer si on veut limiter à 0
        return this;
    }

    public Tresory addMoney(int amount) {
        this.myIndustry.setRessource(this.getMoney() + amount);
        return this;
    }

    public int simulatePriceBuyingFood(int quantity) {
        return quantity * 8;
    }

    // Au main de vérifier si on ne passe pas en dessous de 0 en bouffe
    public Tresory buyFood(int quantity) {
        int res = quantity * 8;
        useMoney(res);
        addBonusFarm(res);
        return this;
    }

    public void displaySummary() {
        StringBuilder treasurySummary = new StringBuilder();
        treasurySummary.append("Ressources :%n");
        // Argent
        treasurySummary.append(String.format("Argent : %d$%n", getMoney()));
        // IndustryRate
        treasurySummary.append(String.format("Industrie : %d%%%n",getIndustryRate()));
        // FarmRate
        treasurySummary.append(String.format("Agriculture : %d%%%n", getFarmRate()));
        // Food
        treasurySummary.append(String.format("Nourriture : %d%n", getFood()));

        System.out.println(treasurySummary);
    }
}
