package ressources.republic.economy;

public class Industry implements Development {
    int rate = 0;
    double money = 0; // money

    Industry() {}
    Industry (double money, int rate) {
        this.rate = rate;
        this.money = money;
    }

    protected double getMoney() {
        return money;
    }

    protected void setMoney(double money) {
        this.money = money;
    }

    public int getRate() {
        return this.rate;
    }

    public void updateRate(int rate) {
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
        return String.format("\tIndustrie : %d%%%n", this.rate) +
                String.format("\tArgent : %.2f$%n", this.money);
    }
}
