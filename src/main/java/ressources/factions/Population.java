package ressources.factions;

import ressources.listeners.BriberyListener;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {
    private final HashMap<String, Faction> factionByName = new HashMap<>();
    FactionFactory factionFactory = new FactionFactory();

    public Population(String configFileName) {
        setFactionsKeys();
        String configFilePath = "src/main/java/resources/" + configFileName;
        try (FileReader reader = new FileReader(configFilePath)) {
            Properties prop = new Properties();
            prop.load(reader);

            for(Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
                String currentFactionName = factionsSet.getKey();
                int satisfactionRate = parseInt(prop.getProperty(currentFactionName.toLowerCase() + ".satisfaction"));
                int nbSupporters = parseInt(prop.getProperty(currentFactionName.toLowerCase() + ".supporters"));

                Faction currentFaction = createFaction(currentFactionName, nbSupporters, satisfactionRate);
                factionsSet.setValue(currentFaction);
            }

            factionsSubscribeToBribeEventExceptLoyalists();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setFactionsKeys() {
        factionByName.put(Capitalists.class.getSimpleName(), null);
        factionByName.put(Communists.class.getSimpleName(), null);
        factionByName.put(Environmentalists.class.getSimpleName(), null);
        factionByName.put(Liberals.class.getSimpleName(), null);
        factionByName.put(Loyalists.class.getSimpleName(), null);
        factionByName.put(Militarists.class.getSimpleName(), null);
        factionByName.put(Nationalists.class.getSimpleName(), null);
        factionByName.put(Religious.class.getSimpleName(), null);
    }

    public Faction createFaction(String factionName, int nbSupporters, int satisfactionRate) {
        try {
            return factionFactory.createFaction(factionName, nbSupporters, satisfactionRate);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void factionsSubscribeToBribeEventExceptLoyalists() {
        Faction loyalists = factionByName.get(Loyalists.class.getSimpleName());
        for(Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            Faction faction = factionsSet.getValue();
            if(faction.getClass() != Loyalists.class) {
                faction.events.subscribe("bribe", new BriberyListener(loyalists));
            }
        }
    }

    public HashMap<String, Faction> getFactionByName() {
        return factionByName;
    }

    public int getTotalPopulation() {
        // TODO : AtomicInteger or use population = population + factionPopulation
        AtomicInteger population = new AtomicInteger();
        for(Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            int factionPopulation = factionsSet.getValue().getNbSupporters();
            population.set(population.get() + factionPopulation);
        }
        return population.get();
    }
    public void updateSatisfactionRateOnAllFactions(int percentagePoint) {
        for(Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            factionsSet.getValue().updateSatisfactionRate(percentagePoint);
        }
    }

    public void updateSatisfactionRateByFaction(int percentagePoint, String factionName) {
        factionByName.get(factionName).updateSatisfactionRate(percentagePoint);
    }

    public void updateNbSupportersOnAllFactions(int percentage) {
        for(Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            factionsSet.getValue().updateNbSupportersBy(percentage);
        }
    }

    public void updateNbSupportersByFaction(int percentage, String factionName) {
        factionByName.get(factionName).updateNbSupportersBy(percentage);
    }

    public int parseInt(String toBeParsed) {
        return Integer.parseInt(toBeParsed);
    }

    // TODO getNbPopulation

    // TODO : eliminateAleatPartisan() -> ce qui enlève 2% de satisfaction à toutes les factions

}
