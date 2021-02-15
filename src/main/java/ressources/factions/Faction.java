package ressources.factions;

import ressources.game.GameRules;
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

    public void addSupporters(int nbSupporters) {
        setNbSupporters(this.nbSupporters + nbSupporters);
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

    public boolean canBeBribed() {
        return !this.getClass().getSimpleName().equals(Loyalists.class.getSimpleName());
    }

    public int getBribePrice() {
        return getNbSupporters() * GameRules.BRIBE_PRICE_BY_SUPPORTER;
    }

    public void bribe() {
        updateSatisfactionRate(GameRules.BRIBE_SATISFACTION_RATE);
        events.notify("bribe", this);
    }

    @Override
    public String toString() {
        return String.format("Faction: %s%nSupporters: %s%nSatisfaction: %s%n", getName(), getNbSupporters(), getSatisfactionRate());
    }
}
