package utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    private boolean frozen, hasAttacked, isTank;
    private int type;

    public Card() {}
    public Card(CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
        frozen = false;
        hasAttacked = false;
        setType();
        switch (name) {
            case "Goliath", "Warden" -> isTank = true;
            default -> isTank = false;
        }
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
    public void addHealth(int health) {this.health += health;}
    public void subtractHealth(int health) {this.health -= health;}
    public void subtractAttackDamage(int attackDamage) {
        this.attackDamage -= attackDamage;
        if(this.attackDamage < 0)
            this.attackDamage = 0;
    }
    public void addAttackDamage(int attackDamage) {
        this.attackDamage += attackDamage;
    }

    public void setFrozen(boolean frozen) {this.frozen = frozen;}
    public boolean getFrozen() {return frozen;}
    public void setHasAttacked(boolean hasAttacked) {this.hasAttacked = hasAttacked;}
    public boolean getHasAttacked() {return hasAttacked;}
    public boolean getIsTank() {return isTank;}

    public void swapHealth(Card card) {
        int aux = health;
        health = card.getHealth();
        card.setHealth(aux);
    }
    public void swapHealthAndAttackDamage() {
        int aux = health;
        health = attackDamage;
        attackDamage = aux;
    }

    public int getType() {
        return type;
    }

    public void setType() {
        final String[] backRowMinions = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
        if(Arrays.asList(backRowMinions).contains(name))
            type = 0;
        else
            type = 1;
    }

    public int attack(Card attacked, boolean opponentHasTanks) {
        if(hasAttacked)
            return 2;
        if(frozen)
            return 3;
        if(opponentHasTanks && !attacked.getIsTank())
            return 4;

        hasAttacked = true;
        attacked.subtractHealth(attackDamage);
        return 0;
    }

    public int action(Card attacked, boolean samePlayer, boolean opponentHasTanks) {
        if(frozen)
            return 1;
        if(hasAttacked)
            return 2;
        if(Objects.equals(name, "Disciple")) {
            if(!samePlayer)
                return 3;
            attacked.addHealth(2);
            return 0;
        }
        if(samePlayer)
            return 4;

       if(opponentHasTanks && !attacked.getIsTank())
           return 5;

        switch(name) {
            case "The Ripper" -> attacked.subtractAttackDamage(2);
            case "Miraj" -> swapHealth(attacked);
            case "The Cursed One" -> attacked.swapHealthAndAttackDamage();
        }

        hasAttacked = true;
        return 0;
    }

    public int attackHero(Card opponentHero, boolean opponentHasTanks) {
        if(frozen)
            return 1;
        if(hasAttacked)
            return 2;
        if(opponentHasTanks)
            return 3;

        hasAttacked = true;
        opponentHero.subtractHealth(attackDamage);
        return 0;
    }

    public String toString() {
        return "CardInput{"
                +  "mana=" + mana
                +  ", description='" + description
                + '\'' + ", colors=" + colors
                + ", name='" + name + '\''
                + '}';
    }

    public ObjectNode toObjectNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("mana", mana);
        node.put("attackDamage", attackDamage);
        node.put("health", health);
        node.put("description", description);
        node.putPOJO("colors", colors);
        node.put("name", name);
        return node;
    }
}
