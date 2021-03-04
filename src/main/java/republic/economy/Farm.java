package republic.economy;

import game.needs.GameRules;

public class Farm implements Development {
    int rate = 0;
    int foodUnits = 0;

    Farm() {}
    Farm (int foodUnits, int rate) {
        this.rate = rate;
        this.foodUnits = foodUnits;
    }

    protected int getFoodUnits() {
        return foodUnits;
    }

    protected void setFoodUnits(int foodUnits) {
        if(foodUnits <= 0) {
            this.foodUnits = 0;
        }
        else {
            this.foodUnits = foodUnits;
        }
    }

    public int getRate() {
        return rate;
    }

    protected void updateRate(int rate) {
        if(rate + this.rate <= 0) {
            this.rate = 0;
        }
        else if(rate + this.rate >= 100) {
            this.rate = 100;
        }
        else {
            this.rate = this.rate + rate;
        }
    }

    public String toString() {
        return String.format("\tAgriculture : %d%%%n", this.rate) +
                String.format("\tNourriture : %d%n", this.foodUnits) +
                String.format("\tVous pouvez nourrir : %d citoyens%n", this.foodUnits / GameRules.NEEDED_FOOD_PER_CITIZEN);
    }
}
