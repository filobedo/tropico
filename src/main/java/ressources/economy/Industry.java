package ressources.economy;

public class Industry implements Development {
    int rate = 0;
    int ressource = 0; // money

    Industry() {}
    Industry (int startIndustryRessource, int startRate) {
        this.rate = startRate;
        this.ressource = startIndustryRessource;
    }

    protected int getRessource() {
        return ressource;
    }

    protected void setRessource(int ressource) {
        this.ressource = ressource;
    }

    public int getRate() {
        return this.rate;
    }

    public void setRate(int rate) {
        this.rate = this.rate + rate;
    }
}
