package ressources.republic.economy;

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

    protected void setFoodUnits(int ressource) {
        this.foodUnits = ressource;
    }

    public int getRate() {
        return rate;
    }

    protected void updateRate(int rate) {
        this.rate = this.rate + rate;
    }
}
