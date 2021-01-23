import Economy.Tresory;
import org.junit.Test;
import org.junit.Assert;

public class testEconomy {
    @Test
    public void test_update_farm() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateFarmMarker(10 + tresory.getFarmMarker());
        Assert.assertEquals("true - farm : 10 industry : 0", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateFarmMarker(-80 + tresory.getFarmMarker());
        Assert.assertEquals("false - farm : 10 industry : 0", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateFarmMarker(10 + tresory.getFarmMarker());
        Assert.assertEquals("true - farm : 20 industry : 0", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
    }

    @Test
    public void test_update_industry() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateIndustryMarker(10 + tresory.getIndustryMarker());
        Assert.assertEquals("true - farm : 0 industry : 10", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateIndustryMarker(-80 + tresory.getIndustryMarker());
        Assert.assertEquals("false - farm : 0 industry : 10", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateIndustryMarker(10 + tresory.getIndustryMarker());
        Assert.assertEquals("true - farm : 0 industry : 20", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
    }

    @Test
    public void test_update_farm_and_industry() {
        Tresory tresory = new Tresory();

        Boolean res;
        res = tresory.updateFarmMarker(10 + tresory.getFarmMarker());
        Assert.assertEquals("true - farm : 10 industry : 0", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateIndustryMarker(20 + tresory.getIndustryMarker());
        Assert.assertEquals("true - farm : 10 industry : 20", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateIndustryMarker(50 + tresory.getIndustryMarker());
        Assert.assertEquals("true - farm : 10 industry : 70", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateIndustryMarker(50  + tresory.getIndustryMarker());
        Assert.assertEquals("false - farm : 10 industry : 70", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
        res = tresory.updateFarmMarker(50 + tresory.getFarmMarker());
        Assert.assertEquals("false - farm : 10 industry : 70", res + " - farm : " + tresory.getFarmMarker() + " industry : " + tresory.getIndustryMarker());
    }

    @Test
    public void test_get_precent() {
        Tresory tresory = new Tresory();

//        Boolean res;
        tresory.updateIndustryMarker(10);
        Assert.assertEquals(100, tresory.getIncomeIndustry());
        tresory.updateFarmMarker(-80  + tresory.getFarmMarker());
        Assert.assertEquals(0, tresory.getIncomeFarm());
        tresory.updateFarmMarker(10  + tresory.getFarmMarker());
        Assert.assertEquals(400, tresory.getIncomeFarm());
    }

}
