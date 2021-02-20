package listeners;

import game.Game;
import game.GameRules;

public class SatisfactionDecreasedListener implements EventListener {
    private final Game game;

    public SatisfactionDecreasedListener(Game game) {
        this.game = game;
    }

    /**
     * Happens when a faction satisfaction rate decreases
     * Only during an event (event irreversible impacts or player choice impacts)
     * @param eventName name of the event, 'satisfaction_decreased' here
     * @param variation how much the faction satisfaction rate decreased by
     */
    @Override
    public void update(String eventName, Object variation) {
        int satisfactionVariation = Math.abs((int)variation);
        this.game.addScore(satisfactionVariation * GameRules.SCORE_POINTS_PER_SATISFACTION_LOST);
    }
}