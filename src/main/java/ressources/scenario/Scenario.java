package ressources.scenario;

import java.util.List;

public class Scenario extends GamePlay {
    public Scenario(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    public boolean canPlayEvents() {
        return false;
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
