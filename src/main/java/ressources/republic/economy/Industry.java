package ressources.republic.economy;

public class Industry implements Development {
    int rate = 0;
    int money = 0; // money

    Industry() {}
    Industry (int money, int rate) {
        this.rate = rate;
        this.money = money;
    }

    protected int getMoney() {
        return money;
    }

    protected void setMoney(int money) {
        this.money = money;
    }

    public int getRate() {
        return this.rate;
    }

    public void updateRate(int rate) {
        this.rate = this.rate + rate;
    }
}
