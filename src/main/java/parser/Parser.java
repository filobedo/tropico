package parser;

import exceptions.MissingEventsException;
import exceptions.MissingParsingKeysException;
import exceptions.MissingParsingObjectException;
import org.json.JSONObject;
import game.GameParameters;
import republic.economy.Resources;
import republic.factions.Population;
import gameplay.GamePlay;

public abstract class Parser {
    protected JSONObject gameData;
    protected String gameStartParameterDifficulty;
    protected double difficultyCoefficient;
    protected GameParameters gameParametersChosen;

    public abstract void openFile(String filePath) throws NullPointerException;
    public abstract void setGameParametersChosen(GameParameters gameParameters);
    public abstract boolean canParseFile();
    public abstract boolean isGameStartParameterDifficultyInJson();
    public abstract GamePlay parseScenario() throws MissingEventsException, ClassNotFoundException, MissingParsingObjectException;
    public abstract Population parsePopulation() throws MissingParsingKeysException;
    public abstract Resources parseResources() throws MissingParsingKeysException;
}
