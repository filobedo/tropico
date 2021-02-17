package ressources.parser;

import ressources.game.GameParameters;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Population;
import ressources.game.GameDifficulty;
import ressources.scenario.GamePlay;

import javax.naming.ConfigurationException;

public interface IParser {
    void openFile(String filePath) throws NullPointerException;
    void setGameParametersChosen(GameParameters gameParameters);
    boolean canParseFile();
    boolean isGameStartParameterDifficultyInJson();
    GamePlay parseScenario() throws ConfigurationException, ClassNotFoundException;
    Population parsePopulation() throws ConfigurationException;
    Resources parseResources() throws ConfigurationException;
}
