package utils.heros;

import fileio.CardInput;
import utils.cards.Card;
import utils.Constants;

import java.util.ArrayList;

public final class KingMudface extends Hero {
    public KingMudface(final CardInput card) {
        super(card);
    }

    @Override
    public int useAbility(final ArrayList<Card> cardsAffected, final boolean cardsBelongToPlayer) {
        if (!cardsBelongToPlayer) {
            return Constants.ROW_NOT_PLAYER;
        }
        for (Card card : cardsAffected) {
            card.addHealth(1);
        }
        return 0;
    }
}

