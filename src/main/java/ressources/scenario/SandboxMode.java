package ressources.scenario;

public class SandboxMode extends Scenario implements IGameMode {
    public SandboxMode(String name, String story, Season currentSeason) {
        super(name, story, currentSeason);
    }

    @Override
    public void nextEvent() {
        // Get next random event according to the season
    }
}
