import junit.framework.TestCase;
import org.junit.Assert;
import ressources.factions.*;
import ressources.listeners.BriberyListener;

public class FactionTest extends TestCase {
    private Faction liberals;

    protected void setUp() {
        liberals = new Liberals(40, 100);
    }

    public void test_faction_constructor_can_throw_exception_when_negative_nb_supporters() {
        try {
            Faction faction = new Communists(-1, 100);
            Assert.fail("IllegalArgumentException not thrown in " + this.getName());
        } catch (Exception exception){
            assertEquals(IllegalArgumentException.class, exception.getClass());
        }
    }

    public void test_constructor_can_throw_exception_when_satisfaction_rate_less_than_0() {
        try {
            Faction faction = new Communists(100, -1);
            Assert.fail("IllegalArgumentException not thrown in " + this.getName());
        } catch (Exception exception){
            assertEquals(IllegalArgumentException.class, exception.getClass());
        }
    }

    public void test_constructor_can_throw_exception_when_satisfaction_rate_greater_than_100() {
        try {
            Faction faction = new Communists(100, 101);
            Assert.fail("IllegalArgumentException not thrown in " + this.getName());
        } catch (Exception exception){
            assertEquals(IllegalArgumentException.class, exception.getClass());
        }
    }

    public void test_set_nb_supporters_to_possible_values() {
        liberals.setNbSupporters(-1000);
        assertEquals(0, liberals.getNbSupporters());
        liberals.setNbSupporters(1000);
        assertEquals(1000, liberals.getNbSupporters());
        liberals.setNbSupporters(25);
        assertEquals(25, liberals.getNbSupporters());
    }

    public void test_set_satisfaction_rate_to_positive_values() {
        liberals.setSatisfactionRate(65);
        assertEquals(65, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(1);
        assertEquals(1, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(100);
        assertEquals(100, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(95);
        assertEquals(95, liberals.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_negative_values() {
        liberals.setSatisfactionRate(-100);
        assertEquals(0, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(-1000);
        assertEquals(0, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(-1);
        assertEquals(0, liberals.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_more_than_100() {
        liberals.setSatisfactionRate(200);
        assertEquals(100, liberals.getSatisfactionRate());
        liberals.setSatisfactionRate(101);
        assertEquals(100, liberals.getSatisfactionRate());
    }

    public void test_set_satisfaction_rate_to_some_value_when_faction_is_unsatisfied() {
        Faction theUnsatisfied = new Communists(10, 0);
        theUnsatisfied.setSatisfactionRate(50);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
        theUnsatisfied.setSatisfactionRate(-50);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
        theUnsatisfied.setSatisfactionRate(120);
        assertEquals(0, theUnsatisfied.getSatisfactionRate());
    }

    public void test_update_nb_supporters() {
        liberals.updateNbSupportersBy(100);
        assertEquals(80, liberals.getNbSupporters());
        liberals.updateNbSupportersBy(-50);
        assertEquals(40, liberals.getNbSupporters());
        liberals.updateNbSupportersBy(10);
        assertEquals(44, liberals.getNbSupporters());
        liberals.updateNbSupportersBy(-98); // 44 * 0.02 = 0.82 -> A supporter is 1 person and not 0.82 person
        assertEquals(0, liberals.getNbSupporters());
        liberals.updateNbSupportersBy(1000); // 44 * 0.02 = 0.82 -> A supporter is 1 person and not 0.82 person
        assertEquals(0, liberals.getNbSupporters());
    }

    public void test_update_satisfaction_rate() {
        liberals.updateSatisfactionRate(100);
        assertEquals(100, liberals.getSatisfactionRate());
        liberals.updateSatisfactionRate(-50);
        assertEquals(50, liberals.getSatisfactionRate());
        liberals.updateSatisfactionRate(1);
        assertEquals(51, liberals.getSatisfactionRate());
        liberals.updateSatisfactionRate(-100);
        assertEquals(0, liberals.getSatisfactionRate());
        liberals.updateSatisfactionRate(100);
        assertEquals(0, liberals.getSatisfactionRate());
    }

    public void test_get_bribe_price() {
        int expectedPrice = 600;
        int bribePrice = liberals.getBribePrice();
        assertEquals(expectedPrice, bribePrice);
    }

    public void test_cannot_get_bribe_price_from_loyalists() {
        Faction loyalists = new Loyalists(100, 100);
        int bribePrice = loyalists.getBribePrice();
        assertEquals(0, bribePrice);
    }

    public void test_bribing_a_faction_should_increase_its_satisfaction() {
        int expectedSatisfactionRate = 60;
        Faction theEnvironmentalists = new Environmentalists(20, 50);
        theEnvironmentalists.events.subscribe("bribe", new BriberyListener(liberals));
        theEnvironmentalists.bribe();
        assertEquals(expectedSatisfactionRate, theEnvironmentalists.getSatisfactionRate());
    }

    public void test_bribing_a_faction_should_decrease_loyalists_satisfaction() {
        Faction theEnvironmentalists = new Environmentalists(20, 50);
        theEnvironmentalists.events.subscribe("bribe", new BriberyListener(liberals));
        theEnvironmentalists.bribe();
        assertEquals(70, liberals.getSatisfactionRate());
    }

    public void test_loyalists_cannot_be_bribed() {
        liberals.bribe();
        assertEquals(100, liberals.getSatisfactionRate());
    }

    // TODO test         - eliminateASupporter();
}
