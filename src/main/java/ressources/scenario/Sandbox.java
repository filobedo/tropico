package ressources.scenario;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Sandbox extends GamePlay {
    public Sandbox(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    public boolean canPlayEvents() {
        for(Season season : Season.values()) {
            if(this.eventsBySeason.get(season).size() == 0) {
                return false;
            }
        }
        return true;
    }

    public void nextEvent() {
        // Get next random event according to the season
        Random randomGenerator = new Random();
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        int indexRandomEvent = randomGenerator.nextInt(seasonEvents.size());
        this.currentEvent = seasonEvents.get(indexRandomEvent);
        if(this.currentEvent.isARelatedEvent()) {
            seasonEvents.remove(indexRandomEvent);
        }
    }

    public void placeRelatedEvents(List<Event> relatedEvents) {
        for(Event relatedEventToPlace : relatedEvents) {
            relatedEventToPlace.setIsARelatedEvent();
            Season seasonTarget = Season.getRandom();
            int yearTarget = randomIndexInSeason(seasonTarget);
            this.eventsBySeason.get(seasonTarget).add(yearTarget, relatedEventToPlace);
        }

    }

    public int randomIndexInSeason(Season seasonTarget) {
        Random randomGenerator = new Random();
        int min = 0;
        int max = this.eventsBySeason.get(seasonTarget).size() - 1;
        return randomGenerator.nextInt(max - min + 1 ) + min;
    }
}
