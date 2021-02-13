package ressources.factions;

import ressources.listeners.BriberyListener;
import ressources.parser.IParser;

import javax.naming.ConfigurationException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {
    private final Map<String, Faction> factionByName = new HashMap<>();
    private FactionFactory factionFactory = new FactionFactory();
    private IParser parser;

    public Population() {
        setFactionNamesInFactionByName();
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

    public Map<String, Faction> getFactionByName() {
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

    // delete
    public void updateNbSupportersOnMultipleFactions(int percentagePoint, String[] factionsToUpdate) {
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();
            if (isArrayContainingString(factionsToUpdate, currentFactionName)) {
                factionsSet.getValue().updateNbSupportersBy(percentagePoint);
            }
        }
    }

    // delete
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
