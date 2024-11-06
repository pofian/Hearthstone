package utils;

public final class Constants {
    private Constants() { }
    public static final String[] MESSAGES = {
            "Attacker card is frozen.",
            "Attacker card has already attacked this turn.",
            "Attacked card is not of type 'Tank'.",
            "Attacked card does not belong to the current player.",
            "Attacked card does not belong to the enemy."
    };
    public static final int CARD_FROZEN = 1, CARD_HAS_ATTACKED = 2, NOT_TANK = 3,
                            CARD_NOT_PLAYER = 4, CARD_NOT_OPPONENT = 5;

    public static final int NOT_ENOUGH_MANA_CARD = 1, ROW_FULL = 2, NOT_ENOUGH_MANA_ABILITY = 1,
                            HERO_HAS_ATTACKED = 2, ROW_NOT_ENEMY = 3, ROW_NOT_PLAYER = 4;

    public static final int ROW_COUNT = 4, COLUMN_COUNT = 5,
                            PLAYER_ONE_FRONT_ROW = 2, PLAYER_ONE_BACK_ROW = 3,
                            PLAYER_TWO_FRONT_ROW = 1, PLAYER_TWO_BACK_ROW = 0,
                            HERO_INITIAL_HEALTH = 30, MAX_MANA_INCREMENT = 10;
}
