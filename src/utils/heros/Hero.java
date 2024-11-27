package utils.heros;

import fileio.CardInput;
import utils.cards.Card;
import utils.Constants;

import java.util.ArrayList;

public class Hero extends Card {

    public Hero(final CardInput card) {
        super(card);
        setHealth(Constants.HERO_INITIAL_HEALTH);
    }

    /** */
    public int useAbility(final ArrayList<Card> cardsAffected, final boolean cardBelongsToEnemy) {
        return 0;
    }

    /** */
    public static Hero newHero(final CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Lord Royce" -> {
                return new LordRoyce(cardInput);
            }
            case "Empress Thorina" -> {
                return new EmpressThorina(cardInput);
            }
            case "King Mudface" -> {
                return new KingMudface(cardInput);
            }
            case "General Kocioraw" -> {
                return new GeneralKocioraw(cardInput);
            }
            default -> {
                return new Hero(cardInput);
            }
        }
    }
}
