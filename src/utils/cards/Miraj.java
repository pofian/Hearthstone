package utils.cards;

import fileio.CardInput;
import utils.Constants;

public final class Miraj extends Card {
    public Miraj(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public int ability(final Card attacked, final boolean samePlayer,
                       final boolean opponentHasTanks) {
        if (samePlayer) {
            return Constants.CARD_NOT_OPPONENT;
        }
        if (opponentHasTanks && !attacked.getIsTank()) {
            return Constants.NOT_TANK;
        }
        swapHealth(attacked);
        return 0;
    }
}
