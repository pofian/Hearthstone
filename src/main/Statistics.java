package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

import fileio.Coordinates;
import utils.cards.Card;
import utils.Constants;
import utils.heros.Hero;
import utils.Player;

public final class Statistics {
    private int totalGamesPlayed;
    private final int[] playerWins = new int[2];
    private ArrayNode output;
    private static final Statistics INSTANCE = new Statistics();

    private Statistics() {

    }

    public static Statistics getInstance() {
        return INSTANCE;
    }

    /** */
    public void setOutputAndReset(final ArrayNode newOutput) {
        this.output = newOutput;
        totalGamesPlayed = 0;
        playerWins[0] = 0;
        playerWins[1] = 0;
    }

    /** */
    public void increaseTotalGamesPlayed() {
        totalGamesPlayed++;
    }

    /** */
    public void getTotalGamesPlayed() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", totalGamesPlayed);
        output.addPOJO(node);
    }

    /** */
    public void getPlayerOneWins() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", playerWins[0]);
        output.addPOJO(node);
    }

    /** */
    public void getPlayerTwoWins() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", playerWins[1]);
        output.addPOJO(node);
    }

    /** */
    public void getPlayerDeck(final int playerIdx, final Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        ArrayList<ObjectNode> cardList = new ArrayList<>();
        for (Card card : player.getDeck()) {
            cardList.add(card.toObjectNode());
        }
        node.putPOJO("output", cardList);
        output.addPOJO(node);
    }

    /** */
    public void getCardsInHand(final int playerIdx, final Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", playerIdx);

        ArrayList<ObjectNode> cardList = new ArrayList<>();
        for (Card card : player.getHand()) {
            cardList.add(card.toObjectNode());
        }
        node.putPOJO("output", cardList);
        output.addPOJO(node);
    }

    /** */
    public void getCardsOnTable(final ArrayList<ArrayList<Card>> table) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsOnTable");

        ArrayNode tableNode = new ArrayNode(JsonNodeFactory.instance);
        for (ArrayList<Card> tableRow : table) {
            ArrayList<ObjectNode> cardNode = new ArrayList<>();
            for (Card card : tableRow) {
                cardNode.add(card.toObjectNode());
            }
            tableNode.addPOJO(cardNode);
        }
        node.putPOJO("output", tableNode);
        output.addPOJO(node);
    }

    /** */
    public void getFrozenCardsOnTable(final ArrayList<ArrayList<Card>> table) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getFrozenCardsOnTable");

        ArrayList<ObjectNode> cardNode = new ArrayList<>();
        for (ArrayList<Card> tableRow : table) {
            for (Card card : tableRow) {
                if (card.getFrozen()) {
                    cardNode.add(card.toObjectNode());
                }
            }
        }
        node.putPOJO("output", cardNode);
        output.addPOJO(node);
    }

    /** */
    public void getPlayerMana(final int playerIdx, final Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", playerIdx);
        node.put("output", player.getMana());
        output.addPOJO(node);
    }

    /** */
     public void getPlayerHero(final int playerIdx, final Hero hero) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);

        ObjectNode newNode = JsonNodeFactory.instance.objectNode();
        newNode.put("mana", hero.getMana());
        newNode.put("description", hero.getDescription());
        newNode.putPOJO("colors",  hero.getColors());
        newNode.put("name", hero.getName());
        newNode.put("health", hero.getHealth());
        node.putPOJO("output", newNode);

        output.addPOJO(node);
    }

    /** */
    public void getPlayerTurn(final int playerWhoHasTurn) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", playerWhoHasTurn);
        output.addPOJO(node);
    }

    /** */
    public void placeCard(final int handIdx, final int errorCode) {
        if (errorCode == 0) {
            return;
        }
        final String[] messages = {
                "Not enough mana to place card on table.",
                "Cannot place card on table since row is full.",
        };
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "placeCard");
        node.put("handIdx", handIdx);
        node.put("error", messages[errorCode - 1]);
        output.addPOJO(node);
    }

    /** */
    public void getCardAtPosition(final int x, final int y,
                                  final ArrayList<ArrayList<Card>> table) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);

        ArrayList<Card> cards = table.get(x);
        if (y < 0 || y >= cards.size()) {
            node.put("output", "No card available at that position.");
        } else {
            node.put("output", cards.get(y).toObjectNode());
        }
        output.addPOJO(node);
    }

    /** */
    public void cardUsesAttack(final Coordinates cordAttacker,
                               final Coordinates cordAttacked, final int errorCode) {
        if (errorCode == 0) {
            return;
        }
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAttack");
        node.putPOJO("cardAttacker", cordAttacker);
        node.putPOJO("cardAttacked", cordAttacked);
        node.put("error", Constants.MESSAGES[errorCode - 1]);
        output.addPOJO(node);
    }

    /** */
    public void cardUsesAbility(final Coordinates cordAttacker,
                                final Coordinates cordAttacked, final int errorCode) {
       if (errorCode == 0) {
            return;
        }
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAbility");
        node.putPOJO("cardAttacker", cordAttacker);
        node.putPOJO("cardAttacked", cordAttacked);
        node.put("error", Constants.MESSAGES[errorCode - 1]);
        output.addPOJO(node);
    }

    /** */
    public void useAttackHero(final Coordinates cordAttacker, final int errorCode) {
       if (errorCode == 0) {
            return;
        }
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "useAttackHero");
        node.putPOJO("cardAttacker", cordAttacker);
        node.put("error", Constants.MESSAGES[errorCode - 1]);
        output.addPOJO(node);
    }

    /** */
    public void useHeroAbility(final int affectedRow, final int errorCode) {
        if (errorCode == 0) {
            return;
        }
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "useHeroAbility");
        node.put("affectedRow", affectedRow);
        final String[] messages = {
                "Not enough mana to use hero's ability.",
                "Hero has already attacked this turn.",
                "Selected row does not belong to the enemy.",
                "Selected row does not belong to the current player."
        };
        node.put("error", messages[errorCode - 1]);
        output.addPOJO(node);
    }

    /** */
    public void gameEnded(final int playerWhoWon) {
        playerWins[playerWhoWon]++;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        final String[] players = {"one", "two"};
        node.put("gameEnded", "Player " + players[playerWhoWon] + " killed the enemy hero.");
        output.addPOJO(node);
    }

}
