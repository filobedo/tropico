package ressources.listeners;

import ressources.game.Game;
import ressources.game.GameRules;

public class SatisfactionDecreasedListener implements EventListener {
    private final Game game;

    public SatisfactionDecreasedListener(Game game) {
        this.game = game;
    }

    @Override
    public void update(String eventName, Object variation) {
        int satisfactionVariation = (int)variation;
        this.game.addScore(satisfactionVariation * GameRules.SCORE_POINTS_PER_SATISFACTION_LOST);
    }
}