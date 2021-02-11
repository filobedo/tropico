package ressources.factions;

import ressources.listeners.BriberyListener;

import javax.naming.ConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {
    private final HashMap<String, Faction> factionByName = new HashMap<>();
    FactionFactory factionFactory = new FactionFactory();

    public Population(String configFilePath) {
        setFactionNamesInFactionByName();
        setFactionValuesWithConfigProperties(configFilePath);
    }

    public void setFactionNamesInFactionByName() {
        factionByName.put(Capitalists.class.getSimpleName(), null);
        factionByName.put(Communists.class.getSimpleName(), null);
        factionByName.put(Environmentalists.class.getSimpleName(), null);
        factionByName.put(Liberals.class.getSimpleName(), null);
        factionByName.put(Loyalists.class.getSimpleName(), null);
        factionByName.put(Militarists.class.getSimpleName(), null);
        factionByName.put(Nationalists.class.getSimpleName(), null);
        factionByName.put(Religious.class.getSimpleName(), null);
    }

    public void setFactionValuesWithConfigProperties(String configFilePath) {
        try (FileReader reader = new FileReader(configFilePath)) {
            Properties properties = new Properties();
            properties.load(reader);
            setFactionValuesInFactionByName(properties);
            factionsSubscribeToBribeEventExceptLoyalists();
        } catch (IOException | ConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    // TODO to see -> AbsentInformationException not working
    //  java: package com.sun.jdi does not exist
    public void setFactionValuesInFactionByName(Properties properties) throws ConfigurationException {
        for(Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();

            if(doesFactionExistInConfigProperties(currentFactionName, properties)) {
                int satisfactionRate = parseInt(properties.getProperty(currentFactionName.toLowerCase() + ".satisfaction"));
                int nbSupporters = parseInt(properties.getProperty(currentFactionName.toLowerCase() + ".supporters"));

                Faction currentFaction = createAndGetFaction(currentFactionName, nbSupporters, satisfactionRate);
                factionsSet.setValue(currentFaction);
            }
            else {
                String exceptionMessage = String.format("%s faction doesn't exist in config properties!", currentFactionName);
                throw new ConfigurationException(exceptionMessage);
            }
        }
    }

    public boolean doesFactionExistInConfigProperties(String factionName, Properties properties) {
        String factionSatisfactionRate = factionName.toLowerCase() + ".satisfaction";
        String factionNbSupporters = factionName.toLowerCase() + ".supporters";
        return properties.containsKey(factionSatisfactionRate) && properties.containsKey(factionNbSupporters);
    }

    public Faction createAndGetFaction(String factionName, int nbSupporters, int satisfactionRate) {
        try {
            return factionFactory.createFaction(factionName, nbSupporters, satisfactionRate);
        } catch (ClassNotFoundException e) { // TODO Faut-il throw; ? Bonnes pratiques ? Quelles exception faut il throw
            e.printStackTrace();
        }
        return null;
    }

    public void factionsSubscribeToBribeEventExceptLoyalists() {
        Faction loyalists = factionByName.get(Loyalists.class.getSimpleName());
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            Faction faction = factionsSet.getValue();
            if (faction.getClass() != Loyalists.class) {
                faction.events.subscribe("bribe", new BriberyListener(loyalists));
            }
        }
    }

    public HashMap<String, Faction> getFactionByName() {
        return factionByName;
    }

    public Faction getFaction(String name) {
        return factionByName.get(name);
    }

    public int getTotalPopulation() {
        AtomicInteger population = new AtomicInteger();
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            int currentFactionPopulation = factionsSet.getValue().getNbSupporters();
            population.addAndGet(currentFactionPopulation);
        }
        return population.get();
    }

    public double getGlobalSatisfactionRate() {
        double totalSatisfactionRate = 0;
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            Faction currentFaction = factionsSet.getValue();
            double satisfactionRate = currentFaction.getSatisfactionRate();
            double nbSupporters = currentFaction.getNbSupporters();
            totalSatisfactionRate += nbSupporters * satisfactionRate;
        }
        return totalSatisfactionRate / getTotalPopulation();
    }

    public void updateSatisfactionRateByFaction(int percentagePoint, String factionName) {
        factionByName.get(factionName).updateSatisfactionRate(percentagePoint);
    }

    public void updateNbSupportersByFaction(int percentage, String factionName) {
        factionByName.get(factionName).updateNbSupportersBy(percentage);
    }

    public void updateNbSupportersOnMultipleFactions(int percentagePoint, String[] factionsToUpdate) {
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();
            if (isArrayContainingString(factionsToUpdate, currentFactionName)) {
                factionsSet.getValue().updateNbSupportersBy(percentagePoint);
            }
        }
    }

    public void updateSatisfactionRateOnMultipleFactions(int percentagePoint, String[] factionsToUpdate) {
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();
            if (isArrayContainingString(factionsToUpdate, currentFactionName)) {
                factionsSet.getValue().updateSatisfactionRate(percentagePoint);
            }
        }
    }

    public void updateSatisfactionRateOnAllFactions(int percentagePoint) {
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            factionsSet.getValue().updateSatisfactionRate(percentagePoint);
        }
    }

    public void updateNbSupportersOnAllFactions(int percentage) {
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            factionsSet.getValue().updateNbSupportersBy(percentage);
        }
    }


    // TODO nombre d'unité de nourriture ou nombre de partisans à éliminer selon l'implémentation de la classe au desssus
    public void eliminateSupportersUntilEnoughFood(int foodUnits) {
        double neededFood = getTotalPopulation() * 4;
        double difference = neededFood - foodUnits;
        AtomicInteger nbSupportersToEliminate = new AtomicInteger((int)Math.ceil(difference / 4));
        Random randomGenerator = new Random();
        List<String> factionNames = new ArrayList<>(factionByName.keySet());

        while(nbSupportersToEliminate.get() != 0) {
            int randomIndex = randomGenerator.nextInt(factionNames.size());
            String randomFactionName = factionNames.get(randomIndex);
            this.factionByName.get(randomFactionName).eliminateASupporter();
            nbSupportersToEliminate.decrementAndGet();
        }
        updateSatisfactionRateOnAllFactions(-2);
    }

    public boolean isArrayContainingString(String[] searchList, String searched) {
        return Arrays.asList(searchList).contains(searched);
    }

    public int parseInt(String toBeParsed) {
        if(!toBeParsed.equals("")) {
            return Integer.parseInt(toBeParsed);
        }
        return 0;
    }
}
