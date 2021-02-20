import ressources.republic.economy.Resources;
import org.junit.Test;
import org.junit.Assert;

public class EconomyTest {
    @Test
    public void test_update_farm() {
        Resources resources = new Resources();

        Boolean res;
        resources.updateFarmRate(10 + resources.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateFarmRate(-80 + resources.getFarmRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateFarmRate(10 + resources.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
    }

    @Test
    public void test_update_industry() {
        Resources resources = new Resources();

        Boolean res;
        resources.updateIndustryRate(10 + resources.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateIndustryRate(-80 + resources.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateIndustryRate(10 + resources.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
    }

    @Test
    public void test_update_farm_and_industry() {
        Resources resources = new Resources();

        Boolean res;
        resources.updateFarmRate(10 + resources.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateIndustryRate(20 + resources.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 20", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateIndustryRate(50 + resources.getIndustryRate());
//        Assert.assertEquals("farm : 10 industry : 70", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateIndustryRate(50  + resources.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
        resources.updateFarmRate(50 + resources.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + resources.getFarmRate() + " industry : " + resources.getIndustryRate());
    }

    @Test
    public void test_food_stock() {
        Resources resources = new Resources();
        //Year 1
        resources.updateFarmRate(10 + resources.getFarmRate());
        //End year
        resources.generateFarmIncome();
        Assert.assertEquals(400, resources.getFoodUnits());

        //Year 2
        resources.updateFarmRate(80 + resources.getFarmRate());
        //End year
        resources.generateFarmIncome();
//        Assert.assertEquals(4000, resources.getFoodUnits());
    }

    @Test
    public void test_food_conso() {
        Resources resources = new Resources();
        //Year 1
        resources.updateFarmRate(10 + resources.getFarmRate());
        //End year
        resources.generateFarmIncome();
        Assert.assertEquals(360, resources.feed(10).getFoodUnits());
        //Year 2
        resources.updateFarmRate(80 + resources.getFarmRate());
        //End year
        resources.generateFarmIncome();
//        Assert.assertEquals(3880, resources.feed(20).getFoodUnits());
    }

    @Test
    public void test_food_add_bonus() {
        Resources resources = new Resources();
        //Year 1
        resources.updateFarmRate(10 + resources.getFarmRate());
        //End year
        resources.generateFarmIncome();
        Assert.assertEquals(360, resources.feed(10).getFoodUnits());
        //Year 2
        resources.updateFarmRate(80 + resources.getFarmRate());
        //Add bonus
        resources.addFood(150);
        //End year
        resources.generateFarmIncome();
//        Assert.assertEquals(4030, resources.feed(20).getFoodUnits());
    }

    @Test
    public void test_update_money() {
        Resources resources = new Resources();

        //Industry up by 10%
        resources.updateIndustryRate(10);
        //End year
        resources.earnMoney(100);
        resources.generateIndustryIncome();
        resources.useMoney(100);
        Assert.assertEquals(100, resources.getMoney(), 0.1);

    }

}
