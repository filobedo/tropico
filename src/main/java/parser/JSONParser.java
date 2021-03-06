package parser;

import exceptions.MissingEventsException;
import exceptions.MissingParsingKeysException;
import exceptions.MissingParsingObjectException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import game.needs.GameRules;
import republic.economy.Resources;
import republic.factions.Faction;
import republic.factions.Population;
import game.needs.GameDifficulty;
import gameplay.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JSONParser extends Parser {
    protected JSONObject gameData;

    @Override
    public void openFile(String filePath) throws NullPointerException {
        File file = new File(filePath);
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JSONTokener token = new JSONTokener(reader);
            this.gameData = new JSONObject(token);
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + filePath);
        }
    }

    @Override
    public boolean canParseFile() {
        if(this.gameData.has(ParsingKeys.name)) {
            if(this.gameData.has(ParsingKeys.story)) {
                if(this.gameData.has(ParsingKeys.gameStartParameters)) {
                    if(this.gameData.has(ParsingKeys.gameplay)) {
                        JSONObject gamePlay = this.gameData.getJSONObject(ParsingKeys.gameplay);
                        return gamePlay.length() == 4 && hasAllSeasons(gamePlay);
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean hasAllSeasons(Object gp) {
        JSONObject gamePlay = (JSONObject) gp;
        for(Season season : Season.values()) {
            if(!gamePlay.has(season.name())) {
               return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGameStartParameterDifficultyInJson() {
        this.difficultyCoefficient = this.gameParametersChosen.getGameDifficulty().getDifficultyCoefficient();
        GameDifficulty chosenGameDifficulty = this.gameParametersChosen.getGameDifficulty();
        if(this.gameData.getJSONObject(ParsingKeys.gameStartParameters).has(chosenGameDifficulty.name())) {
            this.gameStartParameterDifficulty = chosenGameDifficulty.name();
            return true;
        }
        else if(this.gameData.getJSONObject(ParsingKeys.gameStartParameters).has(GameDifficulty.NORMAL.name())) {
            System.out.printf("%nLa difficulté \"%s\" n'existe pas dans ce scénario, c'est-à-dire que les ressources de base (population, agriculture, argent...) sont de difficulté %s.", chosenGameDifficulty.toString(), GameDifficulty.NORMAL.toString());
            System.out.printf("%nCependant, la difficulté des évènements est appliqué selon votre choix, c'est-à-dire que si un évènement en mode normal diminue la population de 10%%,%nalors avec la difficulté que vous avez choisi, la population diminuera de %d%%.%n", (int)(10 * chosenGameDifficulty.getDifficultyCoefficient()));
            this.gameStartParameterDifficulty = GameDifficulty.NORMAL.name();
            return true;
        }
        System.out.printf("%nLes difficultés %s et %s n'existent pas dans ce scénario.%n", chosenGameDifficulty.toString(), GameDifficulty.NORMAL.toString());
        return false;
    }

    @Override
    public boolean doesChosenDifficultyHasSavedGame(File file, GameDifficulty gameDifficulty) {
        JSONObject savedGame;
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JSONTokener token = new JSONTokener(reader);
            savedGame = new JSONObject(token);
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + file.getPath());
        }

        if(savedGame.getJSONObject(ParsingKeys.gameStartParameters).has(gameDifficulty.name())) {
            this.gameStartParameterDifficulty = gameDifficulty.name();
            return true;
        }
        return false;
    }

    @Override
    public int getSavedYear() {
        if(gameData.has(ParsingKeys.year)) {
            return gameData.getInt(ParsingKeys.year);
        }
        return 0;
    }

    @Override
    public String getSavedCurrentSeason() {
        if(gameData.has(ParsingKeys.currentSeason)) {
            return gameData.getString(ParsingKeys.currentSeason);
        }
        return "";
    }

    @Override
    public int getSavedEventCount() {
        if(gameData.has(ParsingKeys.eventCount)) {
            return gameData.getInt(ParsingKeys.eventCount);
        }
        return 0;
    }

    @Override
    public double getSavedScore() {
        if(gameData.has(ParsingKeys.score)) {
            return gameData.getDouble(ParsingKeys.score);
        }
        return 0;
    }

    @Override
    public Population parsePopulation() throws MissingParsingKeysException {
        Population population = new Population();
        JSONObject gameStartParameters = this.gameData.getJSONObject(ParsingKeys.gameStartParameters).getJSONObject(this.gameStartParameterDifficulty);
        if(canParsePopulation(gameStartParameters, population)) {
            JSONObject factions = gameStartParameters.getJSONObject(ParsingKeys.factions);
            for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
                String factionName = factionsSet.getKey();
                int satisfactionRate = factions.getJSONObject(factionName.toUpperCase()).getInt(ParsingKeys.satisfactionRate);
                int nbSupporters = factions.getJSONObject(factionName.toUpperCase()).getInt(ParsingKeys.nbSupporters);
                Faction currentFaction = population.createAndGetFaction(factionName, nbSupporters, satisfactionRate);
                factionsSet.setValue(currentFaction);
            }
            population.factionsSubscribeToBribeEventExceptLoyalists();
            return population;
        }
        throw new MissingParsingKeysException("Missing JSON key to set faction values");
    }

    @Override
    protected boolean canParsePopulation(Object gameStartParams, Population population) {
        JSONObject gameStartParameters = (JSONObject) gameStartParams;
        if(gameStartParameters.has(ParsingKeys.factions)) {
            JSONObject factions = gameStartParameters.getJSONObject(ParsingKeys.factions);
            return areFactionsInfoInJson(population.getFactionByName().keySet(), factions);
        }
        return false;
    }

    @Override
    protected boolean areFactionsInfoInJson(Set<String> factionNames, Object factionsInfo) {
        JSONObject factions = (JSONObject) factionsInfo;
        for(String factionName : factionNames) {
            String upperFactionName = factionName.toUpperCase();
            if(factions.has(upperFactionName)) {
                JSONObject faction = factions.getJSONObject(upperFactionName);
                if(isFactionInfoInJson(faction)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    protected boolean isFactionInfoInJson(Object currentFaction) {
        JSONObject faction = (JSONObject) currentFaction;
        if(faction.has(ParsingKeys.satisfactionRate)) {
            return faction.has(ParsingKeys.nbSupporters);
        }
        return false;
    }

    @Override
    public Resources parseResources() throws MissingParsingKeysException {
        JSONObject gameStartParameters = this.gameData.getJSONObject(ParsingKeys.gameStartParameters).getJSONObject(this.gameStartParameterDifficulty);
        if(canParseRepublicResources(gameStartParameters)) {
            int farmRate = gameStartParameters.getInt(ParsingKeys.farmRate);
            int foodUnits = gameStartParameters.getInt(ParsingKeys.foodUnits);
            double money = gameStartParameters.getDouble(ParsingKeys.money);
            int industryRate = gameStartParameters.getInt(ParsingKeys.industryRate);
            try {
                return new Resources(foodUnits, money, farmRate, industryRate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        throw new MissingParsingKeysException("Missing JSON key(s) to set republic resources values (agriculture, industry...)");
    }

    @Override
    public boolean canParseRepublicResources(Object gameStartParams) {
        JSONObject gameStartParameters = (JSONObject) gameStartParams;
        if(gameStartParameters.has(ParsingKeys.farmRate)) {
            if(gameStartParameters.has(ParsingKeys.industryRate)) {
                if(gameStartParameters.has(ParsingKeys.money)) {
                    return gameStartParameters.has(ParsingKeys.foodUnits);
                }
            }
        }
        return false;
    }

    @Override
    public GamePlay parseGamePlay() throws MissingEventsException, MissingParsingObjectException, ClassNotFoundException {
        JSONObject gamePlayToParse = this.gameData.getJSONObject(ParsingKeys.gameplay);

        if(gamePlayToParse.length() == 0) {
            throw new MissingEventsException("Missing events.");
        }
        else {
            String name = gameData.getString(ParsingKeys.name);
            String story = gameData.getString(ParsingKeys.story);
            GamePlay gamePlay = getGamePlay(name, story);
            if(gamePlay == null) { throw new ClassNotFoundException("This game mode doesn't exist"); }
            for(Season season : Season.values()) {
                JSONArray seasonToParse = gamePlayToParse.getJSONArray(season.name());
                gamePlay.addEventsToSeason(season, parseSeason(seasonToParse));
            }
            return gamePlay;
        }
    }

    @Override
    protected GamePlay getGamePlay(String name, String story) {
        if(this.gameParametersChosen.isGameModeScenario()) {
            return new ScenarioGamePlay(name, story, getFirstSeason());
        }
        else if(this.gameParametersChosen.isGameModeSandbox()) {
            return new SandboxGamePlay(name, story, getFirstSeason());
        }
        return null;
    }

    @Override
    protected Season getFirstSeason() {
        if(this.gameData.has(ParsingKeys.firstSeason)) {
            return Season.valueOf(this.gameData.getString(ParsingKeys.firstSeason).toUpperCase());
        }
        return null;
    }

    @Override
    protected List<Event> parseSeason(Object seasonToParse) throws MissingParsingObjectException {
        JSONArray season = (JSONArray) seasonToParse;
        List<Event> seasonEvents = new ArrayList<>();
        for(int eventCount = 0; eventCount < season.length(); eventCount += 1 ) {
            JSONObject event = season.getJSONObject(eventCount);
            Event currentEvent = parseEvent(event);
            seasonEvents.add(currentEvent);
        }

        return seasonEvents;
    }

    @Override
    protected Event parseEvent(Object eventToParse) throws MissingParsingObjectException {
        JSONObject event = (JSONObject) eventToParse;
        String name = event.getString(ParsingKeys.name);
        String description = "";
        if(event.has(ParsingKeys.description)) {
            description = event.getString(ParsingKeys.description);
        }
        Event currentEvent = new Event(name, description);
        if(hasIrreversibleEffects(event)) {
            currentEvent.setIrreversibleEffects(parseEffects(event.getJSONObject(ParsingKeys.irreversible)));
        }
        JSONArray choices = event.getJSONArray(ParsingKeys.choices);
        List<Choice> eventChoices = parseChoices(choices);

        currentEvent.setChoices(eventChoices);
        return currentEvent;
    }

    @Override
    protected boolean hasIrreversibleEffects(Object event) {
        return ((JSONObject)event).has(ParsingKeys.irreversible);
    }

    @Override
    protected List<Choice> parseChoices(Object choicesToParse) throws MissingParsingObjectException {
        JSONArray choices = (JSONArray) choicesToParse;
        List<Choice> eventChoices = new ArrayList<>();
        if(choices.length() < GameRules.MIN_CHOICE_PER_EVENT || choices.length() > GameRules.MAX_CHOICE_PER_EVENT) {
            throw new MissingParsingObjectException("There isn't enough choice, or too many choices");
        }
        for(int indexChoice = 0; indexChoice < choices.length(); indexChoice += 1 ) {
            JSONObject choice = choices.getJSONObject(indexChoice);

            String name = choice.getString(ParsingKeys.name);
            String description = choice.getString(ParsingKeys.description);
            Choice currentChoice = new Choice(name, description);
            currentChoice.setEffects(parseEffects(choice.getJSONObject(ParsingKeys.effects)));

            if(choice.has(ParsingKeys.relatedEvents)) {
                currentChoice.setRelatedEvent(parseSeason(choice.getJSONArray(ParsingKeys.relatedEvents)));
            }
            eventChoices.add(currentChoice);
        }

        return eventChoices;
    }

    @Override
    protected Effect parseEffects(Object effectsToParse) {
        JSONObject effects = (JSONObject) effectsToParse;
        Map<String, Map<String, Integer>> factionEffects = new HashMap<>();
        if(effects.has(ParsingKeys.factions)) {
            factionEffects = parseFactionEffects(effects.getJSONArray(ParsingKeys.factions));
        }
        Map<String, Integer> factorEffects = parseFactorEffects(effects);
        return new Effect(factionEffects, factorEffects);
    }

    @Override
    protected Map<String, Map<String, Integer>> parseFactionEffects(Object factionEffectsToParse) {
        JSONArray factionEffects = (JSONArray) factionEffectsToParse;
        Map<String, Map<String, Integer>> effectsByFaction = new HashMap<>();
        for(int indexFactionEffect = 0; indexFactionEffect < factionEffects.length(); indexFactionEffect += 1 ) {
            JSONObject faction = factionEffects.getJSONObject(indexFactionEffect);

            Map<String, Integer> effectOnFaction = new HashMap<>();
            if(faction.has(ParsingKeys.satisfactionRate)) {
                int satisfactionRateEffect = (int)Math.round(faction.getInt(ParsingKeys.satisfactionRate) * this.difficultyCoefficient);
                effectOnFaction.put(ParsingKeys.satisfactionRate, satisfactionRateEffect);
            }
            if(faction.has(ParsingKeys.nbSupporters)) {
                int satisfactionRateEffect = (int)Math.round(faction.getInt(ParsingKeys.nbSupporters) * this.difficultyCoefficient);
                effectOnFaction.put(ParsingKeys.nbSupporters, satisfactionRateEffect);
            }
            String factionName = faction.getString(ParsingKeys.name);
            effectsByFaction.put(factionName, effectOnFaction);
        }
        return effectsByFaction;
    }

    @Override
    protected Map<String, Integer> parseFactorEffects(Object effectsToParse) {
        JSONObject effects = (JSONObject) effectsToParse;
        Map<String, Integer> effectByFactor = new HashMap<>();
        if(effects.has(ParsingKeys.industryRate)) {
            int industryEffect = (int)Math.round(effects.getInt(ParsingKeys.industryRate) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.industryRate, industryEffect);
        }
        if(effects.has(ParsingKeys.farmRate)) {
            int agricultureEffect = (int)Math.round(effects.getInt(ParsingKeys.farmRate) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.farmRate, agricultureEffect);
        }
        if(effects.has(ParsingKeys.foodUnits)) {
            int foodUnitsEffect = (int)Math.round(effects.getInt(ParsingKeys.foodUnits) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.foodUnits, foodUnitsEffect);
        }
        if(effects.has(ParsingKeys.money)) {
            int moneyEffect = (int)Math.round(effects.getInt(ParsingKeys.money) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.money, moneyEffect);
        }
        if(effects.has(ParsingKeys.population)) {
            int populationEffect = (int)Math.round(effects.getInt(ParsingKeys.population) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.population, populationEffect);
        }
        if(effects.has(ParsingKeys.satisfactionRate)) {
            int satisfactionRateEffect = (int)Math.round(effects.getInt(ParsingKeys.satisfactionRate) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.satisfactionRate, satisfactionRateEffect);
        }
        return effectByFactor;
    }
}
