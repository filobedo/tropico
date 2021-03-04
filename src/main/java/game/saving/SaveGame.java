package game.saving;

import republic.Republic;
import republic.economy.Resources;

public abstract class SaveGame {

    public abstract void saveGame(Republic republic, String playerName);

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

}
