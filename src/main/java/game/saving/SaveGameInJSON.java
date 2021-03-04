package game.saving;

import org.json.JSONObject;
import parser.ParsingKeys;
import republic.Republic;
import republic.factions.Faction;
import republic.factions.Population;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class SaveGameInJSON extends SaveGame {

    public SaveGameInJSON() {
    }

    @Override
    public void saveGame(Republic republic, String playerName) {
        JSONObject gameStartParameters = getGameStartParameters(republic);
        URL url = this.getClass().getClassLoader().getResource("");
        String path = Objects.requireNonNull(url).getPath() + "/sandbox/saves/player_" + playerName + ".json";

        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(gameStartParameters.toString());
            fileWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public JSONObject getGameStartParameters(Republic republic) {
        JSONObject gameStartParameters = new JSONObject();
        JSONObject backUp = new JSONObject();
        JSONObject savedGameStartParameters = new JSONObject();

        // Add resources
        backUp.put(ParsingKeys.farmRate, getFarmRate(republic.getResources()));
        backUp.put(ParsingKeys.foodUnits, getFoodUnits(republic.getResources()));
        backUp.put(ParsingKeys.industryRate, getIndustryRate(republic.getResources()));
        backUp.put(ParsingKeys.money, getMoney(republic.getResources()));

        // Add factions
        JSONObject factions = getPopulation(republic.getPopulation());
        backUp.put(ParsingKeys.factions, factions);

        savedGameStartParameters.put(ParsingKeys.saved, backUp);
        gameStartParameters.put(ParsingKeys.gameStartParameters, savedGameStartParameters);

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
