package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private final String name;
    private final String story;
    private List<Event> events = new ArrayList<>();
    private Event currentEvent;
    private Season currentSeason;

    public Scenario(String name, String story, Season currentSeason) {
        this.name = name;
        this.story = story;
        this.currentSeason = currentSeason;
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
