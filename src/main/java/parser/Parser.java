package parser;

import exceptions.MissingEventsException;
import exceptions.MissingParsingKeysException;
import exceptions.MissingParsingObjectException;
import gameplay.*;
import game.GameParameters;
import republic.economy.Resources;
import republic.factions.Population;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Parser {

    protected String gameStartParameterDifficulty;
    protected double difficultyCoefficient;
    protected GameParameters gameParametersChosen;

    public void setGameParametersChosen(GameParameters gameParametersChosen) {
        this.gameParametersChosen = gameParametersChosen;
    }

    public abstract void openFile(String filePath) throws NullPointerException;
    public abstract boolean canParseFile();
    protected abstract boolean hasAllSeasons(Object gamePlay);
    public abstract boolean isGameStartParameterDifficultyInJson();

    public abstract Population parsePopulation() throws MissingParsingKeysException;
    protected abstract boolean canParsePopulation(Object gameStartParams, Population population);
    protected abstract boolean areFactionsInfoInJson(Set<String> factionNames, Object factions);
    protected abstract boolean isFactionInfoInJson(Object faction);

    public abstract Resources parseResources() throws MissingParsingKeysException;
    public abstract boolean canParseRepublicResources(Object gameStartParams);

    public abstract GamePlay parseGamePlay() throws MissingEventsException, ClassNotFoundException, MissingParsingObjectException;
    protected abstract GamePlay getGamePlay(String name, String story);
    protected abstract Season getFirstSeason();

    protected abstract List<Event> parseSeason(Object seasonToParse) throws MissingParsingObjectException;
    protected abstract Event parseEvent(Object eventToParse) throws MissingParsingObjectException;
    protected abstract boolean hasIrreversibleEffects(Object event);

    protected abstract List<Choice> parseChoices(Object choicesToParse) throws MissingParsingObjectException;
    protected abstract Effect parseEffects(Object effectsToParse);

    protected abstract Map<String, Map<String, Integer>> parseFactionEffects(Object factionEffectsToParse);
    protected abstract Map<String, Integer> parseFactorEffects(Object effectsToParse);

}
