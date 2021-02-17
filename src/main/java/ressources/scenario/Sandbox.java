package ressources.scenario;

import java.util.List;
import java.util.Random;

public class Sandbox extends GamePlay {
    public Sandbox(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    public void nextEvent() {
        // Get next random event according to the season
        Random random = new Random();
        List<Event> seasonEvents = this.eventsBySeason.get(this.currentSeason);
        int indexRandomEvent = random.nextInt(seasonEvents.size());
        currentEvent = seasonEvents.get(indexRandomEvent);
    }
}
