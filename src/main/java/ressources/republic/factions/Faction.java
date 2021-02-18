package ressources.republic.factions;

import ressources.game.GameRules;
import ressources.publisher.EventManager;

public abstract class Faction {
    private int nbSupporters;
    private int satisfactionRate;
    public EventManager events;

    protected Faction(int nbSupporters, int satisfactionRate) throws IllegalArgumentException {
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

    abstract public String getName();

    public int getNbSupporters() {
        return this.nbSupporters;
    }

    public int getSatisfactionRate() {
        return this.satisfactionRate;
    }

    private void setNbSupporters(int nbSupporters) {
        if(nbSupporters >= 0) {
            this.nbSupporters = nbSupporters;
        }
        else {
            this.nbSupporters = 0;
        }
    }

    private void setSatisfactionRate(int newRate) {
        if(this.satisfactionRate == 0) {
            if(newRate > 0) {
                System.out.printf("Les %s sont contre votre République (insatisfaits), il n'est plus possible de changer leur taux de satisfaction.%n", getName());
            }
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

    protected void addSupporters(int nbSupporters) {
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
        if(this.satisfactionRate > 0) {
            return !this.getClass().getSimpleName().equals(Loyalists.class.getSimpleName());
        }
        return false;
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
        String factionSummary = String.format("\t%s : %n", getName());
        factionSummary += String.format("\t\tPartisans : %d \t| Satisfaction : %d%%%n", getNbSupporters(), getSatisfactionRate());
        return factionSummary;
    }
}
