package ressources.economy;

public class Farm implements Development {
    int rate = 0;
    int ressource = 0; // Food

    Farm() {}
    Farm (int startFarmRessource, int startRate) {
        this.rate = startRate;
        this.ressource = startFarmRessource;
    }

    protected int getRessource() {
        return ressource;
    }

    protected void setRessource(int ressource) {
        this.ressource = ressource;
    }

    public int getRate() {
        return rate;
    }

    protected void setRate(int rate) {
        this.rate = rate;
    }
}
