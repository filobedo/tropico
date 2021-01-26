package Economy;

public class Tresory {
    Industry myIndustry;
    Farm myFarm;

    public Tresory() {
        this.myIndustry = new Industry();
        this.myFarm = new Farm();
    }

    public Boolean updateFarmMarker(int newFarmMarker) {
        if(!checkMarker(newFarmMarker, this.myIndustry.getMarker())) {
            return false;
        }
        this.myFarm.setMarker(newFarmMarker);
        return true;
    }

    public Boolean updateIndustryMarker(int newIndustryMarker) {
        if(!checkMarker(newIndustryMarker, this.myFarm.getMarker())) {
            return false;
        }
        this.myIndustry.setMarker(newIndustryMarker);
        return true;
    }

    private Boolean checkMarker(int newMarker, int otherOneMarker) {
        if (newMarker + otherOneMarker > 100) {
            return false;
        }
        if (newMarker + otherOneMarker < 0) {
            return false;
        }
        return true;
    }

    public int getFarmMarker() {
        return this.myFarm.getMarker();
    }

    public int getIndustryMarker() {
        return this.myIndustry.getMarker();
    }

    //get ressources this year
    private int getIncomeIndustry() {
        int res = 0;
        res = this.getIndustryMarker() * 10;
        return res;
    }

    //get ressources this year
    private int getIncomeFarm() {
        int res = 0;
        res = this.getFarmMarker() * 40;
        return res;
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
        int res = this.getFood() - (people * 4);
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
}
