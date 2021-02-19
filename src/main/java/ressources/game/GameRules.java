package ressources.game;

public class GameRules {
    public static final int MINIMUM_GLOBAL_SATISFACTION_RATE = 25;

    public static final int MIN_CHOICE_PER_EVENT = 1;
    public static final int MAX_CHOICE_PER_EVENT = 4;

    public static final int NEEDED_FOOD_PER_CITIZEN = 4;
    public static final int FOOD_PRICE = 8;

    public static final int GENERATED_MONEY_BY_INDUSTRY_RATE = 10;
    public static final int GENERATED_FOOD_BY_FARM_RATE = 40;

    public static final int BRIBE_FACTION_DECREASE_LOYALISTS_SATISFACTION = -10;
    public static final int BRIBE_INCREASE_SATISFACTION_RATE = 10;
    public static final int BRIBE_PRICE_PER_SUPPORTER = 15;

    public static final int NB_YEAR_END_OPTIONS = 3;
    public static final int YEAR_END_DO_NOTHING_CHOICE = 1;
    public static final int YEAR_END_BRIBE_CHOICE = 2;
    public static final int YEAR_END_BUY_FOOD_CHOICE = 3;


    public static final int INITIAL_SCORE = 10;
    public static final int SCORE_POINTS_PER_SATISFACTION_WON = 2;
    public static final int SCORE_POINTS_PER_SATISFACTION_LOST= -1;
    public static final int END_SCORE_POINTS_PER_YEAR = 10;
    public static final int END_SCORE_POINTS_PER_CITIZEN = 2;
    public static final int END_SCORE_POINTS_PER_INDUSTRY_RATE = 5;
    public static final int END_SCORE_POINTS_PER_DOLLAR_POSITIVE = 3;
    public static final int END_SCORE_POINTS_PER_DOLLAR_NEGATIVE = -2;
    public static final int END_SCORE_POINTS_PER_FARM_RATE = 5;
    public static final int END_SCORE_POINTS_PER_FOOD_UNITS = 1;

    public static final int END_GAME_CHOICE_START_NEW_GAME= 1;
    public static final int END_GAME_CHOICE_STOP_PLAYING= 2;
}
