package ressources.scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scenario {
    private final String name;
    private final String story;
//    private List<Event> events = new ArrayList<>();
    private Map<Season, List<Event>> eventsBySeason = new HashMap<>();
    private Event currentEvent;
    private Season currentSeason;

    public Scenario(String name, String story, Season currentSeason) {
        this.name = name;
        this.story = story;
        this.currentSeason = currentSeason;
        setSeasonsInEventsBySeason();
    }

    public String getName() {
        return this.name;
    }

    public String getStory() {
        return this.story;
    }

    public Season getCurrentSeason() {
        return this.currentSeason;
    }

    public void setSeasonsInEventsBySeason() {
        for(Season season : Season.values()) {
            this.eventsBySeason.put(season, null);
        }
    }

    public void addEventsToSeason(Season season, List<Event> events) {
        this.eventsBySeason.put(season, events);
    }

    public void nextEvent(int year) {
        this.currentEvent = this.eventsBySeason.get(Season.SPRING).get(year);
    }

    public void nextSeason() {
        this.currentSeason = Season.getNextSeason(this.currentSeason);
    }
    public boolean isScenarioFinished() {
        return this.currentEvent == null;
    }

    public void displayCurrentEvent(int nbEvent) {
        this.currentEvent.display(nbEvent);
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }
}
