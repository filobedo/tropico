package ressources.factions;

public class Faction {
    private int nbSupporters;
    private int approvalPercent;

    public Faction(int nbSupporters, int approvalPercent) {
        this.nbSupporters = nbSupporters;
        this.approvalPercent = approvalPercent;
    }

    public void decreaseNbSupportersBy(int percent) {
        this.nbSupporters *= 1 - percent/100;
    }

    public void increaseNbSupportersBy(int percent) {
        this.nbSupporters *= 1 + percent/100;
    }
}
