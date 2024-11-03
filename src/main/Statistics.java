package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.Card;
import utils.Hero;
import utils.Player;

import java.util.ArrayList;

public class Statistics {

    private final ArrayNode output;
    public Statistics(ArrayNode output) {
        this.output = output;
    }

    public void getCardsInHand(int idx, Player player) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", idx);
        node.putPOJO("output", player.getDeck().getCards());
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

    public void getPlayerTurn() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", 2);
        output.addPOJO(node);
    }

}
