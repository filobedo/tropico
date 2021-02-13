package ressources.game;

public class UserInput {
    public static void pressAnyKeyToContinue()
    {
        System.out.println("Appuyez sur la touche 'Entrer' pour continuer...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }
}
