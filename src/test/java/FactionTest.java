import junit.framework.TestCase;
import ressources.Faction;

public class FactionTest extends TestCase {
    private Faction loyalists;
    private Faction unsatisfiedLoyalists;

    protected void setUp() {
        loyalists = new Faction("The loyalists", 40, 40);
        unsatisfiedLoyalists = new Faction("The forever unsatisfied", 10, 0);
    }

    public void test_set_satisfaction_rate_to_some_value() {
        loyalists.setSatisfactionRate(65);
        assertEquals(65, loyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_negative_value() {
        loyalists.setSatisfactionRate(-100);
        assertEquals(0, loyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_more_than_100() {
        loyalists.setSatisfactionRate(200);
        assertEquals(100, loyalists.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_some_value_when_faction_is_unsatisfied() {
        unsatisfiedLoyalists.setSatisfactionRate(50);
        assertEquals(0, unsatisfiedLoyalists.getSatisfactionRate());
    }

}
