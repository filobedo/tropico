package gameplay;

import java.util.List;
import java.util.Random;

public class SandboxGamePlay extends GamePlay {

    public SandboxGamePlay(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    /**
     * Test if there is at least one event in each season
     * @return if scenario can be fully played
     */
    @Override
    public boolean canPlayEvents() {
        if(this.firstSeason == null) {
            setFirstSeason();
        }
        for(Season season : Season.values()) {
            if(this.eventsBySeason.get(season).size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * If the first season is not set in JSON file, it will be generated randomly
     */
    @Override
    public void setFirstSeason()  {
        // Generate random season
        Season firstSeason = Season.getRandom();
        this.firstSeason = firstSeason;
        this.currentSeason = firstSeason;
    }

    /**
     * Set next random event according to the current season
     */
    @Override
    public void nextEvent() {
        Random randomGenerator = new Random();
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        int indexRandomEvent = randomGenerator.nextInt(seasonEvents.size());
        this.currentEvent = seasonEvents.get(indexRandomEvent);
        if(this.currentEvent.isARelatedEvent()) {
            seasonEvents.remove(indexRandomEvent);
        }
    }

    /**
     * Place a list of (related) events in the list of events
     * Placing is fully random then when these events are played, they are deleted from event list
     * @param relatedEvents
     */
    @Override
    public void placeRelatedEvents(List<Event> relatedEvents) {
        for(Event relatedEventToPlace : relatedEvents) {
            relatedEventToPlace.setIsARelatedEvent();
            Season seasonTarget = Season.getRandom();
            int yearTarget = randomIndexInSeason(seasonTarget);
            if(yearTarget >= this.eventsBySeason.get(seasonTarget).size()) {
                this.eventsBySeason.get(seasonTarget).add(relatedEventToPlace);
            }
            else {
                this.eventsBySeason.get(seasonTarget).add(yearTarget, relatedEventToPlace);
            }
        }
    }

    public int randomIndexInSeason(Season seasonTarget) {
        Random randomGenerator = new Random();
        int min = 0;
        int max = this.eventsBySeason.get(seasonTarget).size();
        return randomGenerator.nextInt(max - min + 1 ) + min;
    }
}
