package game.needs;

public enum GameDifficulty {
    EASY(0.5) {
        @Override
        public String toString() {
            return "facile";
        }
    },
    NORMAL(1){
        @Override
        public String toString() {
            return "normal";
        }
    },
    HARD(2){
        @Override
        public String toString() {
            return "dur";
        }
    };

    private final double difficultyCoefficient;

    GameDifficulty(double difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public double getDifficultyCoefficient() {
        return difficultyCoefficient;
    }
}
