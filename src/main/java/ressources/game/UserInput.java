package ressources.game;

public class UserInput {
    public static void pressAnyKeyToContinue()
    {
        System.out.printf("%n%nAppuyez sur la touche 'Entrer' pour continuer...%n");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }
}
