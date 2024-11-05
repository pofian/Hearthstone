package main;

import fileio.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

import utils.*;

import static java.lang.Math.min;

public final class Game {
    final private ArrayList<Player> players;
    final private Statistics statistics;
    final private ArrayList<ArrayList<Card>> table;
    private final int startingPlayer;
    private int playerWhoHasTurn, currentRound;

    public Game(final Input inputData, final GameInput gameInput, final Statistics statistics) {
        this.statistics = statistics;
        ArrayList<CardInput> deckPlayerOne = inputData.getPlayerOneDecks().
                getDecks().get(gameInput.getStartGame().getPlayerOneDeckIdx());
        ArrayList<CardInput> deckPlayerTwo = inputData.getPlayerTwoDecks().
                getDecks().get(gameInput.getStartGame().getPlayerTwoDeckIdx());

        table = new ArrayList<>(Constants.ROW_COUNT);
        for (int i = 0; i < Constants.ROW_COUNT; i++) {
            table.add(new ArrayList<>());
        }

        players = new ArrayList<>(2);
        players.add(new Player(deckPlayerOne,
                new Hero(gameInput.getStartGame().getPlayerOneHero()), 2, 3, table));
        players.add(new Player(deckPlayerTwo,
                new Hero(gameInput.getStartGame().getPlayerTwoHero()), 1, 0, table));

        int seed = gameInput.getStartGame().getShuffleSeed();
        Collections.shuffle(players.get(0).getDeck(), new Random(seed));
        Collections.shuffle(players.get(1).getDeck(), new Random(seed));

        startingPlayer = gameInput.getStartGame().getStartingPlayer() - 1;
        currentRound = 0;
        newRound();
    }

    public void runGame(final ActionsInput action) {
        switch (action.getCommand()) {
            case "getTotalGamesPlayed" -> statistics.getTotalGamesPlayed();
            case "getPlayerOneWins" -> statistics.getPlayerOneWins();
            case "getPlayerTwoWins" -> statistics.getPlayerTwoWins();
            case "getPlayerDeck" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerDeck(idx, players.get(idx - 1));
            }
            case "getPlayerMana" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerMana(idx, players.get(idx - 1));
            }
            case "getCardsOnTable" -> statistics.getCardsOnTable(table);
            case "getCardsInHand" -> {
                int idx = action.getPlayerIdx();
                statistics.getCardsInHand(idx, players.get(idx - 1));
            }
            case "getPlayerHero" -> {
                int idx = action.getPlayerIdx();
                statistics.getPlayerHero(idx, players.get(idx - 1).getHero());
            }
            case "getPlayerTurn" -> statistics.getPlayerTurn(playerWhoHasTurn + 1);
            case "getCardAtPosition" -> statistics.getCardAtPosition(action.getX(),
                                                                        action.getY(), table);
            case "getFrozenCardsOnTable" -> statistics.getFrozenCardsOnTable(table);

            case "endPlayerTurn" -> {
                Player player = players.get(playerWhoHasTurn);
                for (Card card : table.get(player.getBackRowIdx())) {
                    card.setFrozen(false);
                    card.setHasAttacked(false);
                }
                for (Card card : table.get(player.getFrontRowIdx())) {
                    card.setFrozen(false);
                    card.setHasAttacked(false);
                }
                playerWhoHasTurn = (playerWhoHasTurn + 1) % 2;
                if (playerWhoHasTurn == startingPlayer) {
                    newRound();
                }
            }
            case "placeCard" -> {
                int idx = action.getHandIdx();
                Player player = players.get(playerWhoHasTurn);
                statistics.placeCard(idx, player.placeCard(idx));
            }
            case "cardUsesAttack" -> {
                int errorCode;
                if (action.getCardAttacker().getX() / 2 == action.getCardAttacked().getX() / 2) {
                    errorCode = 1;
                } else {
                    Card attacker = table.get(action.getCardAttacker().getX()).
                            get(action.getCardAttacker().getY());
                    Card attacked = table.get(action.getCardAttacked().getX()).
                            get(action.getCardAttacked().getY());
                    errorCode = attacker.attack(attacked,
                            players.get(1 - playerWhoHasTurn).hasPlacedTanks());
                    if (errorCode == 0 && attacked.getHealth() <= 0) {
                        table.get(action.getCardAttacked().getX()).
                                remove(action.getCardAttacked().getY());
                    }
                }
                statistics.cardUsesAttack(action.getCardAttacker(),
                        action.getCardAttacked(), errorCode);
            }
            case "cardUsesAbility" -> {
                int errorCode;
                Card attacker = table.get(action.getCardAttacker().getX()).
                        get(action.getCardAttacker().getY());
                Card attacked = table.get(action.getCardAttacked().getX()).
                        get(action.getCardAttacked().getY());
                errorCode = attacker.action(attacked,
                        action.getCardAttacker().getX() / 2 == action.getCardAttacked().getX() / 2,
                        players.get(1 - playerWhoHasTurn).hasPlacedTanks());
                if (errorCode == 0 && attacked.getHealth() <= 0) {
                    table.get(action.getCardAttacked().getX()).
                            remove(action.getCardAttacked().getY());
                }
                statistics.cardUsesAbility(action.getCardAttacker(),
                        action.getCardAttacked(), errorCode);
            }
            case "useAttackHero" -> {
                int errorCode;
                Card attacker = table.get(action.getCardAttacker().getX()).
                        get(action.getCardAttacker().getY());
                Player opponent = players.get(1 - playerWhoHasTurn);
                errorCode = attacker.attackHero(opponent.getHero(), opponent.hasPlacedTanks());

                statistics.useAttackHero(action.getCardAttacker(), errorCode);
                if (players.get(1 - playerWhoHasTurn).getHero().getHealth() <= 0) {
                    statistics.gameEnded(playerWhoHasTurn);
                }
            }
            case "useHeroAbility" -> {
                int affectedRow = action.getAffectedRow();
                statistics.useHeroAbility(affectedRow,
                        players.get(playerWhoHasTurn).useHeroAbility(affectedRow));
            }
        }
    }

    private void newRound() {
        currentRound++;
        playerWhoHasTurn = startingPlayer;
        for (Player player : players) {
            player.addMana(min(currentRound, Constants.MAX_MANA_INCREMENT));
            player.drawCard();
            player.getHero().setHasAttacked(false);
        }
    }
}
