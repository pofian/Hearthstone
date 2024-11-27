package utils.heros;

import fileio.CardInput;
import utils.cards.Card;
import utils.Constants;

import java.util.ArrayList;

public final class LordRoyce extends Hero {
    public LordRoyce(final CardInput card) {
        super(card);
    }

    @Override
    public int useAbility(final ArrayList<Card> cardsAffected, final boolean cardsBelongToPlayer) {
        if (cardsBelongToPlayer) {
            return Constants.ROW_NOT_ENEMY;
        }
        for (Card card : cardsAffected) {
            card.setFrozen(true);
        }
        return 0;
    }
}

