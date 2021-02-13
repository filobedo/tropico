package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private final String name;
    private final String story;
    private final Season firstSeason;
    private List<Event> events = new ArrayList<>();
    private Event currentEvent;

    public Scenario(String name, String story, Season firstSeason) {
        this.name = name;
        this.story = story;
        this.firstSeason = firstSeason;
    }

    public String getName() {
        return name;
    }

    public String getStory() {
        return story;
    }

    public Season getFirstSeason() {
        return firstSeason;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void nextEvent(int index) {
        if(index != events.size()) {
            this.currentEvent = this.events.get(index);
        }
        else {
            this.currentEvent = null;
        }
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
