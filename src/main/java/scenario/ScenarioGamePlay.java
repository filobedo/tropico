package scenario;

import exceptions.MissingEventsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioGamePlay extends GamePlay {

    public ScenarioGamePlay(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    /**
     * Test if all scenario events will be played
     * @return if scenario can be fully played
     */
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

    /**
     * If first season is not set in JSON file, then it will be set by determining what season could be the first
     * season to play the scenario fully
     * If all seasons have an equal number of events, it means all seasons can be the first season to be played
     * @throws MissingEventsException meaning that there is not enough events in the scenario file
     */
    @Override
    public void setFirstSeason() throws MissingEventsException {
        if(doAllSeasonsHaveEqualNbEvents()) {
            Season randomSeason = Season.getRandom();
            this.firstSeason = randomSeason;
            this.currentSeason = randomSeason;
        }
        else {
            for(Season season : Season.values()) {
                if(willAllEventsBePlayed(season)) {
                    this.firstSeason = season;
                    this.currentSeason = season;
                    return;
                }
            }
            throw new MissingEventsException("This scenario can't be played because it can not be played fully (all events won't be played). Try to add events in events 'poor' seasons");
        }
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
        int nbEventsFirstSeason = this.eventsBySeason.get(Season.WINTER).size();
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
     * Launch a simulation of the current scenario
     * When this simulation is done
     * If there is one or more "events" in any season, then the scenario won't be fully played (one or more event won't be played)
     * @return boolean meaning if all events will be played
     */
    public boolean willAllEventsBePlayed(Season firstSeasonSimulation) {
        ScenarioSimulation scenarioSimulation = new ScenarioSimulation(getNbEventsBySeason());
        scenarioSimulation.launch(firstSeasonSimulation);
        Map<Season, Integer> nbEventsBySeasonAfterSimulation = scenarioSimulation.getNbEventsBySeasonAfterSimulation();
        return doScenarioHaveNoEvents(nbEventsBySeasonAfterSimulation);
    }


    /**
     * Sets the next event according to the current season and the current year
     * If there is no more events : current event will be set to null
     */
    @Override
    public void nextEvent() {
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        try {
            this.currentEvent = seasonEvents.get(this.year);
        } catch (Exception ex) {
            this.currentEvent = null;
        }
    }

    /**
     * Place related events in the correct season
     * and between current year and last year possible
     * @param relatedEvents list of events
     */
    @Override
    public void placeRelatedEvents(List<Event> relatedEvents) {
        for(Event relatedEventToPlace : relatedEvents) {
            relatedEventToPlace.setIsARelatedEvent();
            Season seasonTarget = getSeasonWhereRelatedEventWillTakePlace();
            int yearTarget = getYearWhereRelatedEventWillTakePlace(this.year);
            if(yearTarget >= this.eventsBySeason.get(seasonTarget).size()) {
                this.eventsBySeason.get(seasonTarget).add(relatedEventToPlace);
            }
            else {
                this.eventsBySeason.get(seasonTarget).add(yearTarget, relatedEventToPlace);
            }
        }
    }

    /**
     * Get the season where related event will take place
     * @return  the next season of the basically last season who should have been
     *          played if the scenario had no related events placed in
     */
    public Season getSeasonWhereRelatedEventWillTakePlace() {
        ScenarioSimulation scenarioSimulation = new ScenarioSimulation(getNbEventsBySeason());
        scenarioSimulation.launch(this.firstSeason);
        return scenarioSimulation.getSeasonAfterLastSeason();
    }

    /**
     * Get random year between the current year (or next year if it's the last season of the current year)
     * and the last possible year
     * @param yearMin is minimum year possible to place the related event
     * @return year where to place a related event
     */
    public int getYearWhereRelatedEventWillTakePlace(int yearMin) {
        int yearMax = getMaxYearPossible();
        Random randomGenerator = new Random();
        if(isCurrentSeasonLastSeasonOfCurrentYear()) {
            yearMin += 1;
        }
        return randomGenerator.nextInt(yearMax - yearMin + 1 ) + yearMin;
    }

    public boolean isCurrentSeasonLastSeasonOfCurrentYear() {
        return Season.getNextSeason(this.currentSeason) == this.firstSeason;
    }

    /**
     * Get the maximum year that the scenario can play
     * @return max year possible
     */
    public int getMaxYearPossible() {
        Map<Season, Integer> nbEventsBySeason = getNbEventsBySeason();
        int nbTotalEvents = 0;
        for(Integer nbEvents : nbEventsBySeason.values()) {
            nbTotalEvents += nbEvents;
        }
        if(doAllSeasonsHaveEqualNbEvents()) {
            return (int)Math.ceil((double)nbTotalEvents / Season.values().length) + 1;
        }
        else {
            return (int)Math.ceil((double)nbTotalEvents / Season.values().length);
        }
    }
}
