package Economy;

public class Tresory {
    Industry myIndustry;
    Farm myFarm;

    public Tresory() {
        this.myIndustry = new Industry();
        this.myFarm = new Farm();


        System.out.println();
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

    public int getIncomeIndustry() {
        int res = 0;
        res = this.getIndustryMarker() * 10;
        return res;
    }

    public int getIncomeFarm() {
        int res = 0;
        res = this.getFarmMarker() * 40;
        return res;
    }

}
