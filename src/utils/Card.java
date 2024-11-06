package utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Card {
    private final int mana, type;
    private int attackDamage, health;
    private final String description, name;
    private final ArrayList<String> colors;
    private boolean frozen, hasAttacked;
    private final boolean isTank;

    public Card(final CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
        frozen = false;
        hasAttacked = false;
        type = setType();
        isTank = setIsTank();
    }

    public final int getMana() {
        return mana;
    }

    public final String getDescription() {
        return description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    /** */
    public final void addHealth(final int healthAdded) {
        health += healthAdded;
    }

    /** */
    public final void subtractHealth(final int healthSubtracted) {
        health -= healthSubtracted;
    }

    /** */
    public final void subtractAttackDamage(final int attackDamageSubtracted) {
        attackDamage -= attackDamageSubtracted;
        if (attackDamage < 0) {
            attackDamage = 0;
        }
    }

    /** */
    public final void addAttackDamage(final int attackDamageAdded) {
        attackDamage += attackDamageAdded;
    }

    public final void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public final boolean getFrozen() {
        return frozen;
    }

    public final void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public final boolean getHasAttacked() {
        return hasAttacked;
    }

    /** */
    public final boolean setIsTank() {
        return (Objects.equals(name, "Goliath") || Objects.equals(name, "Warden"));
    }

    public final boolean getIsTank() {
        return isTank;
    }

    public final int getType() {
        return type;
    }

    /** */
    public final void swapHealth(final Card card) {
        int aux = health;
        health = card.getHealth();
        card.setHealth(aux);
    }

    /** */
    public final void swapHealthAndAttackDamage() {
        int aux = health;
        health = attackDamage;
        attackDamage = aux;
    }

    /** */
    private int setType() {
        final String[] backRowMinions = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
        if (Arrays.asList(backRowMinions).contains(name)) {
            return 0;
        } else {
            return 1;
        }
    }

    /** */
    public final int attack(final Card attacked, final boolean opponentHasTanks) {
        if (hasAttacked) {
            return Constants.CARD_HAS_ATTACKED;
        }
        if (frozen) {
            return Constants.CARD_FROZEN;
        }
        if (opponentHasTanks && !attacked.getIsTank()) {
            return Constants.NOT_TANK;
        }

        hasAttacked = true;
        attacked.subtractHealth(attackDamage);
        return 0;
    }

    /** */
    public final int action(final Card attacked, final boolean samePlayer,
                            final boolean opponentHasTanks) {
        if (frozen) {
            return Constants.CARD_FROZEN;
        }
        if (hasAttacked) {
            return Constants.CARD_HAS_ATTACKED;
        }
        if (Objects.equals(name, "Disciple")) {
            if (!samePlayer) {
                return Constants.CARD_NOT_PLAYER;
            }
            attacked.addHealth(2);
            return 0;
        }
        if (samePlayer) {
            return Constants.CARD_NOT_OPPONENT;
        }

        if (opponentHasTanks && !attacked.getIsTank()) {
            return Constants.NOT_TANK;
        }

        switch (name) {
            case "The Ripper" -> attacked.subtractAttackDamage(2);
            case "Miraj" -> swapHealth(attacked);
            case "The Cursed One" -> attacked.swapHealthAndAttackDamage();
            default -> System.out.println("Card does not have an action");
        }

        hasAttacked = true;
        return 0;
    }

    /** */
    public final int attackHero(final Card opponentHero, final boolean opponentHasTanks) {
        if (frozen) {
            return Constants.CARD_FROZEN;
        }
        if (hasAttacked) {
            return Constants.CARD_HAS_ATTACKED;
        }
        if (opponentHasTanks) {
            return Constants.NOT_TANK;
        }

        hasAttacked = true;
        opponentHero.subtractHealth(attackDamage);
        return 0;
    }

    /** */
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
