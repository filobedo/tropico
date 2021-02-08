package ressources.factions;

import ressources.publisher.EventManager;

public abstract class Faction {
    private int nbSupporters;
    private int satisfactionRate;
    public EventManager events;

    public Faction(int nbSupporters, int satisfactionRate) throws IllegalArgumentException {
        if(nbSupporters < 0) {
            throw new IllegalArgumentException("Number of supporters can't be a negative value!");
        }
        if(satisfactionRate < 0 || satisfactionRate > 100) {
            throw new IllegalArgumentException("The satisfaction rate must be between 0 and 100!");
        }
        this.nbSupporters = nbSupporters;
        this.satisfactionRate = satisfactionRate;
        if(this.getClass() != Loyalists.class) {
            this.events = new EventManager("bribe");
        }
        else {
            this.events = null;
        }
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
        if(nbSupporters >= 0) {
            this.nbSupporters = nbSupporters;
        }
        else {
            this.nbSupporters = 0;
        }
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

    public void updateNbSupportersBy(int percentage) {
        int newNbSupporters = (int)(this.nbSupporters * (1 + (double)percentage/100));
        setNbSupporters(newNbSupporters);
    }

    public void updateSatisfactionRate(int percentagePoint) {
        setSatisfactionRate(this.satisfactionRate + percentagePoint);
    }

    public void eliminateASupporter() {
        setNbSupporters(getNbSupporters() - 1);
    }

    public int getBribePrice() {
        if(this.getClass() != Loyalists.class) {
            return getNbSupporters() * 15; // TODO '15' à mettre dans une constante qque part ? + Changer selon la difficulté
        }
        else { // TODO Les println, faut-il faire des exceptions à la place ou les garder printer pour le joueur
            System.out.println("Since Loyalists cannot be bribed, they don't have a bribe price");
            return 0;
        }
    }

    public void bribe() { // TODO Diminuer la trésorerie par getBribePrice() dans une autre classe
        updateSatisfactionRate(10); // TODO '10' pareil que le TODO de getBribePrice()
        events.notify("bribe", this);
        // TODO System.out.println("You don't have enough money to bribe " + getName() + " faction!");
    }

    @Override
    public String toString() {
        return String.format("Faction: %s%nSupporters: %s%nSatisfaction: %s%n", getName(), getNbSupporters(), getSatisfactionRate());
    }
}
