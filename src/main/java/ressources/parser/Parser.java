package ressources.parser;

import org.json.JSONObject;
import ressources.game.GameParameters;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Population;
import ressources.game.GameDifficulty;
import ressources.scenario.GamePlay;

import javax.naming.ConfigurationException;

public abstract class Parser {
    protected JSONObject gameData;
    protected String gameStartParameterDifficulty;
    protected double difficultyCoefficient;
    protected GameParameters gameParametersChosen;

    public abstract void openFile(String filePath) throws NullPointerException;
    public abstract void setGameParametersChosen(GameParameters gameParameters);
    public abstract boolean canParseFile();
    public abstract boolean isGameStartParameterDifficultyInJson();
    public abstract GamePlay parseScenario() throws ConfigurationException, ClassNotFoundException;
    public abstract Population parsePopulation() throws ConfigurationException;
    public abstract Resources parseResources() throws ConfigurationException;
}
