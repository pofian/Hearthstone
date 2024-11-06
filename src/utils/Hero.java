package utils;

import fileio.CardInput;
import java.util.ArrayList;

public final class Hero extends Card {

    public Hero(final CardInput card) {
        super(card);
        int initialHealth = Constants.HERO_INITIAL_HEALTH;
        setHealth(initialHealth);
    }

    /** */
    public void useAbility(final ArrayList<Card> cardsAffected) {
        setHasAttacked(true);
        switch (getName()) {
            case "Lord Royce" -> {
                for (Card card : cardsAffected) {
                    card.setFrozen(true);
                }
            }
            case "Empress Thorina" -> {
                Card maxHpCard = null;
                for (Card card : cardsAffected) {
                    if (maxHpCard == null || card.getHealth() > maxHpCard.getHealth()) {
                        maxHpCard = card;
                    }
                }
                if (maxHpCard != null) {
                    cardsAffected.remove(maxHpCard);
                }
            }
            case "King Mudface" -> {
                for (Card card : cardsAffected) {
                    card.addHealth(1);
                }
            }
            case "General Kocioraw" -> {
                for (Card card : cardsAffected) {
                    card.addAttackDamage(1);
                }
            }
            default -> System.out.println("Hero does not have an ability");
        }
    }
}
