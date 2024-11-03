package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;

import java.util.Collections;

import java.util.ArrayList;
import java.util.Random;

import utils.*;

public class Game {
    final private ArrayList<Player> players, sortedPlayers;
    final Statistics statistics;
    ArrayList<ArrayList<Minion>> table;

    public Game(Input inputData, GameInput gameInput, Statistics statistics) {
        this.statistics = statistics;
        ArrayList<CardInput> deckPlayerOne = inputData.getPlayerOneDecks().
                getDecks().get(gameInput.getStartGame().getPlayerOneDeckIdx());
        ArrayList<CardInput> deckPlayerTwo = inputData.getPlayerTwoDecks().
                getDecks().get(gameInput.getStartGame().getPlayerTwoDeckIdx());


        int seed = gameInput.getStartGame().getShuffleSeed();
        Collections.shuffle(deckPlayerOne, new Random(seed));
        Collections.shuffle(deckPlayerTwo, new Random(seed));

        players = new ArrayList<>(2);
        players.add(new Player(new Deck(deckPlayerOne), new Hero(gameInput.getStartGame().getPlayerOneHero())));
        players.add(new Player(new Deck(deckPlayerTwo), new Hero(gameInput.getStartGame().getPlayerTwoHero())));

        if(gameInput.getStartGame().getStartingPlayer() == 1) {
            sortedPlayers = players;
        } else {
            sortedPlayers = new ArrayList<>(2);
            sortedPlayers.add(players.get(1));
            sortedPlayers.add(players.get(0));
        }

        table = new ArrayList<>(4);

        runGame(gameInput.getActions());
    }

    private void runGame(ArrayList<ActionsInput> actions) {

        for(Player player : players)
            player.drawCard();

        for(ActionsInput action : actions) {
            int idx = action.getPlayerIdx();
            switch (action.getCommand()) {
                case "getPlayerDeck" -> statistics.getCardsInHand(idx, players.get(idx-1));
                case "getPlayerHero" -> statistics.getPlayerHero(idx, players.get(idx-1).getHero());
                case "getPlayerTurn" -> statistics.getPlayerTurn();
            }
        }

    }
}
