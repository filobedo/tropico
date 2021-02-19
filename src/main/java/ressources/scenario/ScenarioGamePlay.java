package ressources.scenario;

import exceptions.MissingEventsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioGamePlay extends GamePlay {

    public ScenarioGamePlay(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    @Override
    public boolean canPlayEvents() {
        if(doScenarioHaveNoEvents(getNbEventsBySeason())) {
            return false;
        }
        if(this.firstSeason == null) {
            try {
                setFirstSeason();
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        if(doFirstSeasonHaveNoEvent()) {
            return false;
        }
        if(doAllSeasonsHaveEqualNbEvents()) {
            return true;
        }
        return willAllEventsBePlayed(this.firstSeason);
    }

    @Override
    public void setFirstSeason() throws MissingEventsException {
        // Generate the good season that can be the starting season
        for(Season season : Season.values()) {
            if(willAllEventsBePlayed(season)) {
                this.firstSeason = season;
                this.currentSeason = season;
                return;
            }
        }
        throw new MissingEventsException("This scenario can't be played because it can not be played fully (all events won't be played)");
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
        return this.eventsBySeason.get(this.firstSeason).size() == 0;
    }

    public boolean doAllSeasonsHaveEqualNbEvents() {
        int nbEventsFirstSeason = this.eventsBySeason.get(this.firstSeason).size();
        for(Season season : Season.values()) {
            if(nbEventsFirstSeason != this.eventsBySeason.get(season).size()) {
                return false;
            }
        }
        return true;
    }

    public Map<Season, Integer> getNbEventsBySeason() {
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
     * @return boolean meaning if all events will be played
     */
    public boolean willAllEventsBePlayed(Season firstSeasonSimulation) {
        Map<Season, Integer> nbEventsBySeason = getNbEventsBySeason();
        ScenarioSimulation scenarioSimulation = new ScenarioSimulation(nbEventsBySeason);
        scenarioSimulation.launch(firstSeasonSimulation);
        nbEventsBySeason = scenarioSimulation.getNbEventsBySeasonAfterSimulation();
        return doScenarioHaveNoEvents(nbEventsBySeason);
    }

    @Override
    public void nextEvent() {
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        try {
            this.currentEvent = seasonEvents.get(this.year);
        } catch (Exception ex) {
            this.currentEvent = null;
        }
    }

    @Override
    public void placeRelatedEvents(List<Event> relatedEvents) {
        // Get season where I can place it
        for(Event relatedEventToPlace : relatedEvents) {
            Season seasonTarget = getSeasonWhereRelatedEventWillTakePlace();
            // Insert it in last year or current year
            int yearTarget = getYearWhereRelatedEventWillTakePlace(this.year);
            if(yearTarget > this.eventsBySeason.get(seasonTarget).size()) {
                this.eventsBySeason.get(seasonTarget).add(relatedEventToPlace);
            }
            else {
                this.eventsBySeason.get(seasonTarget).add(yearTarget, relatedEventToPlace);
            }
        }
    }

    public Season getSeasonWhereRelatedEventWillTakePlace() {
        Map<Season, Integer> nbEventsBySeason = getNbEventsBySeason();
        ScenarioSimulation scenarioSimulation = new ScenarioSimulation(nbEventsBySeason);
        scenarioSimulation.launch(this.firstSeason);
        return scenarioSimulation.getSeasonAfterLastSeason();
    }

    public int getYearWhereRelatedEventWillTakePlace(int yearMin) {
        int yearMax = getMaxYearPossible();
        // Randomly between currentYear and last year possible
        Random randomGenerator = new Random();
        if(Season.getNextSeason(this.currentSeason) == this.firstSeason) {
            yearMin += 1;
        }
        return randomGenerator.nextInt(yearMax - yearMin + 1 ) + yearMin;
    }

    public int getMaxYearPossible() {
        Map<Season, Integer> nbEventsBySeason = getNbEventsBySeason();
        int nbTotalEvents = 0;
        for(Integer nbEvents : nbEventsBySeason.values()) {
            nbTotalEvents += nbEvents;
        }
        return (int)Math.ceil((double)nbTotalEvents / Season.values().length);
    }
}
