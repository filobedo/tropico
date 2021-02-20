import junit.framework.TestCase;
import org.junit.Assert;
import republic.factions.*;

public class FactionFactoryTest extends TestCase {
    private FactionFactory factionFactory;

    protected void setUp() {
        factionFactory = new FactionFactory();
    }

    public void test_create_faction_can_throw_class_not_found_exception() {
        try {
            factionFactory.createFaction("FakeFaction", 50, 100);
            Assert.fail("ClassNotFoundException not thrown in " + this.getName());
        } catch (ClassNotFoundException ex) {
            assertEquals(ClassNotFoundException.class, ex.getClass());
        }
    }

    public void test_create_faction_can_return_null() {
        try {
            Faction createdFaction = factionFactory.createFaction(null, 50, 100);
            assertNull(createdFaction);
        } catch (Exception ex){
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_capitalists() {
        try {
            Faction capitalists = factionFactory.createFaction("CAPitAliSts", 50, 100);
            assertEquals(Capitalists.class, capitalists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_communists() {
        try {
            Faction communists = factionFactory.createFaction("COMMUNISTS", 50, 100);
            assertEquals(Communists.class, communists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_ecologists() {
        try {
            Faction ecologists = factionFactory.createFaction("Ecologists", 50, 100);
            assertEquals(Ecologists.class, ecologists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_liberals() {
        try {
            Faction liberals = factionFactory.createFaction("liberals", 50, 100);
            assertEquals(Liberals.class, liberals.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_loyalists() {
        try {
            Faction loyalists = factionFactory.createFaction("LoYALists", 50, 100);
            assertEquals(Loyalists.class, loyalists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_militarists() {
        try {
            Faction militarists = factionFactory.createFaction("MilitaRists", 50, 100);
            assertEquals(Militarists.class, militarists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_nationalists() {
        try {
            Faction nationalists = factionFactory.createFaction("NatioNalIsts", 50, 100);
            assertEquals(Nationalists.class, nationalists.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }

    public void test_create_faction_can_create_religious() {
        try {
            Faction religious = factionFactory.createFaction("RelIgioUs", 50, 100);
            assertEquals(Religious.class, religious.getClass());
        } catch (Exception ex) {
            Assert.fail(ex.getClass() + " shouldn't have been thrown");
            ex.printStackTrace();
        }
    }
}
