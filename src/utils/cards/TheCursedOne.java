package utils.cards;

import fileio.CardInput;
import utils.Constants;

public final class TheCursedOne extends Card {
    public TheCursedOne(final CardInput cardInput) {
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
        attacked.swapHealthAndAttackDamage();
        return 0;
    }
}
