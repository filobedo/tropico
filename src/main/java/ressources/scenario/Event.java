package ressources.scenario;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String name;
    private String description;
    private List<Choice> choices = new ArrayList<>();
    private Effect irreversibleEffects;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
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
        System.out.printf("%nEntrez votre choix :%n");
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
