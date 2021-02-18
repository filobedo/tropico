package ressources.republic.economy;

import ressources.game.GameRules;

public class Resources {
    Industry industry;
    Farm farm;

    public Resources() {
        this.industry = new Industry();
        this.farm = new Farm();
    }

    public Resources(int foodUnits, int money, int farmRate, int industryRate) throws IllegalArgumentException {
        if(!isFarmAndIndustrySumRateCorrect(farmRate, industryRate)) {
            throw new IllegalArgumentException("Industry and/or farm rate have an incorrect sum value.");
        }
        this.industry = new Industry(money, industryRate);
        this.farm = new Farm(foodUnits, farmRate);
    }

    private Boolean isFarmAndIndustrySumRateCorrect(int newMarker, int otherOneMarker) {
        return newMarker + otherOneMarker <= 100 && newMarker + otherOneMarker >= 0;
    }

    public Industry getIndustry() {
        return this.industry;
    }

    public Farm getFarm() {
        return this.farm;
    }

    public void updateFarmRate(int percentagePoint) {
        int finalPercentagePointToAdd = getBalancedRate(getFarmRate(), percentagePoint, getIndustryRate());
        this.farm.updateRate(finalPercentagePointToAdd);
    }

    public void updateIndustryRate(int percentagePoint) {
        int finalPercentagePointToAdd = getBalancedRate(getIndustryRate(), percentagePoint, getFarmRate());
        this.industry.updateRate(finalPercentagePointToAdd);
    }

    private int getBalancedRate(int rateToUpdate, int percentagePointToAdd, int oppositeRate) {
        int totalNewRate = rateToUpdate + percentagePointToAdd + oppositeRate;
        if(totalNewRate > 100) {
            return 100 - oppositeRate - rateToUpdate;
        }
        if(totalNewRate < 0) {
            return -(rateToUpdate);
        }
        return percentagePointToAdd;
    }

    public int getFarmRate() {
        return this.farm.getRate();
    }

    public int getIndustryRate() {
        return this.industry.getRate();
    }

    public void generateFarmIncome() {
        addFood(getFoodIncomeFromFarm());
    }

    public void generateIndustryIncome() {
        earnMoney(getMoneyIncomeFromIndustry());
    }

    public int getMoneyIncomeFromIndustry() {
        return this.getIndustryRate() * GameRules.GENERATED_MONEY_BY_INDUSTRY_RATE;
    }

    public int getFoodIncomeFromFarm() {
        return this.getFarmRate() * GameRules.GENERATED_FOOD_BY_FARM_RATE;
    }

    public int getFoodUnits() {
        return this.farm.getFoodUnits();
    }

    public int getMoney() {
        return this.industry.getMoney();
    }

    public void addFood(int foodUnits) {
        this.farm.setFoodUnits(foodUnits + getFoodUnits());
    }

    public Resources feed(int population) {
        int res = getFoodUnits() - (population * GameRules.NEEDED_FOOD_PER_CITIZEN);
        this.farm.setFoodUnits(res);
        return this; // Quoi retourner en fonction de la consommation de la bouffe ?
    }

    public void useMoney(int amount) {
        if(amount > getMoney()) {
            this.industry.setMoney(0); //changer si on veut limiter à 0
        }
        else {
            this.industry.setMoney(getMoney() - amount); //changer si on veut limiter à 0
        }
    }

    public void earnMoney(int amount) {
        this.industry.setMoney(getMoney() + amount);
    }

    public int buyingFoodUnitsPossible() {
        return getMoney() / GameRules.FOOD_PRICE;
    }

    public int getFoodPrice(int foodUnits) {
        return foodUnits * GameRules.FOOD_PRICE;
    }

    public void buyFood(int foodUnits) {
        useMoney(getFoodPrice(foodUnits));
        addFood(foodUnits);
    }

    public void displaySummary() {
        System.out.printf("%nRessources : %n%s%s", this.industry.toString(), this.farm.toString());
    }
}
