package ressources.scenario;

import java.util.List;

public class Scenario extends GamePlay {
    public Scenario(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    public void nextEvent() {
        // Get next event ?? See with teacher
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        this.currentEvent = seasonEvents.get(this.year);
    }
}
