package listeners;

import game.Game;
import game.needs.GameRules;

public class SatisfactionIncreasedListener implements EventListener {
    private final Game game;

    public SatisfactionIncreasedListener(Game game) {
        this.game = game;
    }

    /**
     * Happens when a faction satisfaction rate increases
     * Only during an event (event irreversible impacts or player choice impacts)
     * @param eventName name of the event, 'satisfaction_increased' here
     * @param variation how much the faction satisfaction rate increased by
     */
    @Override
    public void update(String eventName, Object variation) {
        int satisfactionVariation = Math.abs((int)variation);
        this.game.addScore(satisfactionVariation * GameRules.SCORE_POINTS_PER_SATISFACTION_WON);
    }
}