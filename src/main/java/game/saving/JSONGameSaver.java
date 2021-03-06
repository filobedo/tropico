package game.saving;

import game.Game;
import org.json.JSONObject;
import parser.ParsingKeys;
import republic.Republic;
import republic.factions.Faction;
import republic.factions.Population;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JSONGameSaver extends GameSaver {

    public JSONGameSaver(Game gameToSave) {
        super(gameToSave);
    }

    @Override
    public void saveGame() {
        // Save year and season
        JSONObject save  = new JSONObject();
        save.put(ParsingKeys.year, getYear());
        save.put(ParsingKeys.eventCount, getEventCount());
        save.put(ParsingKeys.currentSeason, getCurrentSeasonName());
        save.put(ParsingKeys.score, getScore());

        // Save game start parameters
        JSONObject gameStartParameters = getGameStartParameters();
        save.put(ParsingKeys.gameStartParameters, gameStartParameters);

        File saveFile = new File(this.gameToSave.getSavePath());
        saveFile.getParentFile().mkdirs();

        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.write(save.toString());
            fileWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject getGameStartParameters() {
        Republic republic = this.gameToSave.getRepublic();
        JSONObject savedGameStartParameters = new JSONObject();
        JSONObject backUp = new JSONObject();

        // Save resources
        backUp.put(ParsingKeys.farmRate, getFarmRate(republic.getResources()));
        backUp.put(ParsingKeys.foodUnits, getFoodUnits(republic.getResources()));
        backUp.put(ParsingKeys.industryRate, getIndustryRate(republic.getResources()));
        backUp.put(ParsingKeys.money, getMoney(republic.getResources()));

        // Save factions
        JSONObject factions = getPopulation(republic.getPopulation());
        backUp.put(ParsingKeys.factions, factions);

        savedGameStartParameters.put(this.gameToSave.getGameDifficulty().name(), backUp);

        return savedGameStartParameters;
    }

    public JSONObject getPopulation(Population population) {
        JSONObject factions = new JSONObject();

        for (Map.Entry<String, Faction> factionsSet : population.getFactionByName().entrySet()) {
            JSONObject faction = new JSONObject();
            String currentFactionName = factionsSet.getKey().toUpperCase();
            Faction currentFaction = factionsSet.getValue();

            faction.put(ParsingKeys.satisfactionRate, currentFaction.getSatisfactionRate());
            faction.put(ParsingKeys.nbSupporters, currentFaction.getNbSupporters());

            factions.put(currentFactionName, faction);
        }

        return factions;
    }

}
