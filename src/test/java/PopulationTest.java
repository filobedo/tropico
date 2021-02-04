import junit.framework.TestCase;
import ressources.factions.Faction;
import ressources.factions.Population;

import java.util.Map;

public class PopulationTest extends TestCase {
    private Population population;

    protected void setUp() {
        population = new Population("populationtest.properties");
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

    public void test_update_satisfaction_rate_on_all_factions() {
        population.updateSatisfactionRateOnAllFactions(1000);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(100, factionsSet.getValue().getSatisfactionRate());
        }
        population.updateSatisfactionRateOnAllFactions(-50);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(50, factionsSet.getValue().getSatisfactionRate());
        }
        population.updateSatisfactionRateOnAllFactions(-100);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(0, factionsSet.getValue().getSatisfactionRate());
        }
        population.updateSatisfactionRateOnAllFactions(100);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(0, factionsSet.getValue().getSatisfactionRate());
        }
    }

    public void test_update_nb_supporters_on_all_factions() {
        population.updateNbSupportersOnAllFactions(100);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(30, factionsSet.getValue().getNbSupporters());
        }
        population.updateNbSupportersOnAllFactions(100);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(60, factionsSet.getValue().getNbSupporters());
        }
        population.updateNbSupportersOnAllFactions(-50);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(30, factionsSet.getValue().getNbSupporters());
        }
        population.updateNbSupportersOnAllFactions(-100);
        for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            assertEquals(0, factionsSet.getValue().getNbSupporters());
        }
    }

    /* TODO : test factionsSubscribeToBribeEvent(), updateSatisfactionRateByFaction(), updateNbSupportersByFaction()
        createFaction() success, createFaction() throws exception, getTotalPopulation()
    */
}
