package main;

import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Input;
import fileio.GameInput;

import java.util.ArrayList;
import utils.Player;
import utils.Card;
import utils.Hero;
import utils.Constants;
import static java.lang.Math.min;

public final class Game {
    private final ArrayList<Player> players;
    private final Statistics statistics;
    private final ArrayList<ArrayList<Card>> table;
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

        int seed = gameInput.getStartGame().getShuffleSeed();
        players = new ArrayList<>(2);
        players.add(new Player(deckPlayerOne, new Hero(gameInput.getStartGame().getPlayerOneHero()),
                Constants.PLAYER_ONE_FRONT_ROW, Constants.PLAYER_ONE_BACK_ROW, table, seed));
        players.add(new Player(deckPlayerTwo, new Hero(gameInput.getStartGame().getPlayerTwoHero()),
                Constants.PLAYER_TWO_FRONT_ROW, Constants.PLAYER_TWO_BACK_ROW, table, seed));

        startingPlayer = gameInput.getStartGame().getStartingPlayer() - 1;
        currentRound = 0;
        newRound();
    }

    /**
     *
     */
    public void runGame(final ActionsInput action) {
        switch (action.getCommand()) {
            case "getTotalGamesPlayed" -> statistics.getTotalGamesPlayed();
            case "getPlayerOneWins" -> statistics.getPlayerOneWins();
            case "getPlayerTwoWins" -> statistics.getPlayerTwoWins();
            case "getPlayerDeck" -> {
                int playerIdx = action.getPlayerIdx();
                statistics.getPlayerDeck(playerIdx, players.get(playerIdx - 1));
            }
            case "getPlayerMana" -> {
                int playerIdx = action.getPlayerIdx();
                statistics.getPlayerMana(playerIdx, players.get(playerIdx - 1));
            }
            case "getCardsOnTable" -> statistics.getCardsOnTable(table);
            case "getCardsInHand" -> {
                int playerIdx = action.getPlayerIdx();
                statistics.getCardsInHand(playerIdx, players.get(playerIdx - 1));
            }
            case "getPlayerHero" -> {
                int playerIdx = action.getPlayerIdx();
                statistics.getPlayerHero(playerIdx, players.get(playerIdx - 1).getHero());
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
                int playerIdx = action.getHandIdx();
                statistics.placeCard(playerIdx, players.get(playerWhoHasTurn).placeCard(playerIdx));
            }
            case "cardUsesAttack" -> {
                int errorCode;
                if (action.getCardAttacker().getX() / 2 == action.getCardAttacked().getX() / 2) {
                    errorCode = Constants.CARD_NOT_OPPONENT;
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
            default -> System.out.println("Unrecognised command");
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
