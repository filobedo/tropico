package ressources.factions;

public class Faction {
    private String name;
    private int nbSupporters;
    private int satisfactionRate;

    public Faction(String name, int nbSupporters, int satisfactionRate) {
        // TODO : conditions ???? satisfactionRate & nbSupporters cannot be negative
        this.name = name;
        this.nbSupporters = nbSupporters;
        this.satisfactionRate = satisfactionRate;
    }

    public int getNbSupporters() {
        return this.nbSupporters;
    }

    public int getSatisfactionRate() {
        return this.satisfactionRate;
    }

    public void setNbSupporters(int nbSupporters) {
        this.nbSupporters = nbSupporters;
    }

    public void setSatisfactionRate(int newRate) {
        if(this.satisfactionRate == 0) {
            System.out.println(String.format("%s faction is against this republic (satisfaction rate 0%), it is no longer possible to increase their satisfaction rate.", this.name));
        }
        else {
            if(newRate <= 0) {
                this.satisfactionRate = 0;
            }
            else if(newRate >= 100) {
                this.satisfactionRate = 100;
            }
            else {
                this.satisfactionRate = newRate;
            }
        }

    }
    
    public void winsSupporters(int percentage) {
        setNbSupporters(this.nbSupporters * 1 + percentage/100);
    }

    public void losesSupporters(int percentage) {
        setSatisfactionRate(this.nbSupporters * 1 - percentage/100);
    }

    public void increaseSatisfactionBy(int percentagePoint) {
        setSatisfactionRate(this.nbSupporters + percentagePoint);
    }

    public void decreaseSatisfactionBy(int percentagePoint) {
        setSatisfactionRate(this.nbSupporters - percentagePoint);
    }

    public int getBribePrice() {
        return getNbSupporters() * 15; // TODO '15' à mettre dans une constante qque part ?
    }

    public void bribe() {
        increaseSatisfactionBy(10); // TODO '10' pareil que getBribePrice()
    }

    // TODO : Ou faire la diminution de l'approbation des loyalistes ? lorsque bribe()
    // Donc utiliser les observers

}
