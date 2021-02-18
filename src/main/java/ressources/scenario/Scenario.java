package ressources.scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scenario extends GamePlay {
    public Scenario(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    public boolean canPlayEvents() {
        if(doScenarioHaveNoEvents(getNumberOfEventsInAList())) {
            return false;
        }
        if(doFirstSeasonHaveNoEvent()) {
            return false;
        }
        if(doAllSeasonsHaveEqualNbEvents()) {
            return true;
        }
        Map<Season, Integer> nbEventsBySeason = getNumberOfEventsInAList();
        return willAllEventsBePlayed(nbEventsBySeason);
    }

    public boolean doScenarioHaveNoEvents(Map<Season, Integer> nbEventsBySeason) {
        for(Integer nbEvents : nbEventsBySeason.values()) {
            if(nbEvents > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean doFirstSeasonHaveNoEvent() {
        return this.eventsBySeason.get(this.currentSeason).size() == 0;
    }

    public boolean doAllSeasonsHaveEqualNbEvents() {
        int nbEventsFirstSeason = this.eventsBySeason.get(this.currentSeason).size();
        for(Season season : Season.values()) {
            if(nbEventsFirstSeason != this.eventsBySeason.get(season).size()) {
                return false;
            }
        }
        return true;
    }

    public Map<Season, Integer> getNumberOfEventsInAList() {
        Map<Season, Integer> nbEventsBySeason = new HashMap<>();
        for(Season season : Season.values()) {
            nbEventsBySeason.put(season, this.eventsBySeason.get(season).size());
        }
        return nbEventsBySeason;
    }

    /**
     * Simulates scenario by decreasing the number of events by season in the map
     * When this simulation is done
     * If there is one or more "events" in any seasons, then the scenario won't be fully played
     * @param nbEventsBySeason Number of events according to the season in the scenario JSON file
     * @return boolean meaning if all events will be played
     */
    public boolean willAllEventsBePlayed(Map<Season, Integer> nbEventsBySeason) {
        int currSeasonNbEvents = nbEventsBySeason.get(this.currentSeason);
        Season currSeason = this.currentSeason;
        while(currSeasonNbEvents != 0) {
            Integer newNbEvents = nbEventsBySeason.get(currSeason) - 1;
            nbEventsBySeason.put(currSeason, newNbEvents);
            currSeason = Season.getNextSeason(currSeason);
            currSeasonNbEvents = nbEventsBySeason.get(currSeason);
        }
        return doScenarioHaveNoEvents(nbEventsBySeason);
    }

    public void nextEvent() {
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        try {
            this.currentEvent = seasonEvents.get(this.year);
        } catch (Exception ex) {
            this.currentEvent = null;
        }
    }

    public void placeRelatedEvents() {

    }
}
