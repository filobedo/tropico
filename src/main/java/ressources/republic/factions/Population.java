package ressources.republic.factions;

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

    private void setFactionNamesInFactionByName() {
        this.factionByName.put(Capitalists.class.getSimpleName(), null);
        this.factionByName.put(Communists.class.getSimpleName(), null);
        this.factionByName.put(Ecologists.class.getSimpleName(), null);
        this.factionByName.put(Liberals.class.getSimpleName(), null);
        this.factionByName.put(Loyalists.class.getSimpleName(), null);
        this.factionByName.put(Militarists.class.getSimpleName(), null);
        this.factionByName.put(Nationalists.class.getSimpleName(), null);
        this.factionByName.put(Religious.class.getSimpleName(), null);
    }

    public Faction createAndGetFaction(String factionName, int nbSupporters, int satisfactionRate) {
        try {
            return this.factionFactory.createFaction(factionName, nbSupporters, satisfactionRate);
        } catch (ClassNotFoundException e) {
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
        if(getTotalPopulation() != 0) {
            return getTotalSatisfactionRate() / getTotalPopulation();
        }
        return 0;
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

    public int getNbSupportersToEliminateToHaveEnoughFood(int foodUnits) {
        int populationWhoCanBeFed = foodUnits / GameRules.NEEDED_FOOD_PER_CITIZEN;
        return getTotalPopulation() - populationWhoCanBeFed;
    }

    public int increasePopulationRandomly() {
        int increasePopulationPercentage = generateRandomNumber(1, 10);
        int nbSupportersToGenerate = (int)Math.round(getTotalPopulation() * ((double)increasePopulationPercentage / 100));
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
            Faction faction = factionsSet.getValue();
            populationSummary.append(faction.toString());
        }
        populationSummary.append(toString());
        System.out.println(populationSummary);
    }

    public void displayAvailableFactions() {
        int nbCountFaction = 1;
        System.out.println("Choisissez dans cette liste des factions :");
        for (Faction faction : this.factionByName.values()) {
            String factionName = faction.getName();
            if(faction.canBeBribed()) {
                System.out.printf("\t%d. Les %s => %d$%n", nbCountFaction, factionName, faction.getBribePrice());
            }
            else {
                System.out.printf("\t%d. Les %s ne peuvent pas recevoir de pot de vin%n", nbCountFaction, factionName);
            }
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

    public String toString() {
        return (String.format("=> Population totale : %d", getTotalPopulation())) +
                (String.format(" - Satisfaction globale : %.2f%%", getGlobalSatisfactionRate()));
    }
}
