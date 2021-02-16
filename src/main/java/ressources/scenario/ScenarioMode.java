package ressources.scenario;

public class ScenarioMode extends Scenario implements IGameMode {
    public ScenarioMode(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    @Override
    public void nextEvent() {
        // Get next event ?? See with teacher
    }
}
