package ressources.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import ressources.game.GameParameters;
import ressources.game.GameRules;
import ressources.republic.economy.Resources;
import ressources.republic.factions.Faction;
import ressources.republic.factions.Population;
import ressources.game.GameDifficulty;
import ressources.scenario.*;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JSONParser extends Parser {

    public void openFile(String filePath) throws NullPointerException {
        File file = new File(filePath);
        try (InputStream is = new FileInputStream(file)) {
            JSONTokener token = new JSONTokener(is);
            this.gameData = new JSONObject(token);
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + filePath);
        }
    }

    public void setGameParametersChosen(GameParameters gameParametersChosen) {
        this.gameParametersChosen = gameParametersChosen;
    }

    public boolean canParseFile() {
        if(this.gameData.has(ParsingKeys.name)) {
            if(this.gameData.has(ParsingKeys.story)) {
                if(this.gameData.has(ParsingKeys.gameStartParameters)) {
                    if(this.gameData.has(ParsingKeys.scenario)) {
                        JSONObject scenario = this.gameData.getJSONObject(ParsingKeys.scenario);
                        return scenario.length() == 4 && hasAllSeasons(scenario);
                    }
                }
            }
        }
        return false;
    }

    public boolean hasAllSeasons(JSONObject scenario) {
        for(Season season : Season.values()) {
            if(!scenario.has(season.name())) {
               return false;
            }
        }
        return true;
    }

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

    public Population parsePopulation() throws ConfigurationException {
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
        throw new ConfigurationException("Missing JSON key to set faction values");
    }

    public boolean canParsePopulation(JSONObject gameStartParameters, Population population) {
        if(gameStartParameters.has(ParsingKeys.factions)) {
            JSONObject factions = gameStartParameters.getJSONObject(ParsingKeys.factions);
            return areFactionsInfoInJson(population.getFactionByName().keySet(), factions);
        }
        return false;
    }

    private boolean areFactionsInfoInJson(Set<String> factionNames, JSONObject factions) {
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

    private boolean isFactionInfoInJson(JSONObject faction) {
        if(faction.has(ParsingKeys.name)) {
            if(faction.has(ParsingKeys.satisfactionRate)) {
                return faction.has(ParsingKeys.nbSupporters);
            }
        }
        return false;
    }


    public Resources parseResources() throws ConfigurationException {
        JSONObject gameStartParameters = this.gameData.getJSONObject(ParsingKeys.gameStartParameters).getJSONObject(this.gameStartParameterDifficulty);
        if(canParseRepublicResources(gameStartParameters)) {
            int farmRate = gameStartParameters.getInt(ParsingKeys.farmRate);
            int foodUnits = gameStartParameters.getInt(ParsingKeys.foodUnits);
            int treasury = gameStartParameters.getInt(ParsingKeys.money);
            int industryRate = gameStartParameters.getInt(ParsingKeys.industryRate);
            try {
                return new Resources(foodUnits, treasury, farmRate, industryRate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        throw new ConfigurationException("Missing JSON key(s) to set republic resources values (agriculture, industry...)");
    }

    public boolean canParseRepublicResources(JSONObject gameStartParameters) {
        if(gameStartParameters.has(ParsingKeys.farmRate)) {
            if(gameStartParameters.has(ParsingKeys.industryRate)) {
                if(gameStartParameters.has(ParsingKeys.money)) {
                    return gameStartParameters.has(ParsingKeys.foodUnits);
                }
            }
        }
        return false;
    }

    public GamePlay parseScenario() throws ConfigurationException, ClassNotFoundException {
        JSONObject scenarioToParse = this.gameData.getJSONObject(ParsingKeys.scenario);

        if(scenarioToParse.length() == 0) {
            throw new ConfigurationException("Missing events.");
        }
        else {
            String name = gameData.getString(ParsingKeys.name);
            String story = gameData.getString(ParsingKeys.story);
            GamePlay gamePlay = getGamePlay(name, story);
            if(gamePlay == null) { throw new ClassNotFoundException("This game mode doesn't exist"); }
            for(Season season : Season.values()) {
                JSONArray seasonToParse = scenarioToParse.getJSONArray(season.name());
                try {
                    gamePlay.addEventsToSeason(season, parseSeason(seasonToParse));
                } catch (Exception ex) {
                    throw ex;
                }
            }
            return gamePlay;
        }
    }

    public GamePlay getGamePlay(String name, String story) {
        if(this.gameParametersChosen.isGameModeScenario()) {
            return new Scenario(name, story, getFirstSeason());
        }
        else if(this.gameParametersChosen.isGameModeSandbox()) {
            return new Sandbox(name, story, getFirstSeason());
        }
        return null;
    }

    public List<Event> parseSeason(JSONArray seasonToParse) throws ConfigurationException {
        List<Event> seasonEvents = new ArrayList<>();
        for(int eventCount = 0; eventCount < seasonToParse.length(); eventCount += 1 ) {
            JSONObject event = seasonToParse.getJSONObject(eventCount);
            try {
                Event currentEvent = parseEvent(event);
                seasonEvents.add(currentEvent);
            }
            catch (Exception ex) {
                throw ex;
            }
        }

        return seasonEvents;
    }

    public Event parseEvent(JSONObject event) throws ConfigurationException {
        JSONArray choices = event.getJSONArray(ParsingKeys.choices);

        String name = event.getString(ParsingKeys.name);
        String description = event.getString(ParsingKeys.description);
        Event currentEvent = new Event(name, description);
        if(hasIrreversibleEffects(event)) {
            currentEvent.setIrreversibleEffects(parseEffects(event.getJSONObject(ParsingKeys.irreversible)));
        }
        try {
            List<Choice> eventChoices = parseChoices(choices);
            currentEvent.setChoices(eventChoices);
        } catch (Exception ex) {
            throw ex;
        }
        return currentEvent;
    }

    public boolean hasIrreversibleEffects(JSONObject event) {
        return event.has(ParsingKeys.irreversible);
    }

    public List<Choice> parseChoices(JSONArray choices) throws ConfigurationException {
        List<Choice> eventChoices = new ArrayList<>();
        if(choices.length() < GameRules.MIN_CHOICE_PER_EVENT || choices.length() > GameRules.MAX_CHOICE_PER_EVENT) {
            throw new ConfigurationException("There isn't enough choice, or too many choices");
        }
        for(int indexChoice = 0; indexChoice < choices.length(); indexChoice += 1 ) {
            JSONObject choice = choices.getJSONObject(indexChoice);

            String name = choice.getString(ParsingKeys.name);
            String description = choice.getString(ParsingKeys.description);
            Choice currentChoice = new Choice(name, description);
            currentChoice.setEffects(parseEffects(choice.getJSONObject(ParsingKeys.effects)));

            if(choice.has(ParsingKeys.relatedEvents)) {
                currentChoice.setRelatedEvent(parseEvent(choice.getJSONObject(ParsingKeys.relatedEvents)));
            }
            eventChoices.add(currentChoice);
        }

        return eventChoices;
    }

    public Effect parseEffects(JSONObject effects) {
        Map<String, Map<String, Integer>> factionEffects = new HashMap<>();
        if(effects.has(ParsingKeys.factions)) {
            factionEffects = parseFactionEffects(effects.getJSONArray(ParsingKeys.factions));
        }
        Map<String, Integer> factorEffects = parseFactorEffects(effects);
        return new Effect(factionEffects, factorEffects);
    }

    public Map<String, Map<String, Integer>> parseFactionEffects(JSONArray factionEffects) {
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

    public Map<String, Integer> parseFactorEffects(JSONObject effects) {
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
            int treasuryEffect = (int)Math.round(effects.getInt(ParsingKeys.money) * this.difficultyCoefficient);
            effectByFactor.put(ParsingKeys.money, treasuryEffect);
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

    public Season getFirstSeason() {
        if(this.gameData.has(ParsingKeys.firstSeason)) {
            return Season.valueOf(this.gameData.getString(ParsingKeys.firstSeason).toUpperCase());
        }
        return Season.getRandom();
    }
}
