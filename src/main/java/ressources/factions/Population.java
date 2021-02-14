package ressources.factions;

import ressources.listeners.BriberyListener;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {
    private final LinkedHashMap<String, Faction> factionByName = new LinkedHashMap<>();
    private FactionFactory factionFactory = new FactionFactory();

    public Population() {
        setFactionNamesInFactionByName();
    }

    public void setFactionNamesInFactionByName() {
        this.factionByName.put(Capitalists.class.getSimpleName(), null);
        this.factionByName.put(Communists.class.getSimpleName(), null);
        this.factionByName.put(Environmentalists.class.getSimpleName(), null);
        this.factionByName.put(Liberals.class.getSimpleName(), null);
        this.factionByName.put(Loyalists.class.getSimpleName(), null);
        this.factionByName.put(Militarists.class.getSimpleName(), null);
        this.factionByName.put(Nationalists.class.getSimpleName(), null);
        this.factionByName.put(Religious.class.getSimpleName(), null);
    }

    public Faction createAndGetFaction(String factionName, int nbSupporters, int satisfactionRate) {
        try {
            return this.factionFactory.createFaction(factionName, nbSupporters, satisfactionRate);
        } catch (ClassNotFoundException e) { // TODO Faut-il throw; ? Bonnes pratiques ? Quelles exception faut il throw
            e.printStackTrace();
        }
        return null;
    }

    public void factionsSubscribeToBribeEventExceptLoyalists() {
        Faction loyalists = this.factionByName.get(Loyalists.class.getSimpleName());
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            Faction faction = factionsSet.getValue();
            if (faction.getClass() != Loyalists.class) {
                faction.events.subscribe("bribe", new BriberyListener(loyalists));
            }
        }
    }

    public Map<String, Faction> getFactionByName() {
        return this.factionByName;
    }

    public Faction getFaction(String name) {
        return this.factionByName.get(name);
    }

    public int getTotalPopulation() {
        AtomicInteger population = new AtomicInteger();
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            int currentFactionPopulation = factionsSet.getValue().getNbSupporters();
            population.addAndGet(currentFactionPopulation);
        }
        return population.get();
    }

    public double getGlobalSatisfactionRate() {
        double totalSatisfactionRate = 0;
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            Faction currentFaction = factionsSet.getValue();
            double satisfactionRate = currentFaction.getSatisfactionRate();
            double nbSupporters = currentFaction.getNbSupporters();
            totalSatisfactionRate += nbSupporters * satisfactionRate;
        }
        return totalSatisfactionRate / getTotalPopulation();
    }

    public int getNbFactions() {
        return this.factionByName.size();
    }

    public void updateSatisfactionRateByFaction(int percentagePoint, String factionName) {
        this. factionByName.get(factionName).updateSatisfactionRate(percentagePoint);
    }

    public void updateNbSupportersByFaction(int percentage, String factionName) {
        this.factionByName.get(factionName).updateNbSupportersBy(percentage);
    }

    // delete
    public void updateNbSupportersOnMultipleFactions(int percentagePoint, String[] factionsToUpdate) {
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();
            if (isArrayContainingString(factionsToUpdate, currentFactionName)) {
                factionsSet.getValue().updateNbSupportersBy(percentagePoint);
            }
        }
    }

    // delete
    public void updateSatisfactionRateOnMultipleFactions(int percentagePoint, String[] factionsToUpdate) {
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            String currentFactionName = factionsSet.getKey();
            if (isArrayContainingString(factionsToUpdate, currentFactionName)) {
                factionsSet.getValue().updateSatisfactionRate(percentagePoint);
            }
        }
    }

    public void updateSatisfactionRateOnAllFactions(int percentagePoint) {
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            factionsSet.getValue().updateSatisfactionRate(percentagePoint);
        }
    }

    public void updateNbSupportersOnAllFactions(int percentage) {
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            factionsSet.getValue().updateNbSupportersBy(percentage);
        }
    }


    // TODO nombre d'unité de nourriture ou nombre de partisans à éliminer selon l'implémentation de la classe au desssus
    // renvoie si la fonction a eliminé des gens
    public boolean eliminateSupportersUntilEnoughFood(int foodUnits) {
        boolean hasElimnatedSupporters = false;
        double neededFood = getTotalPopulation() * 4;
        double difference = neededFood - foodUnits;
        AtomicInteger nbSupportersToEliminate = new AtomicInteger((int)Math.ceil(difference / 4));
        Random randomGenerator = new Random();
        List<String> factionNames = new ArrayList<>(this.factionByName.keySet());

        while(nbSupportersToEliminate.get() > 0) {
            int randomIndex = randomGenerator.nextInt(factionNames.size());
            String randomFactionName = factionNames.get(randomIndex);
            this.factionByName.get(randomFactionName).eliminateASupporter();
            nbSupportersToEliminate.decrementAndGet();
            hasElimnatedSupporters = true;
        }
        if(hasElimnatedSupporters) {
            updateSatisfactionRateOnAllFactions(-2);
        }
        return hasElimnatedSupporters;
    }

    public boolean isArrayContainingString(String[] searchList, String searched) {
        return Arrays.asList(searchList).contains(searched);
    }

    // DELETE
    public int parseInt(String toBeParsed) {
        if(!toBeParsed.equals("")) {
            return Integer.parseInt(toBeParsed);
        }
        return 0;
    }

    public void displaySummary() {
        StringBuilder populationSummary = new StringBuilder();
        populationSummary.append("Population :%n");
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            String name = factionsSet.getKey();
            Faction faction = factionsSet.getValue();
            String factionSummary = String.format("%s :%n", name);
            factionSummary += String.format("\tPartisans : %d - Satisfaction : %d%%%n", faction.getNbSupporters(), faction.getSatisfactionRate());
            populationSummary.append(factionSummary);
        }
        populationSummary.append(String.format("=> Population totale : %d", getTotalPopulation()));
        populationSummary.append(String.format(" - Satisfaction globale : %.2f", getGlobalSatisfactionRate()));
        System.out.println(populationSummary);
    }

    public void displayAvailableFactions() {
        int nbCountFaction = 1;
        System.out.println("Choisissez dans cette liste des factions :");
        for (String factionName : this.factionByName.keySet()) {
            System.out.printf("\t%d. %s%n", nbCountFaction, factionName);
            nbCountFaction += 1;
        }
    }

    public String getFactionNameByIndex(int playerChoice) {
        int nbCountFaction = 0;
        int playerChoiceIndex = playerChoice - 1;
        for (String factionName : this.factionByName.keySet()) {
            if(playerChoiceIndex == nbCountFaction) {
                return factionName;
            }
            nbCountFaction += 1;
        }
        return null;
    }
}
