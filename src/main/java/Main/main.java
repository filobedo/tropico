package Main;

import ressources.economy.Tresory;

public class main {
    public static void main(String[] args) {
        System.out.println("test main");
        Tresory tresory = new Tresory();

        //update des markers
        Boolean res;
        res = tresory.updateFarmRate(10);
        System.out.println(res + " - farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateFarmRate(-80);
        System.out.println(res + " - farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateFarmRate(10);
        System.out.println(res + " - farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());

    }
}
