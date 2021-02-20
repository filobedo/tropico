package scenario;

import java.util.Map;

public class ScenarioSimulation {
    private Map<Season, Integer> nbEventsBySeason;
    private Season seasonAfterLastSeason;

    public ScenarioSimulation(Map<Season, Integer> nbEventsBySeason) {
        this.nbEventsBySeason = nbEventsBySeason;
    }

    /**
     * Launch the scenario simulation
     * Simulates scenario by decreasing the number of events by season in the map
     * Simulates the fact that a player is playing this scenario
     * Sets the number of events by season after the simulation
     * Sets the next season after the last season played by the simulation
     * @param firstSeason launch simulation with this first season
     */
    public void launch(Season firstSeason) {
        int currentSeasonNbEvents = nbEventsBySeason.get(firstSeason);
        Season currentSimulatedSeason = firstSeason;

        while(currentSeasonNbEvents != 0) {
            Integer newNbEvents = nbEventsBySeason.get(currentSimulatedSeason) - 1;
            nbEventsBySeason.put(currentSimulatedSeason, newNbEvents);
            currentSimulatedSeason = Season.getNextSeason(currentSimulatedSeason);
            currentSeasonNbEvents = nbEventsBySeason.get(currentSimulatedSeason);
        }
        this.seasonAfterLastSeason = currentSimulatedSeason;
    }

    public Map<Season, Integer> getNbEventsBySeasonAfterSimulation() {
        return nbEventsBySeason;
    }

    public Season getSeasonAfterLastSeason() {
        return seasonAfterLastSeason;
    }
}
