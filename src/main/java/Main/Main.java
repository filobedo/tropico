package Main;

import ressources.economy.Tresory;

public class Main {
    public static void main(String[] args) {
        System.out.println("test main");
        Tresory tresory = new Tresory();

        //update des markers
        Boolean res;
        res = tresory.updateFarmMarker(10);
        System.out.println(res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateFarmMarker(-80);
        System.out.println(res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateFarmMarker(10);
        System.out.println(res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());

    }
}
