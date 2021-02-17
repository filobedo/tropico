package ressources.economy;

import ressources.game.GameRules;

public class Treasury {
    Industry industry;
    Farm farm;

    public Treasury() {
        this.industry = new Industry();
        this.farm = new Farm();
    }

    public Treasury(int foodQuantity, int money, int farmRate, int industryRate) throws IllegalArgumentException {
        if(!isFarmAndIndustrySumRateCorrect(farmRate, industryRate)) {
            throw new IllegalArgumentException("Industry and/or farm rate have an incorrect sum value.");
        }
        this.industry = new Industry(money, industryRate);
        this.farm = new Farm(foodQuantity, farmRate);
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

    public Boolean updateFarmRate(int newFarmRate) {
        int updateValue = checkRate(newFarmRate, this.industry.getRate());
        this.farm.updateRate(updateValue);
        return true;
    }

    public Boolean updateIndustryRate(int newIndustryRate) {
        int updateValue = checkRate(newIndustryRate, this.farm.getRate());
        this.industry.updateRate(updateValue);
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

    public int getFarmRate() {
        return this.farm.getRate();
    }

    public int getIndustryRate() {
        return this.industry.getRate();
    }

    public Treasury generateFarmIncome() {
        addFood(getFoodIncomeFromFarm());
        return this;
    }

    public void generateIndustryIncome() {
        earnMoney(getMoneyIncomeFromIndustry());
    }

    //get Resources this year
    private int getMoneyIncomeFromIndustry() {
        return this.getIndustryRate() * GameRules.GENERATED_MONEY_BY_INDUSTRY_RATE;
    }

    //get Resources this year
    private int getFoodIncomeFromFarm() {
        return this.getFarmRate() * GameRules.GENERATED_FOOD_BY_FARM_RATE;
    }

    public int getFoodQuantity() {
        return this.farm.getFoodQuantity();
    }

    public int getMoney() {
        return this.industry.getMoney();
    }

    public void addFood(int foodUnits) {
        this.farm.setFoodQuantity(foodUnits + getFoodQuantity());
    }

    public Treasury eat(int population) {
        int res = getFoodQuantity() - (population * GameRules.NEEDED_FOOD_PER_CITIZEN);
        this.farm.setFoodQuantity(res);
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

    public int getFoodPrice(int foodQuantity) {
        return foodQuantity * GameRules.FOOD_PRICE;
    }

    public void buyFood(int foodQuantity) {
        useMoney(getFoodPrice(foodQuantity));
        addFood(foodQuantity);
    }

    public void displaySummary() {
        String treasurySummary = String.format("Resources :%n") +
                // Argent
                String.format("\tArgent : %d$%n", getMoney()) +
                // IndustryRate
                String.format("\tIndustrie : %d%%%n", getIndustryRate()) +
                // FarmRate
                String.format("\tAgriculture : %d%%%n", getFarmRate()) +
                // Food
                String.format("\tNourriture : %d%n", getFoodQuantity());
        System.out.println(treasurySummary);
    }
}
