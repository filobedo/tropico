package ressources.economy;

public class Industry implements Development {
    int marker = 0;
    int ressource = 0; // Food

    protected int getRessource() {
        return ressource;
    }

    protected void setRessource(int ressource) {
        this.ressource = ressource;
    }

    public int getMarker() {
        return this.marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }
}
