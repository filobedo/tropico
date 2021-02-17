package ressources.parser;

import ressources.republic.economy.Resources;
import ressources.republic.factions.Population;
import ressources.game.GameDifficulty;
import ressources.scenario.Scenario;

import javax.naming.ConfigurationException;

public interface IParser {
    void openFile(String filePath) throws NullPointerException;
    boolean canParseFile();
    boolean isGameStartParameterDifficultyInJson(GameDifficulty gameDifficulty);
    Scenario parseScenario() throws ConfigurationException;
    Population parsePopulation() throws ConfigurationException;
    Resources parseResources() throws ConfigurationException;
}
