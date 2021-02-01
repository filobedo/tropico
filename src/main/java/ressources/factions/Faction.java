package ressources.factions;

import ressources.publisher.EventManager;

public abstract class Faction {
    private int nbSupporters;
    private int satisfactionRate;
    public EventManager events;

    public Faction(int nbSupporters, int satisfactionRate) {
        // TODO : conditions ???? satisfactionRate & nbSupporters cannot be negative
        this.nbSupporters = nbSupporters;
        this.satisfactionRate = satisfactionRate;
        this.events = new EventManager("bribe");
    }

    public String getName() {
        return this.getClass().getSimpleName();
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
            System.out.printf("%s faction is against this republic (unsatisfied), it is no longer possible to increase their satisfaction rate.%n", getName());
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
        if(percentage > 0) {
            setNbSupporters(this.nbSupporters * (1 + percentage/100));
        }
    }

    public void losesSupporters(int percentage) {
        if(percentage > 0) {
            setSatisfactionRate(this.nbSupporters * (1 - percentage / 100));
        }
    }

    public void increaseSatisfactionBy(int percentagePoint) {
        if(percentagePoint > 0) {
            setSatisfactionRate(this.satisfactionRate + percentagePoint);
        }
    }

    public void decreaseSatisfactionBy(int percentagePoint) {
        if(percentagePoint > 0) {
            setSatisfactionRate(this.satisfactionRate - percentagePoint);
        }
    }

    public int getBribePrice() {
        return getNbSupporters() * 15; // TODO '15' à mettre dans une constante qque part ?
    }

    public void bribe() { // TODO Diminuer la trésorerie par getBribePrice()
        increaseSatisfactionBy(10); // TODO '10' pareil que le TODO de getBribePrice()
        events.notify("bribe", this);
        // TODO System.out.println("You don't have enough money to bribe " + getName() + " faction!");
    }

    @Override
    public String toString() {
        return String.format("Faction: %s%nSupporters: %s%nSatisfaction: %s%n", getName(), getNbSupporters(), getSatisfactionRate());
    }
}
