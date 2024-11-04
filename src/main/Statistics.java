package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.Coordinates;
import utils.Card;
import utils.Hero;
import utils.Player;

import java.util.ArrayList;

public class Statistics {

    private final ArrayNode output;
    public Statistics(ArrayNode output) {
        this.output = output;
    }

    public void getPlayerDeck(int idx, Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", idx);
        ArrayList<ObjectNode> cardList = new ArrayList<>();
        for(Card card: player.getDeck().getCards())
            cardList.add(card.toObjectNode());
        node.putPOJO("output", cardList);
        output.addPOJO(node);
    }

    public void getCardsInHand(int idx, Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", idx);
        ArrayList<ObjectNode> cardList = new ArrayList<>();
        for(Card card: player.getHand())
            cardList.add(card.toObjectNode());
        node.putPOJO("output", cardList);
        output.addPOJO(node);
    }

    public void getCardsOnTable(ArrayList<ArrayList<Card>> table) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardsOnTable");

        ArrayNode tableNode = new ArrayNode(JsonNodeFactory.instance);
        for(ArrayList<Card> tableRow : table) {
            ArrayList<ObjectNode> cardNode = new ArrayList<>();
            for(Card card: tableRow)
                cardNode.add(card.toObjectNode());
            tableNode.addPOJO(cardNode);
        }
        node.putPOJO("output", tableNode);
        output.addPOJO(node);
    }

    public void getPlayerMana(int idx, Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", idx);
        node.putPOJO("output", player.getMana());
        output.addPOJO(node);
    }

    public void getPlayerHero(int idx, Hero hero) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", idx);

        ObjectNode newNode = JsonNodeFactory.instance.objectNode();
        newNode.put("mana", hero.getMana());
        newNode.put("description", hero.getDescription());

        ArrayNode colorsArray = JsonNodeFactory.instance.arrayNode();
        hero.getColors().forEach(colorsArray::add);
        newNode.set("colors", colorsArray);

        newNode.put("name", hero.getName());
        newNode.put("health", hero.getHealth());

        node.putPOJO("output", newNode);
        output.addPOJO(node);
    }

    public void getPlayerTurn(int playerWhoHasTurn) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", playerWhoHasTurn);
        output.addPOJO(node);
    }

    public void placeCard(int handIdx, int errCode) {
        if(errCode == 0)
            return;
        final String[] messages = {
                "Not enough mana to place card on table.",
                "Cannot place card on table since row is full.",
        };
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "placeCard");
        node.put("handIdx", handIdx);
        node.put("error", messages[errCode - 1]);
        output.addPOJO(node);
    }

    public void getCardAtPosition(int x, int y, ArrayList<ArrayList<Card>> table) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);

        ArrayList<Card> cards = table.get(x);
        if(y < 0 || y >= cards.size())
            node.put("output", "No card available at that position.");
        else
            node.put("output", cards.get(y).toObjectNode());
        output.addPOJO(node);
    }

    public void cardUsesAttack(Coordinates cordAttacker, Coordinates cordAttacked, int errCode) {
        if(errCode == 0)
            return;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAttack");
        ObjectNode cardAttackerNode = JsonNodeFactory.instance.objectNode();
        cardAttackerNode.put("x", cordAttacker.getX());
        cardAttackerNode.put("y", cordAttacker.getY());
        ObjectNode cardAttackedNode = JsonNodeFactory.instance.objectNode();
        cardAttackedNode.put("x", cordAttacked.getX());
        cardAttackedNode.put("y", cordAttacked.getY());
        node.set("cardAttacker", cardAttackerNode);
        node.set("cardAttacked", cardAttackedNode);

        final String[] messages = {
                "Attacked card does not belong to the enemy.",
                "Attacker card has already attacked this turn.",
                "Attacker card is frozen.",
                "Attacked card is not of type 'Tank'.",
                "Attacked card does not exist."
        };
        node.put("error", messages[errCode - 1]);
        output.addPOJO(node);
    }

    public void cardUsesAbility(Coordinates cordAttacker, Coordinates cordAttacked, int errorCode) {
        if(errorCode == 0)
            return;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "cardUsesAbility");

        ObjectNode cardAttackerNode = JsonNodeFactory.instance.objectNode();
        cardAttackerNode.put("x", cordAttacker.getX());
        cardAttackerNode.put("y", cordAttacker.getY());
        ObjectNode cardAttackedNode = JsonNodeFactory.instance.objectNode();
        cardAttackedNode.put("x", cordAttacked.getX());
        cardAttackedNode.put("y", cordAttacked.getY());
        node.set("cardAttacker", cardAttackerNode);
        node.set("cardAttacked", cardAttackedNode);

        final String[] messages = {
                "Attacker card is frozen.",
                "Attacker card has already attacked this turn.",
                "Attacked card does not belong to the current player.",
                "Attacked card does not belong to the enemy.",
                "Attacked card is not of type 'Tank'.",
                "Attacked card does not exist."
        };
        node.put("error", messages[errorCode - 1]);
        output.addPOJO(node);
    }

}
