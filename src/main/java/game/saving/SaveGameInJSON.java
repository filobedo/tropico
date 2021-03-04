package game.saving;

import org.json.JSONObject;
import parser.ParsingKeys;
import republic.Republic;
import republic.factions.Faction;
import republic.factions.Population;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public abstract class SaveGameInJSON extends SaveGame{

    @Override
    public void saveGame(Republic republic, String playerName) {
        JSONObject gameStartParameters = getGameStartParameters(republic);
        String path = this.getClass().getClassLoader().getResource("/saves/playerGameSave_" + playerName + ".json").getPath();

        try(FileWriter fileWriter = new FileWriter(path)){
            fileWriter.write(gameStartParameters.toString());
            fileWriter.flush();

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject getGameStartParameters(Republic republic) {
        JSONObject gameStartParameters = new JSONObject();
        JSONObject savedGameStartParameters = new JSONObject();

        // Add resources
        savedGameStartParameters.put(ParsingKeys.farmRate, getFarmRate(republic.getResources()));
        savedGameStartParameters.put(ParsingKeys.foodUnits, getFoodUnits(republic.getResources()));
        savedGameStartParameters.put(ParsingKeys.industryRate, getIndustryRate(republic.getResources()));
        savedGameStartParameters.put(ParsingKeys.money, getMoney(republic.getResources()));

        // Add factions
        JSONObject factions = getPopulation(republic.getPopulation());
        savedGameStartParameters.put(ParsingKeys.factions, factions);

        gameStartParameters.put(ParsingKeys.gameStartParameters, ParsingKeys.savedGameStartParameters);

        return gameStartParameters;
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
