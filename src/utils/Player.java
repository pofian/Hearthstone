package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private Deck deck;
    private ArrayList<Card> hand;
    private Hero hero;
    private int mana;
    private boolean hasFinishedTurn;
    private int frontRowIdx, backRowIdx;
    private final ArrayList<ArrayList<Card>> table;

    public Player (Deck deck, Hero hero, int frontRowIdx, int backRowIdx, ArrayList<ArrayList<Card>> table) {
        this.deck = deck;
        hand = new ArrayList<>();
        this.hero = hero;
        mana = 0;
        this.frontRowIdx = frontRowIdx;
        this.backRowIdx = backRowIdx;
        this.table = table;
    }

    public void drawCard() {
        if(!deck.getCards().isEmpty())
            hand.add(deck.getCards().remove(0));
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void addMana(int mana) {
        this.mana += mana;
    }

    public void subMana(int mana) {
        this.mana -= mana;
    }

    public int getMana() {
        return mana;
    }

    public boolean isHasFinishedTurn() {
        return hasFinishedTurn;
    }

    public void setHasFinishedTurn(boolean hasFinishedTurn) {
        this.hasFinishedTurn = hasFinishedTurn;
    }

    public int getBackRowIdx() {
        return backRowIdx;
    }

    public int getFrontRowIdx() {
        return frontRowIdx;
    }

    public int placeCard(int handIdx) {
//        if(handIdx >= hand.size())
//            return 3;

        Card card = hand.get(handIdx);
        if(mana < card.getMana())
            return 1;
        int type = card.getType();
        ArrayList<Card> placeRow = table.get(frontRowIdx * type + backRowIdx * (1-type));
        if(placeRow.size() >= 5)
            return 2;

        mana -= card.getMana();
        placeRow.add(hand.remove(handIdx));

        return 0;
    }

}
