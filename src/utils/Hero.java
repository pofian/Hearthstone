package utils;

import fileio.CardInput;
import utils.Card;

import java.util.ArrayList;

public class Hero extends Card {

    public Hero (CardInput card) {
        super(card);
        setHealth(30);
    }

    public void useAbility(ArrayList<Card> cardsAffected) {
        setHasAttacked(true);

        switch (getName()) {
            case "Lord Royce" -> {
                for(Card card : cardsAffected)
                  card.setFrozen(true);
            }
            case "Empress Thorina" -> {
                Card maxHpCard = null;
                for(Card card : cardsAffected)
                    if(maxHpCard == null || card.getHealth() > maxHpCard.getHealth())
                        maxHpCard = card;
                if(maxHpCard != null)
                    cardsAffected.remove(maxHpCard);
            }
            case "King Mudface" -> {
                for(Card card : cardsAffected)
                    card.addHealth(1);
            }
            case "General Kocioraw" -> {
                for(Card card : cardsAffected)
                    card.addAttackDamage(1);
            }

        }
    }

    public String toString() {
        return "CardInput{"
                +  "mana=" + getMana()
                +  ", description='" + getDescription()
                + '\'' + ", colors=" + getColors()
                + ", name='" + getName() + '\''
                + ", health=" + getHealth()
                + '}';
    }
}
