package utils.heros;

import fileio.CardInput;
import utils.cards.Card;
import utils.Constants;

import java.util.ArrayList;

public final class EmpressThorina extends Hero {
    public EmpressThorina(final CardInput card) {
        super(card);
    }

    @Override
    public int useAbility(final ArrayList<Card> cardsAffected, final boolean cardsBelongToPlayer) {
        if (cardsBelongToPlayer) {
            return Constants.ROW_NOT_ENEMY;
        }
        Card maxHpCard = null;
        for (Card card : cardsAffected) {
            if (maxHpCard == null || card.getHealth() > maxHpCard.getHealth()) {
                maxHpCard = card;
            }
        }
        if (maxHpCard != null) {
            cardsAffected.remove(maxHpCard);
        }
        return 0;
    }
}
