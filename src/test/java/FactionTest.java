import junit.framework.TestCase;
import ressources.factions.Environmentalists;
import ressources.factions.Faction;
import ressources.factions.Loyalists;
import ressources.listeners.BriberyListener;

public class FactionTest extends TestCase {
    private Faction theLoyalists;
    private Faction theUnsatisfied;

    protected void setUp() {
        theLoyalists = new Loyalists(40, 40);
        theUnsatisfied = new Environmentalists(10, 0);
    }

    public void test_set_satisfaction_rate_to_some_value() {
        theLoyalists.setSatisfactionRate(65);
        assertEquals(65, theLoyalists.getSatisfactionRate());
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
    }

    public void test_bribing_a_faction_should_increase_its_satisfaction() {
        Faction theLoyalists = new Loyalists(200, 80);
        Faction theEcologists = new Ecologists(10, 80);
        theEcologists.events.subscribe("bribe", new BriberyListener(theLoyalists));
        theEcologists.bribe();
        assertEquals(90, theEcologists.getSatisfactionRate());
    }

    public void test_bribing_a_faction_should_decrease_loyalists_satisfaction() {
        Faction theLoyalists = new Loyalists(200, 80);
        Faction theEcologists = new Ecologists(10, 80);
        theEcologists.events.subscribe("bribe", new BriberyListener(theLoyalists));
        theEcologists.bribe();
        assertEquals(65, theLoyalists.getSatisfactionRate());
    }

    public void test_loyalists_cannot_be_bribed() {
        Faction theLoyalists = new Loyalists(200, 80);
        theLoyalists.events.subscribe("bribe", new BriberyListener(theLoyalists));
        theLoyalists.bribe();
        assertEquals(80, theLoyalists.getSatisfactionRate());
    }
}
