package utils;

import fileio.CardInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private final ArrayList<Card> hand, deck;
    private final Hero hero;
    private int mana;
    private final int frontRowIdx, backRowIdx;
    private final ArrayList<ArrayList<Card>> table;

    public Player(final ArrayList<CardInput> deck, final Hero hero, final int frontRowIdx,
                  final int backRowIdx, final ArrayList<ArrayList<Card>> table, final int seed) {
        hand = new ArrayList<>();
        this.deck = createDeck(deck);
        Collections.shuffle(this.deck, new Random(seed));
        this.hero = hero;
        mana = 0;
        this.frontRowIdx = frontRowIdx;
        this.backRowIdx = backRowIdx;
        this.table = table;
    }

    private ArrayList<Card> createDeck(final ArrayList<CardInput> deckInput) {
        ArrayList<Card> newDeck = new ArrayList<>();
        for (CardInput card : deckInput) {
            newDeck.add(new Card(card));
        }
        return newDeck;
    }

    /** */
    public void drawCard() {
        if (!deck.isEmpty()) {
            hand.add(deck.remove(0));
        }
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }

    /** */
    public void addMana(final int manaAdded) {
        mana += manaAdded;
    }

    public int getMana() {
        return mana;
    }

    public int getBackRowIdx() {
        return backRowIdx;
    }

    public int getFrontRowIdx() {
        return frontRowIdx;
    }

    /** */
    public int placeCard(final int handIdx) {
        Card card = hand.get(handIdx);
        if (mana < card.getMana()) {
            return Constants.NOT_ENOUGH_MANA_CARD;
        }
        int type = card.getType();
        ArrayList<Card> placeRow = table.get(frontRowIdx * type + backRowIdx * (1 - type));
        if (placeRow.size() >= Constants.COLUMN_COUNT) {
            return Constants.ROW_FULL;
        }

        mana -= card.getMana();
        placeRow.add(hand.remove(handIdx));
        return 0;
    }

    /** */
    public boolean hasPlacedTanks() {
        for (Card card : table.get(frontRowIdx)) {
            if (card.getIsTank()) {
                return true;
            }
        }
        return false;
    }

    /** */
    public int useHeroAbility(final int affectedRowIdx) {
        if (mana < hero.getMana()) {
            return Constants.NOT_ENOUGH_MANA_ABILITY;
        }
        if (hero.getHasAttacked()) {
            return Constants.HERO_HAS_ATTACKED;
        }
        switch (hero.getName()) {
            case "Lord Royce", "Empress Thorina" -> {
                if (affectedRowIdx == frontRowIdx || affectedRowIdx == backRowIdx) {
                    return Constants.ROW_NOT_ENEMY;
                }
            }
            case "General Kocioraw", "King Mudface" -> {
                if (affectedRowIdx != frontRowIdx && affectedRowIdx != backRowIdx) {
                    return Constants.ROW_NOT_PLAYER;
                }
            }
            default -> System.out.println("Hero does not have an ability");
        }
        hero.useAbility(table.get(affectedRowIdx));
        mana -= hero.getMana();
        return 0;
    }
}
