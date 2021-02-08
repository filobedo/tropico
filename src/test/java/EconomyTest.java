import ressources.economy.Tresory;
import org.junit.Test;
import org.junit.Assert;

public class EconomyTest {
    @Test
    public void test_update_farm() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateFarmRate(10 + tresory.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateFarmRate(-80 + tresory.getFarmRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateFarmRate(10 + tresory.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
    }

    @Test
    public void test_update_industry() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateIndustryRate(10 + tresory.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateIndustryRate(-80 + tresory.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateIndustryRate(10 + tresory.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
    }

    @Test
    public void test_update_farm_and_industry() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateFarmRate(10 + tresory.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateIndustryRate(20 + tresory.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 20", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateIndustryRate(50 + tresory.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 70", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateIndustryRate(50  + tresory.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
        res = tresory.updateFarmRate(50 + tresory.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + tresory.getFarmRate() + " industry : " + tresory.getIndustryRate());
    }

    @Test
    public void test_food_stock() {
        Tresory tresory = new Tresory();
        //Year 1
        tresory.updateFarmRate(10 + tresory.getFarmRate());
        //End year
        Assert.assertEquals(400, tresory.updateFoodByYear().getFood());

        //Year 2
        tresory.updateFarmRate(80 + tresory.getFarmRate());
        //End year
        Assert.assertEquals(4000, tresory.updateFoodByYear().getFood());
    }

    @Test
    public void test_food_conso() {
        Tresory tresory = new Tresory();
        //Year 1
        tresory.updateFarmRate(10 + tresory.getFarmRate());
        //End year
        Assert.assertEquals(360, tresory.updateFoodByYear().eat(10).getFood());
        //Year 2
        tresory.updateFarmRate(80 + tresory.getFarmRate());
        //End year
        Assert.assertEquals(3880, tresory.updateFoodByYear().eat(20).getFood());
    }

    @Test
    public void test_food_add_bonus() {
        Tresory tresory = new Tresory();
        //Year 1
        tresory.updateFarmRate(10 + tresory.getFarmRate());
        //End year
        Assert.assertEquals(360, tresory.updateFoodByYear().eat(10).getFood());
        //Year 2
        tresory.updateFarmRate(80 + tresory.getFarmRate());
        //Add bonus
        tresory.addBonusFarm(150);
        //End year
        Assert.assertEquals(4030, tresory.updateFoodByYear().eat(20).getFood());
    }

    @Test
    public void test_update_money() {
        Tresory tresory = new Tresory();

        //Industry up by 10%
        tresory.updateIndustryRate(10);
        //End year
        tresory.addMoney(100);
        tresory.updateIndustryByYear();
        tresory.useMoney(100);
        Assert.assertEquals(100, tresory.getMoney());

    }

}
