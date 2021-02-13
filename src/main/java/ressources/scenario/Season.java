package ressources.scenario;

import java.util.Random;

public enum Season { // TODO Gestion de la difficulté -> dans la gestion des impacts
    WINTER {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    SPRING{
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    SUMMER{
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    AUTUMN{
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    };

    public String capitalize() {
        String season = this.name();
        return season.substring(0, 1).toUpperCase() + season.substring(1).toLowerCase();
     }

    public static Season getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
