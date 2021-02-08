package ressources.event;

import org.json.*;

import java.io.*;


//TODO : lecture

public class Event {
    JSONObject currentEvent;
    JSONArray scenario;


    public Event(String fileName){

        JSONObject jsonParser = new JSONObject();
        File file = new File(fileName);
        try (InputStream is = new FileInputStream(file)) {
            JSONTokener token = new JSONTokener(is);
            JSONObject game = new JSONObject(token);

            this.scenario = game.getJSONArray("scenario");
            this.currentEvent = this.scenario.getJSONObject(0).getJSONArray("events").getJSONObject(0);
        } catch (IOException e){
            throw new NullPointerException("Cannot find resource file " + fileName);
        }
    }

    public void setCurrentEvent(int year, int season){
        if (season < this.scenario.length()){
            if (this.scenario.getJSONObject(season).has("events")){
                if (year < this.scenario.getJSONObject(season).getJSONArray("events").length()){
                    this.currentEvent = this.scenario.getJSONObject(season).getJSONArray("events").getJSONObject(year);
                } else {
                    System.out.println("Error in setCurrEvent method : invalid year value : " + year + "\n");
                }
            } else {
                System.out.println("Error in setCurrEvent method : no events in season : " + season + "\n");
            }
        } else {
            System.out.println("Error in setCurrEvent method : invalid season value : " + season + "\n");
        }
    }

    public String getCurrentEvent(){
        return String.format(this.getEventName() + "\n" + this.getEventDescription() + "\n" + getCurrentEventIrreverssible());
    }

    private String getCurrentEventIrreverssible() {
        if (this.currentEvent.has("irreversible")){
            return String.format("Impact direct :\n%s",  this.getImpact(this.currentEvent, "irreversible"));
        }
        return "";
    }

    public String getCurrentEventChoices(){
        String choices = "";
        int i = 0;
        JSONArray choicesArray = this.currentEvent.getJSONArray("choices") ;
        JSONObject choice;
        while (i < choicesArray.length()){
            choice = choicesArray.getJSONObject(i);
            choices += String.format("--- Choix %d ---\n", (i + 1));
            choices += String.format("%s\n%s\n", choice.getString("name"),  choice.getString("description"));
            choices += "Effet :\n";
            choices += getImpact(choice, "impact");
            choices += ("\n\n");
            i += 1;
        }
        return choices;
    }

    private String getImpact(JSONObject choice, String key){
        String res = "";
        JSONObject impactJSON = choice.getJSONObject(key);
        if (impactJSON.has("money")){
            res += String.format("\tArgent : %d\n", impactJSON.getInt("money"));
        }
        if (impactJSON.has("farm")){
            res += String.format("\tAgriculture : %d\n", impactJSON.getInt("farm"));
        }
        if (impactJSON.has("industry")){
            res += String.format("\tIndustrie : %d\n", impactJSON.getInt("industry"));
        }
        if (impactJSON.has("population")){
            res += String.format("\tPopulation : %d\n", impactJSON.getInt("population"));
        }
        if (impactJSON.has("factions")){
            res += getFactionsImpact(impactJSON.getJSONArray("factions"));
        }
        return res;
    }

    private String getFactionsImpact(JSONArray factions) {
        String factionImpact = "";
        int i = 0;
        JSONObject faction;
        while (i < factions.length()){
            faction = factions.getJSONObject(i);
            factionImpact += String.format("\t" + faction.getString("name") + " :\n");
            if (faction.has("supporters")){
                factionImpact += String.format("\t\tPartisans : " + faction.getInt("supporters") + "\n");
            }
            if (faction.has("satisfaction")){
                factionImpact += String.format("\t\tTaux de satisfaction : " + faction.getInt("satisfaction") + "\n");
            }
            i += 1;
        }
        return factionImpact;
    }

    public String getEventDescription() {
        return this.currentEvent.getString("description");
    }

    public String getSeason(int season){
        return this.scenario.getJSONObject(season).getString("season");
    }

    public String getEventName(){
        return this.currentEvent.getString("name");
    }
//
//    public void loadEvents(String season) {
//        ////Construct
//        JSONObject jsonParser = new JSONObject();
//        String resourceName = "src/main/java/ressources/premier_scenario.json";
//        File f = new File(resourceName);
//        try (InputStream is = new FileInputStream(f)) {
//            JSONTokener token = new JSONTokener(is);
//            JSONObject game = new JSONObject(token);
//            //Construct
//            this.scenario = game.getJSONArray("scenario");
//            //Set curr event (saison, numEvent)
//            JSONObject events = scenario.getJSONObject("evenements");
//            JSONArray summer = events.getJSONArray(season);
//            JSONObject testEvent = summer.getJSONObject(20);
//            System.out.println(testEvent.get("name"));
//
//            //Autre
//            display();
//            choice = getPlayerChoice();
//            updateGameStats(choice, impacts);
//        } catch (IOException e) {
//            throw new NullPointerException("Cannot find resource file " + resourceName);
//        }
//    }
//
//    private int getPlayerChoice() {
//        return 0;
//    }

    public void displayCurrentEvent() {
        System.out.println(this.getCurrentEvent() + this.getCurrentEventChoices());
    }

    void updateGameStats(JSONObject impact) {
        //TODO
        if (impact.get("money") != null) {

        }
    }
}