package ressources.factions;

public class FactionFactory {
    public Faction createFaction(String name, int nbSupporters, int satisfactionRate) throws ClassNotFoundException{
        String factionName = name.toUpperCase();
        if(factionName == null) { return null; }
        switch (factionName) {
            case "CAPITALISTS":
                return new Capitalists(nbSupporters, satisfactionRate);
            case "COMMUNISTS":
                return new Communists(nbSupporters, satisfactionRate);
            case "ENVIRONMENTALISTS":
                return new Environmentalists(nbSupporters, satisfactionRate);
            case "LIBERALS":
                return new Liberals(nbSupporters, satisfactionRate);
            case "LOYALISTS":
                return new Loyalists(nbSupporters, satisfactionRate);
            case "MILITARISTS":
                return new Militarists(nbSupporters, satisfactionRate);
            case "NATIONALISTS":
                return new Nationalists(nbSupporters, satisfactionRate);
            case "RELIGIOUS":
                return new Religious(nbSupporters, satisfactionRate);
            default:
                throw new ClassNotFoundException("Invalid class name!");
        }
    }
}
