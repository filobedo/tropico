//import junit.framework.TestCase;
//import org.junit.Assert;
//import org.junit.Test;
//import ressources.event.Event;
//
//public class EventTest extends TestCase {
//
//    public void test_get_season(){
//        Event event = new Event("src/main/java/ressources/scenario_test.json");
//
//        Assert.assertEquals("Été", event.getSeason(0));
//        Assert.assertEquals("Hiver", event.getSeason(1));
//    }
//
//    public void test_get_event(){
//        Event event = new Event("src/main/java/ressources/scenario_test.json");
//        String res = "Catastrophe ! Une météorite est tombée !\n" +
//                "Une météorite a ravagé notre île, il faut choisir une solution si vous voulez réduire son impact !\n" +
//                "Impact direct :\n" +
//                "\tAgriculture : -2\n" +
//                "\tIndustrie : -5\n" +
//                "\tPopulation : -15\n";
//        Assert.assertEquals(res, event.getCurrentEvent());
//        event.setCurrentEvent(1, 0);
//        res = "Une sécheresse s'est déclaré !\n" +
//                "Notre population crie famine, vous devez faire quelques choses !\n" +
//                "Impact direct :\n" +
//                "\tAgriculture : -10\n" +
//                "\tPopulation : -5\n";
//        Assert.assertEquals(res, event.getCurrentEvent());
//    }
//
//    public void test_get_choices(){
//        Event event = new Event("src/main/java/ressources/scenario_test.json");
//        String res = "--- Choix 1 ---\n" +
//                "Déblayer\n" +
//                "Vous gagnerez de la thunas\n" +
//                "Effet :\n" +
//                "\tArgent : 150\n" +
//                "\tCapitalistes :\n" +
//                "\t\tPartisans : 5\n" +
//                "\t\tTaux de satisfaction : 10\n" +
//                "\tEnvironmentalistes :\n" +
//                "\t\tTaux de satisfaction : -10\n" +
//                "\n" +
//                "\n" +
//                "--- Choix 2 ---\n" +
//                "Laisser\n" +
//                "vous gagnerez de la fertilité\n" +
//                "Effet :\n" +
//                "\tAgriculture : 15\n\n\n";
//        event.displayCurrentEvent();
//        Assert.assertEquals(res, event.getCurrentEventChoices());
//        event.setCurrentEvent(0, 1);
//        res = "--- Choix 1 ---\n" +
//                "Déblayer\n" +
//                "Vous résoudrez les problèmes de transit.\n" +
//                "Effet :\n" +
//                "\tArgent : -200\n" +
//                "\n" +
//                "\n" +
//                "--- Choix 2 ---\n" +
//                "S'en fiche\n" +
//                "Ne pas toucher à la neige ! Laisser les routes pleine de neige.\n" +
//                "Effet :\n" +
//                "\tAgriculture : -15\n" +
//                "\tIndustrie : -15\n" +
//                "\n" +
//                "\n";
//        assertEquals(res, event.getCurrentEventChoices());
//    }
//}
