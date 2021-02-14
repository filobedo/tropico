package ressources.scenario;

import java.util.Random;

public enum Season { // TODO Gestion de la difficulté -> dans la gestion des impacts
    WINTER {
        @Override
        public String toString() {
            return "hiver";
        }
    },
    SPRING{
        @Override
        public String toString() {
            return "printemps";
        }
    },
    SUMMER{
        @Override
        public String toString() {
            return "été";
        }
    },
    AUTUMN{
        @Override
        public String toString() {
            return "automne";
        }
    };

    public String capitalize() {
        String season = this.toString();
        return season.substring(0, 1).toUpperCase() + season.substring(1).toLowerCase();
     }

    public static Season getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
