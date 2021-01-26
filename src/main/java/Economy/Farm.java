package Economy;

public class Farm implements Development {
    int marker = 0;
    int ressource = 0; // Food

    protected int getRessource() {
        return ressource;
    }

    protected void setRessource(int ressource) {
        this.ressource = ressource;
    }

    public int getMarker() {
        return marker;
    }

    protected void setMarker(int marker) {
        this.marker = marker;
    }
}
