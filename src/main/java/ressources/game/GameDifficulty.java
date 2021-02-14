package ressources.game;

public enum GameDifficulty { // TODO Gestion de la difficulté -> dans la gestion des impacts
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

   /* public String capitalize() {
        String difficultyMode = this.toString();
        return difficultyMode.substring(0, 1).toUpperCase() + difficultyMode.substring(1).toLowerCase();
    }*/
}
