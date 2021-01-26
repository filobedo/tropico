import junit.framework.TestCase;
import ressources.factions.Communists;
import ressources.factions.Environmentalists;
import ressources.factions.Faction;
import ressources.factions.Loyalists;
import ressources.listeners.BriberyListener;

public class FactionTest extends TestCase {
    private Faction theLoyalists;
    private Faction theUnsatisfied;
    private Faction theEnvironmentalists;

    protected void setUp() {
        theLoyalists = new Loyalists(40, 100);
        theUnsatisfied = new Communists(10, 0);
        theEnvironmentalists = new Environmentalists(20, 50);
    }

    public void test_set_satisfaction_rate_to_some_value() {
        theLoyalists.setSatisfactionRate(65);
        assertEquals(65, theLoyalists.getSatisfactionRate());
        theLoyalists.setSatisfactionRate(1);
        assertEquals(1, theLoyalists.getSatisfactionRate());
        theLoyalists.setSatisfactionRate(100);
        assertEquals(100, theLoyalists.getSatisfactionRate());
        theLoyalists.setSatisfactionRate(95);
        assertEquals(95, theLoyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_negative_value() {
        theLoyalists.setSatisfactionRate(-100);
        assertEquals(0, theLoyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_more_than_100() {
        theLoyalists.setSatisfactionRate(200);
        assertEquals(100, theLoyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_some_value_when_faction_is_unsatisfied() {
        theUnsatisfied.setSatisfactionRate(50);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
        theUnsatisfied.setSatisfactionRate(-50);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
        theUnsatisfied.setSatisfactionRate(120);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
    }

    public void test_bribing_a_faction_should_increase_its_satisfaction() {
        theEnvironmentalists.events.subscribe("bribe", new BriberyListener(theLoyalists));
        theEnvironmentalists.bribe();
        assertEquals(60, theEnvironmentalists.getSatisfactionRate());
    }

    public void test_bribing_a_faction_should_decrease_loyalists_satisfaction() {
        theEnvironmentalists.events.subscribe("bribe", new BriberyListener(theLoyalists));
        theEnvironmentalists.bribe();
        assertEquals(70, theLoyalists.getSatisfactionRate());
    }

    public void test_loyalists_cannot_be_bribed() {
        theLoyalists.bribe();
        assertEquals(100, theLoyalists.getSatisfactionRate());
    }
}
