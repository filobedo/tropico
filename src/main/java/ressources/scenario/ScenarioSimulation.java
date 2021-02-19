package ressources.scenario;

import java.util.Map;

public class ScenarioSimulation {
    private Map<Season, Integer> nbEventsBySeason;
    private Season seasonAfterLastSeason;

    public ScenarioSimulation(Map<Season, Integer> nbEventsBySeason) {
        this.nbEventsBySeason = nbEventsBySeason;
    }

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
