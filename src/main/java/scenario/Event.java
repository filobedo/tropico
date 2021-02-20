package scenario;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private final String name;
    private final String description;
    private List<Choice> choices = new ArrayList<>();
    private Effect irreversibleEffects;
    private boolean isARelatedEvent = false;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void setIsARelatedEvent() {
        this.isARelatedEvent = true;
    }

    public boolean isARelatedEvent() {
        return isARelatedEvent;
    }

    public void setIrreversibleEffects(Effect irreversibleEffects) {
        this.irreversibleEffects = irreversibleEffects;
    }

    public void display(int nbEvent) {
        System.out.printf("%nÉvénement %d : %s%n", nbEvent, this.name);
        if(!this.description.equals("")) {
            System.out.printf("Description : %s%n", this.description);
        }
        if(hasIrreversibleEffects()) {
            System.out.printf("%nImpact(s) direct(s) :%n");
            this.irreversibleEffects.displayFactionEffects();
            this.irreversibleEffects.displayFactorEffects();
        }
        System.out.printf("%nChoix possible(s) :%n");
        displayChoices();
        System.out.println("Entrez votre choix :");
    }

    public boolean hasIrreversibleEffects() {
        return irreversibleEffects != null;
    }

    public void displayChoices() {
        int nbChoix = 1;
        for(Choice choice : choices) {
            System.out.printf("%n%d. ", nbChoix);
            choice.display();
            nbChoix += 1;
        }
    }

    public Effect getIrreversibleEffects() {
        return irreversibleEffects;
    }

    public int getNbChoices() {
        return this.choices.size();
    }

    public Choice getChoiceByPlayerChoice(int playerChoice) {
        return choices.get(playerChoice - 1);
    }
}
