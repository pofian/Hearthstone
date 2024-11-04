package main;

import fileio.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import utils.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Game {
    final private ArrayList<Player> players;
    final Statistics statistics;
    ArrayList<ArrayList<Card>> table;
    int startingPlayer = 0, playerWhoHasTurn = 0, currentRound;

    public Game(Input inputData, GameInput gameInput, Statistics statistics) {
        this.statistics = statistics;
        ArrayList<CardInput> deckPlayerOne = inputData.getPlayerOneDecks().
                getDecks().get(gameInput.getStartGame().getPlayerOneDeckIdx());
        ArrayList<CardInput> deckPlayerTwo = inputData.getPlayerTwoDecks().
                getDecks().get(gameInput.getStartGame().getPlayerTwoDeckIdx());

        int seed = gameInput.getStartGame().getShuffleSeed();
        Collections.shuffle(deckPlayerOne, new Random(seed));
        Collections.shuffle(deckPlayerTwo, new Random(seed));

        table = new ArrayList<>(4);
        for(int i=0; i<4; i++)
            table.add(new ArrayList<>());

        players = new ArrayList<>(2);
        players.add(new Player(new Deck(deckPlayerOne),
                new Hero(gameInput.getStartGame().getPlayerOneHero()), 2, 3, table));
        players.add(new Player(new Deck(deckPlayerTwo),
                new Hero(gameInput.getStartGame().getPlayerTwoHero()), 1, 0, table));

        startingPlayer = gameInput.getStartGame().getStartingPlayer() - 1;
        currentRound = 0;
        newRound();
        for(ActionsInput action : gameInput.getActions())
            runGame(action);
    }

    private void runGame(ActionsInput action) {
        switch (action.getCommand()) {
            case "getPlayerDeck" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerDeck(idx, players.get(idx-1));
            }
            case "getPlayerMana" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerMana(idx, players.get(idx-1));
            }
            case "getCardsOnTable" -> {
                statistics.getCardsOnTable(table);
            }
            case "getCardsInHand" -> {
                int idx = action.getPlayerIdx();
                statistics.getCardsInHand(idx, players.get(idx-1));
            }
            case "getPlayerHero" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerHero(idx, players.get(idx-1).getHero());
            }
            case "getPlayerTurn" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerTurn(playerWhoHasTurn + 1);
            }
            case "getCardAtPosition" -> {
                statistics.getCardAtPosition(action.getX(), action.getY(), table);
            }

            case "endPlayerTurn" -> {
                players.get(playerWhoHasTurn).setHasFinishedTurn(true);
                playerWhoHasTurn = (playerWhoHasTurn + 1) % 2;
                if(playerWhoHasTurn == startingPlayer)
                    newRound();
            }
            case "placeCard" -> {
                int idx = action.getHandIdx();
                Player player = players.get(playerWhoHasTurn);
                statistics.placeCard(idx, player.placeCard(idx));
            }
            case "cardUsesAttack" -> {
                int errorCode;
                if(action.getCardAttacker().getX() / 2 == action.getCardAttacked().getX() / 2)
                    errorCode = 1;
                else {
                    Card attacker = table.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY());
                    if(table.get(action.getCardAttacked().getX()).size() > action.getCardAttacked().getY()) {
                        Card attacked = table.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY());
                        errorCode = attacker.attack(attacked,
                                !table.get(players.get(1 - playerWhoHasTurn).getFrontRowIdx()).isEmpty());
                        if(errorCode == 0 && attacked.getHealth() <= 0)
                            table.get(action.getCardAttacked().getX()).remove(action.getCardAttacked().getY());
                    } else {
                        System.out.println("naspa");
                        errorCode = 5;
                    }
                }
                statistics.cardUsesAttack(action.getCardAttacker(), action.getCardAttacked(), errorCode);
            }
            case "cardUsesAbility" -> {
                int errorCode;
                if (table.get(action.getCardAttacker().getX()).size() <= action.getCardAttacked().getY() ||
                    table.get(action.getCardAttacked().getX()).size() <= action.getCardAttacked().getY()) {
                    System.out.println("nasoll");
                    errorCode = 6;
                } else {
                    Card attacker = table.get(action.getCardAttacker().getX()).get(action.getCardAttacker().getY());
                    Card attacked = table.get(action.getCardAttacked().getX()).get(action.getCardAttacked().getY());
                    errorCode = attacker.action(attacked,
                            (action.getCardAttacker().getX() / 2 == action.getCardAttacked().getX() / 2),
                            !table.get(players.get(1 - playerWhoHasTurn).getFrontRowIdx()).isEmpty());
                    if(errorCode == 0 && attacked.getHealth() <= 0)
                        table.get(action.getCardAttacked().getX()).remove(action.getCardAttacked().getY());
                }
                statistics.cardUsesAbility(action.getCardAttacker(), action.getCardAttacked(), errorCode);
                if(errorCode == 5) {
                    statistics.getCardsOnTable(table);
                }
            }

//            default -> System.out.println(action.getCommand());
            }
        }

    private void newRound() {
        currentRound++;
        playerWhoHasTurn = startingPlayer;
        for(Player player : players) {
            player.addMana(min(currentRound, 10));
            player.drawCard();
            player.setHasFinishedTurn(false);
        }
        for(ArrayList<Card> arr : table)
            for(Card card : arr) {
                card.setFrozen(false);
                card.setHasAttacked(false);
            }
    }
}
