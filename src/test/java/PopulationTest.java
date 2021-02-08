import junit.framework.TestCase;
import ressources.factions.Faction;
import ressources.factions.Loyalists;
import ressources.factions.Population;
import org.junit.Assert;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;

public class PopulationTest extends TestCase {
    private Population population;
    private static final String resourcesPath = "src/test/resources/";

    protected void setUp() {
        try {
            population = new Population(resourcesPath + "proper-population-infos.properties");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void test_constructor_can_throw_io_exception() {
        try {
            Population population1 = new Population(resourcesPath + "wrong_file_name.properties");
            Assert.fail("IOException not thrown in " + this.getName());
        } catch (Exception exception){
            assertEquals(FileNotFoundException.class, exception.getClass());
        }
    }

    public void test_constructor_can_throw_configuration_exception_because_missing_faction_infos() {
        try {
            Population population = new Population(resourcesPath + "missing-faction-infos.properties");
            Assert.fail("ConfigurationException not thrown in " + this.getName());
        } catch (Exception exception){
            assertEquals(ConfigurationException.class, exception.getClass());
        }
    }

    public void test_population_factions_values_are_of_type_faction() {
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertTrue(factionsSet.getValue() instanceof Faction);
        }
    }

    public void test_population_factions_keys_are_not_empty() {
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertNotSame("", factionsSet.getKey());
        }
    }

    // TODO Tous les tests bidon qui assert true
    public void test_loyalists_info_dont_exist_in_config_properties() {
        try (FileReader reader = new FileReader(resourcesPath + "missing-loyalists-infos.properties")) {
            Properties properties = new Properties();
            properties.load(reader);
            boolean doesLoyalistsExistsInConfigProperties = population.doesFactionExistInConfigProperties("Loyalists", properties);
            assertFalse(doesLoyalistsExistsInConfigProperties);
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " thrown in " + this.getName());
            ex.printStackTrace();
        }
    }

    public void test_loyalists_info_exist_in_config_properties() {
        try (FileReader reader = new FileReader(resourcesPath + "proper-population-infos.properties")) {
            Properties properties = new Properties();
            properties.load(reader);
            boolean doesLoyalistsExistsInConfigProperties = population.doesFactionExistInConfigProperties("Loyalists", properties);
            assertTrue(doesLoyalistsExistsInConfigProperties);
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " thrown in " + this.getName());
            ex.printStackTrace();
        }
    }

    public void test_create_and_get_faction_successfully() {
        Faction createdFaction = population.createAndGetFaction("Loyalists", 50, 100);
        assertTrue(createdFaction instanceof Faction);
    }

    public void test_create_and_get_faction_can_return_null() {
        Faction createdFaction = population.createAndGetFaction("FakeFaction", 50, 100);
        assertEquals(null, createdFaction);
    }

    public void test_factions_subscribe_to_bribe_event() {
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            if (factionsSet.getValue().getClass() != Loyalists.class) {
                assertNotNull(factionsSet.getValue().events);
            } else {
                assertNull(factionsSet.getValue().events);
            }
        }
    }

    public void test_get_faction() {
        Faction noExist = population.getFaction("Fakefaction");
        assertEquals(null, noExist);
    }

    public void test_get_total_population() {
        int actualTotalPopulation = population.getTotalPopulation();
        int expectedTotalPopulation = 120;
        assertEquals(expectedTotalPopulation, actualTotalPopulation);
    }

    public void test_get_global_satisfaction_rate() {
        double actualGlobalSatisfaction = population.getGlobalSatisfactionRate();
        double expectedGlobalSatisfaction = 50;
        assertEquals(expectedGlobalSatisfaction, actualGlobalSatisfaction);
    }

    public void test_update_satisfaction_rate_by_faction() {
        population.updateSatisfactionRateByFaction(-10, "Loyalists");
        int expectedSatisfactionRate = 40;
        assertEquals(expectedSatisfactionRate, population.getFaction("Loyalists").getSatisfactionRate());
    }

    public void test_update_satisfaction_rate_on_multiple_factions() {
        String[] factionsToUpdate = {"Loyalists", "Communists"};
        population.updateSatisfactionRateOnMultipleFactions(10, factionsToUpdate);
        int expectedSatisfactionRate = 60;

        assertEquals(expectedSatisfactionRate, population.getFaction("Loyalists").getSatisfactionRate());
        assertEquals(expectedSatisfactionRate, population.getFaction("Communists").getSatisfactionRate());
    }

    public void test_update_satisfaction_rate_on_all_factions() {
        population.updateSatisfactionRateOnAllFactions(1000);
        assertFactionsSatisfactionRateEquals(100);

        population.updateSatisfactionRateOnAllFactions(-50);
        assertFactionsSatisfactionRateEquals(50);

        population.updateSatisfactionRateOnAllFactions(-100);
        assertFactionsSatisfactionRateEquals(0);

        population.updateSatisfactionRateOnAllFactions(100);
        assertFactionsSatisfactionRateEquals(0);
    }

    public void assertFactionsSatisfactionRateEquals(int expected) {
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(expected, factionsSet.getValue().getSatisfactionRate());
        }
    }

    public void test_update_nb_supporters_by_faction() {
        population.updateNbSupportersByFaction(100, "Loyalists");
        int expectedNbSupporters = 30;
        assertEquals(expectedNbSupporters, population.getFaction("Loyalists").getNbSupporters());
    }

    public void test_update_nb_supporters_on_multiple_factions() {
        String[] factionsToUpdate = {"Loyalists", "Communists"};
        population.updateNbSupportersOnMultipleFactions(100, factionsToUpdate);
        int expectedNbSupporters = 30;

        assertEquals(expectedNbSupporters, population.getFaction("Loyalists").getNbSupporters());
        assertEquals(expectedNbSupporters, population.getFaction("Communists").getNbSupporters());
    }

    public void test_update_nb_supporters_on_all_factions() {
        population.updateNbSupportersOnAllFactions(100);
        assertFactionsNbSupportersEquals(30);

        population.updateNbSupportersOnAllFactions(100);
        assertFactionsNbSupportersEquals(60);

        population.updateNbSupportersOnAllFactions(-50);
        assertFactionsNbSupportersEquals(30);

        population.updateNbSupportersOnAllFactions(-100);
        assertFactionsNbSupportersEquals(0);
    }

    public void assertFactionsNbSupportersEquals(int expected) {
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(expected, factionsSet.getValue().getNbSupporters());
        }
    }

    public void test_eliminate_supporters_until_enough_food() {
        population.eliminateSupportersUntilEnoughFood(471);
        int totalPopulation = population.getTotalPopulation();
        int expectedTotalPopulation = 117;
        assertEquals(expectedTotalPopulation, totalPopulation);
    }

}
