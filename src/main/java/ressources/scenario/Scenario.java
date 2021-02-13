package ressources.scenario;

import ressources.parser.IParser;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String name;
    private String story;
    private Season firstSeason;
    private List<Event> events = new ArrayList<>();

    public Scenario(String name, String story, Season firstSeason) {
        this.name = name;
        this.story = story;
        this.firstSeason = firstSeason;
    }

    public String getName() {
        return name;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
