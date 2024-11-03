package utils;

import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(ArrayList<CardInput> deckInput) {
        cards = new ArrayList<>();
        for(CardInput card : deckInput)
            cards.add(new Card(card));
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
