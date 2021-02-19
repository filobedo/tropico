package ressources.scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GamePlay {
    protected final String name;
    protected final String story;
    protected Map<Season, List<Event>> eventsBySeason = new HashMap<>();
    protected Event currentEvent;
    protected int year = 0;
    protected Season firstSeason;
    protected Season currentSeason;

    public GamePlay(String name, String story, Season firstSeason) {
        this.name = name;
        this.story = story;
        this.firstSeason = firstSeason;
        this.currentSeason = firstSeason;
        setSeasonsInEventsBySeason();
    }

    public String getName() {
        return this.name;
    }

    public String getStory() {
        return this.story;
    }

    public Event getCurrentEvent() {
        return this.currentEvent;
    }

    public int getYear() {
        return this.year;
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

    abstract public boolean canPlayEvents();

    abstract public void nextEvent();

    abstract public void placeRelatedEvents(List<Event> relatedEvents);

    public void nextYear() {
        this.year += 1;
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

}
