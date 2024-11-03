package utils;

import fileio.CardInput;

import java.util.ArrayList;

public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;

    private ArrayList<String> colors;
    private String name;

    public Card() {}
    public Card(CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }
    public Card(Card card) {
        this.mana = card.getMana();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>(card.getColors());
        this.name = new String(card.getName());
    }

    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {this.health = health;}

    public String toString() {
        return "CardInput{"
                +  "mana=" + mana
                +  ", description='" + description
                + '\'' + ", colors=" + colors
                + ", name='" + name + '\''
                + '}';
    }
}
