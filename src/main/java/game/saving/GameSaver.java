package game.saving;

import game.Game;
import republic.economy.Resources;

public abstract class GameSaver {

    protected Game gameToSave;

    public GameSaver(Game gameToSave) {
        this.gameToSave = gameToSave;
    }

    public abstract void saveGame();

    public int getYear() {
        return this.gameToSave.getYear();
    }

    public int getEventCount() {
        return this.gameToSave.getEventCount() + 1;
    }

    public double getScore() {
        return this.gameToSave.getScore();
    }

    public String getCurrentSeasonName() {
        return this.gameToSave.getCurrentSeason().name();
    }

    public int getFarmRate(Resources resources) {
        return resources.getFarmRate();
    }

    public int getFoodUnits(Resources resources) {
        return resources.getFoodUnits();
    }

    public int getIndustryRate(Resources resources) {
        return resources.getIndustryRate();
    }

    public double getMoney(Resources resources) {
        return resources.getMoney();
    }

    public abstract void deleteFile(String path);
}
