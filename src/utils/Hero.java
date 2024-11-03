package utils;

import fileio.CardInput;
import utils.Card;

public class Hero extends Card {

    public Hero (CardInput card) {
        super(card);
        super.setHealth(30);
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
