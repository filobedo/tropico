package ressources.economy;

public class Farm implements Development {
    int rate = 0;
    int foodQuantity = 0;

    Farm() {}
    Farm (int foodQuantity, int rate) {
        this.rate = rate;
        this.foodQuantity = foodQuantity;
    }

    protected int getFoodQuantity() {
        return foodQuantity;
    }

    protected void setFoodQuantity(int ressource) {
        this.foodQuantity = ressource;
    }

    public int getRate() {
        return rate;
    }

    protected void updateRate(int rate) {
        this.rate = this.rate + rate;
    }
}
