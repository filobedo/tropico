import ressources.economy.Treasury;
import org.junit.Test;
import org.junit.Assert;

public class EconomyTest {
    @Test
    public void test_update_farm() {
        Treasury treasury = new Treasury();

        Boolean res;
        res = treasury.updateFarmRate(10 + treasury.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateFarmRate(-80 + treasury.getFarmRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateFarmRate(10 + treasury.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
    }

    @Test
    public void test_update_industry() {
        Treasury treasury = new Treasury();

        Boolean res;
        res = treasury.updateIndustryRate(10 + treasury.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateIndustryRate(-80 + treasury.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 0", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateIndustryRate(10 + treasury.getIndustryRate());
        Assert.assertEquals("farm : 0 industry : 10", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
    }

    @Test
    public void test_update_farm_and_industry() {
        Treasury treasury = new Treasury();

        Boolean res;
        res = treasury.updateFarmRate(10 + treasury.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 0", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateIndustryRate(20 + treasury.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 20", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateIndustryRate(50 + treasury.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 70", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateIndustryRate(50  + treasury.getIndustryRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
        res = treasury.updateFarmRate(50 + treasury.getFarmRate());
        Assert.assertEquals("farm : 10 industry : 90", "farm : " + treasury.getFarmRate() + " industry : " + treasury.getIndustryRate());
    }

    @Test
    public void test_food_stock() {
        Treasury treasury = new Treasury();
        //Year 1
        treasury.updateFarmRate(10 + treasury.getFarmRate());
        //End year
        Assert.assertEquals(400, treasury.generateFarmIncome().getFoodQuantity());

        //Year 2
        treasury.updateFarmRate(80 + treasury.getFarmRate());
        //End year
        Assert.assertEquals(4000, treasury.generateFarmIncome().getFoodQuantity());
    }

    @Test
    public void test_food_conso() {
        Treasury treasury = new Treasury();
        //Year 1
        treasury.updateFarmRate(10 + treasury.getFarmRate());
        //End year
        Assert.assertEquals(360, treasury.generateFarmIncome().eat(10).getFoodQuantity());
        //Year 2
        treasury.updateFarmRate(80 + treasury.getFarmRate());
        //End year
        Assert.assertEquals(3880, treasury.generateFarmIncome().eat(20).getFoodQuantity());
    }

    @Test
    public void test_food_add_bonus() {
        Treasury treasury = new Treasury();
        //Year 1
        treasury.updateFarmRate(10 + treasury.getFarmRate());
        //End year
        Assert.assertEquals(360, treasury.generateFarmIncome().eat(10).getFoodQuantity());
        //Year 2
        treasury.updateFarmRate(80 + treasury.getFarmRate());
        //Add bonus
        treasury.addFood(150);
        //End year
        Assert.assertEquals(4030, treasury.generateFarmIncome().eat(20).getFoodQuantity());
    }

    @Test
    public void test_update_money() {
        Treasury treasury = new Treasury();

        //Industry up by 10%
        treasury.updateIndustryRate(10);
        //End year
        treasury.earnMoney(100);
        treasury.generateIndustryIncome();
        treasury.useMoney(100);
        Assert.assertEquals(100, treasury.getMoney());

    }

}
