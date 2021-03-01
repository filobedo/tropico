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
        if(doASeasonHasNotEvents(getNbEventsBySeason())) {
            return false;
        }
        if(this.firstSeason == null) {
            setFirstSeason();
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

    @Override
    public void displayContext() {
        System.out.printf("%n%s%n", getName());
        System.out.printf("%s%n", getStory());
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
     * @param relatedEvents related events due to a player's choice
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
