package ressources.factions;

import ressources.game.GameRules;
import ressources.listeners.BriberyListener;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Population {
    private final LinkedHashMap<String, Faction> factionByName = new LinkedHashMap<>();
    private final FactionFactory factionFactory = new FactionFactory();

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

    public double getTotalSatisfactionRate() {
        double totalSatisfactionRate = 0;
        for (Map.Entry<String, Faction> factionsSet : this.factionByName.entrySet()) {
            Faction currentFaction = factionsSet.getValue();
            double satisfactionRate = currentFaction.getSatisfactionRate();
            double nbSupporters = currentFaction.getNbSupporters();
            totalSatisfactionRate += nbSupporters * satisfactionRate;
        }
        return totalSatisfactionRate;
    }

    public double getGlobalSatisfactionRate() {
        return getTotalSatisfactionRate() / getTotalPopulation();
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
    // delete
    public boolean isArrayContainingString(String[] searchList, String searched) {
        return Arrays.asList(searchList).contains(searched);
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
    // TODO TEST
    public boolean eliminateSupportersUntilEnoughFood(int nbSupportersToEliminate) {
        boolean hasEliminatedSupporters = false;
        AtomicInteger countNbSupporters = new AtomicInteger(nbSupportersToEliminate);
        while(countNbSupporters.get() > 0) {
            Faction randomFaction = getRandomFaction();
            randomFaction.eliminateASupporter();
            countNbSupporters.decrementAndGet();
            hasEliminatedSupporters = true;
        }
        if(hasEliminatedSupporters) {
            updateSatisfactionRateOnAllFactions(-2);
        }
        return hasEliminatedSupporters;
    }

    // TODO TEST
    public int getNbSupportersToEliminateToHaveEnoughFood(int foodUnits) {
        int populationWhoCanBeFed = foodUnits / GameRules.NEEDED_FOOD_PER_CITIZEN;
        return getTotalPopulation() - populationWhoCanBeFed;
    }

    // TODO TEST
    public int increasePopulationRandomly() {
        int increasePopulationPercentage = generateRandomNumber(1, 10);
        int nbSupportersToGenerate = getTotalPopulation() * (1 + (increasePopulationPercentage / 100));
        int nbSupportersGenerated = nbSupportersToGenerate;
        while(nbSupportersToGenerate > 0) {
            Faction randomFaction = getRandomFaction();
            randomFaction.addSupporters(1);
            nbSupportersToGenerate -= 1;
        }
        return nbSupportersGenerated;
    }

    public Faction getRandomFaction() {
        List<Faction> factionNames = new ArrayList<>(this.factionByName.values());
        int randomIndex = generateRandomNumber(0, factionNames.size() - 1);
        return factionNames.get(randomIndex);
    }

    public int generateRandomNumber(int min, int max) {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(max - min + 1 ) + min;
    }

    public void displaySummary() {
        StringBuilder populationSummary = new StringBuilder();
        System.out.printf("Population :%n");
        for (Map.Entry<String, Faction> factionsSet : factionByName.entrySet()) {
            String name = factionsSet.getKey();
            Faction faction = factionsSet.getValue();
            String factionSummary = String.format("\t%s : %n", name);
            factionSummary += String.format("\t\tPartisans : %d | Satisfaction : %d%%%n", faction.getNbSupporters(), faction.getSatisfactionRate());
            populationSummary.append(factionSummary);
        }
        populationSummary.append(String.format("=> Population totale : %d", getTotalPopulation()));
        populationSummary.append(String.format(" - Satisfaction globale : %.2f%%", getGlobalSatisfactionRate()));
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
