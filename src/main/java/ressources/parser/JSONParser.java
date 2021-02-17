package ressources.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import ressources.economy.Treasury;
import ressources.factions.Faction;
import ressources.factions.Population;
import ressources.game.GameDifficulty;
import ressources.scenario.*;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JSONParser implements IParser{
    private JSONObject gameParameterFile;
    private String gameDifficulty; // TODO pas besoin ?

    public void openFile(String filePath) throws NullPointerException {
        File file = new File(filePath);
        try (InputStream is = new FileInputStream(file)) {
            JSONTokener token = new JSONTokener(is);
            this.gameParameterFile = new JSONObject(token);
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + filePath);
        }
    }

    public boolean canParseFile() {
        if(this.gameParameterFile.has("name")) {
            if(this.gameParameterFile.has("story")) {
                if(this.gameParameterFile.has("gameStartParameters")) {
                    if(this.gameParameterFile.has("scenario")) {
                        JSONObject scenario = this.gameParameterFile.getJSONObject("scenario");
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

    public boolean isGameStartParameterDifficultyInJson(GameDifficulty chosenGameDifficulty) {
        if(this.gameParameterFile.getJSONObject("gameStartParameters").has(chosenGameDifficulty.name())) {
            this.gameDifficulty = chosenGameDifficulty.name();
            return true;
        }
        else if (this.gameParameterFile.getJSONObject("gameStartParameters").has(GameDifficulty.NORMAL.name())) {
            System.out.printf("%nLa difficulté \"%s\" n'existe pas dans ce scénario, c'est-à-dire que les ressources de base (population, agriculture, argent...) sont de difficulté %s.", chosenGameDifficulty.toString(), GameDifficulty.NORMAL.toString());
            System.out.printf("%nCependant, la difficulté des évènements est appliqué selon votre choix, c'est-à-dire que si un évènement en mode normal diminue la population de 10%%,%nalors avec la difficulté que vous avez choisi, la population diminuera de %d%%.%n", (int)(10 * chosenGameDifficulty.getDifficultyCoefficient()));
            this.gameDifficulty = GameDifficulty.NORMAL.name();
            return true;
        }
        System.out.printf("%nLes difficultés %s et %s n'existent pas dans ce scénario.%n", chosenGameDifficulty.toString(), GameDifficulty.NORMAL.toString());
        return false;
    }

    public Population parsePopulation() throws ConfigurationException {
        Population population = new Population();
        if(canParsePopulation(population)) {
            JSONObject factions = this.gameParameterFile.getJSONObject("gameStartParameters").getJSONObject("NORMAL").getJSONObject("factions");
            for(Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
                String factionName = factionsSet.getKey();
                int satisfactionRate = factions.getJSONObject(factionName.toUpperCase()).getInt("satisfactionRate");
                int nbSupporters = factions.getJSONObject(factionName.toUpperCase()).getInt("nbSupporters");
                Faction currentFaction = population.createAndGetFaction(factionName, nbSupporters, satisfactionRate);
                factionsSet.setValue(currentFaction);
            }
            population.factionsSubscribeToBribeEventExceptLoyalists();
            return population;
        }
        throw new ConfigurationException("Missing JSON key to set faction values");
    }

    public boolean canParsePopulation(Population population) {
        JSONObject gameStartParameters = gameParameterFile.getJSONObject("gameStartParameters");
        if(gameStartParameters.has("NORMAL")) {
            JSONObject normal = gameStartParameters.getJSONObject("NORMAL");
            if(normal.has("factions")) {
                JSONObject factions = normal.getJSONObject("factions");
                if(areFactionsInfoInJson(population.getFactionByName().keySet(), factions)) {
                    return true;
                }
            }
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
        if(faction.has("name")) {
            if(faction.has("satisfactionRate")) {
                if(faction.has("nbSupporters")) {
                    return true;
                }
            }
        }
        return false;
    }


    public Treasury parseResources() throws ConfigurationException {
        JSONObject gameStartParameters = this.gameParameterFile.getJSONObject("gameStartParameters").getJSONObject(this.gameDifficulty);
        if(canParseRepublicResources(gameStartParameters)) {
            int farmRate = gameStartParameters.getInt("farmRate");
            int foodUnits = gameStartParameters.getInt("foodUnits");
            int treasury = gameStartParameters.getInt("treasury");
            int industryRate = gameStartParameters.getInt("industryRate");
            try {
                return new Treasury(foodUnits, treasury, farmRate, industryRate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        throw new ConfigurationException("Missing JSON key(s) to set republic resources values (agriculture, industry...)");
    }

    public boolean canParseRepublicResources(JSONObject gameStartParameters) {
        if(gameStartParameters.has("farmRate")) {
            if(gameStartParameters.has("industryRate")) {
                if(gameStartParameters.has("treasury")) {
                    if(gameStartParameters.has("foodUnits")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Scenario parseScenario() throws ConfigurationException {
        JSONObject scenarioToParse = this.gameParameterFile.getJSONObject("scenario");

        if(scenarioToParse.length() == 0) {
            throw new ConfigurationException("Missing events.");
        }
        else {
            String name = gameParameterFile.getString("name");
            String story = gameParameterFile.getString("story");
            Season firstSeason = getFirstSeason();
            Scenario scenario = new Scenario(name, story, firstSeason);
            for(Season season : Season.values()) {
                JSONArray seasonToParse = scenarioToParse.getJSONArray(season.name());
                try {
                    scenario.addEventsToSeason(season, parseEvents(seasonToParse));
                } catch (Exception ex) {
                    throw ex;
                }
            }
            return scenario;
        }
    }

    public List<Event> parseEvents(JSONArray seasonToParse) throws ConfigurationException {
        List<Event> scenarioEvents = new ArrayList<>();
        for(int seasonCount = 0; seasonCount < seasonToParse.length(); seasonCount += 1 ) {
//            JSONObject season = seasonToParse.getJSONObject(seasonCount);
//            JSONArray events = season.getJSONArray("events");
            JSONObject event = seasonToParse.getJSONObject(0);

            try {
                Event currentEvent = parseEvent(event);
                scenarioEvents.add(currentEvent);
            }
            catch (Exception ex) {
                throw ex;
            }
        }

        return scenarioEvents;
    }

    public Event parseEvent(JSONObject event) throws ConfigurationException {
        JSONArray choices = event.getJSONArray("choices");

        String name = event.getString("name");
        String description = event.getString("description");
        Event currentEvent = new Event(name, description);
        if(hasIrreversibleEffects(event)) {
            currentEvent.setIrreversibleEffects(parseEffects(event.getJSONObject("irreversible")));
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
        return event.has("irreversible");
    }

    public List<Choice> parseChoices(JSONArray choices) throws ConfigurationException {
        List<Choice> eventChoices = new ArrayList<>();
        // We can't have 0 choices nor more than 4, we can have 1 to 4 choices
        if(choices.length() < 1 || choices.length() > 4) {
            throw new ConfigurationException("There isn't enough choice, or too many choices");
        }
        for(int indexChoice = 0; indexChoice < choices.length(); indexChoice += 1 ) {
            JSONObject choice = choices.getJSONObject(indexChoice);

            String name = choice.getString("name");
            String description = choice.getString("description");
            Choice currentChoice = new Choice(name, description);
            currentChoice.setEffects(parseEffects(choice.getJSONObject("effects")));
            if(choice.has("relatedEvents")) {
                currentChoice.setRelatedEvent(parseEvent(choice.getJSONObject("relatedEvents")));
            }
            eventChoices.add(currentChoice);
        }

        return eventChoices;
    }

    public Effect parseEffects(JSONObject effects) {
        // TODO IF ???
        Map<String, Map<String, Integer>> factionEffects = new HashMap<>();
        if(effects.has("factions")) {
            factionEffects = parseFactionEffects(effects.getJSONArray("factions"));
        }
        Map<String, Integer> factorEffects = parseFactorEffects(effects);
        return new Effect(factionEffects, factorEffects);
    }

    public Map<String, Map<String, Integer>> parseFactionEffects(JSONArray factionEffects) {
        Map<String, Map<String, Integer>> effectsByFaction = new HashMap<>();
        for(int indexFactionEffect = 0; indexFactionEffect < factionEffects.length(); indexFactionEffect += 1 ) {
            JSONObject faction = factionEffects.getJSONObject(indexFactionEffect);
            Map<String, Integer> effectOnFaction = new HashMap<>();
            if(faction.has("satisfactionRate")) {
                int satisfactionRateEffect = faction.getInt("satisfactionRate");
                effectOnFaction.put("satisfactionRate", satisfactionRateEffect);
            }
            if(faction.has("nbSupporters")) {
                int satisfactionRateEffect = faction.getInt("nbSupporters");
                effectOnFaction.put("nbSupporters", satisfactionRateEffect);
            }
            String factionName = faction.getString("name");
            effectsByFaction.put(factionName, effectOnFaction);
        }
        return effectsByFaction;
    }

    public Map<String, Integer> parseFactorEffects(JSONObject effects) {
        Map<String, Integer> effectByFactor = new HashMap<>();
        if(effects.has("industryRate")) {
            int industryEffect = effects.getInt("industryRate");
            effectByFactor.put("industryRate", industryEffect);
        }
        if(effects.has("farmRate")) {
            int agricultureEffect = effects.getInt("farmRate");
            effectByFactor.put("farmRate", agricultureEffect);
        }
        if(effects.has("foodUnits")) {
            int foodUnitsEffect = effects.getInt("foodUnits");
            effectByFactor.put("foodUnits", foodUnitsEffect);
        }
        if(effects.has("treasury")) {
            int treasuryEffect = effects.getInt("treasury");
            effectByFactor.put("treasury", treasuryEffect);
        }
        if(effects.has("population")) {
            int populationEffect = effects.getInt("population");
            effectByFactor.put("population", populationEffect);
        }
        if(effects.has("satisfactionRate")) {
            int satisfactionRateEffect = effects.getInt("satisfactionRate");
            effectByFactor.put("satisfactionRate", satisfactionRateEffect);
        }

        return effectByFactor;
    }

    public Season getFirstSeason() {
        if(this.gameParameterFile.has("firstSeason")) {
            return Season.valueOf(this.gameParameterFile.getString("firstSeason").toUpperCase());
        }
        return Season.getRandom();
    }
}
