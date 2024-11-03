package utils;

import java.util.ArrayList;

public class Player {
    private Deck deck;
    private ArrayList<Card> hand;
    private Hero hero;
    private int mana;

    public Player (Deck deck, Hero hero) {
        this.deck = deck;
        this.hero = hero;
        hand = new ArrayList<>();
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
}
