package utils.cards;

import fileio.CardInput;
import utils.Constants;

public final class Disciple extends Card {
    public Disciple(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public int ability(final Card attacked, final boolean samePlayer,
                       final boolean opponentHasTanks) {
        if (!samePlayer) {
            return Constants.CARD_NOT_PLAYER;
        }
        attacked.addHealth(2);
        return 0;
    }
}
