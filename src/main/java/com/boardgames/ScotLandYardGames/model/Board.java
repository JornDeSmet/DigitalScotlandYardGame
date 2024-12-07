package com.boardgames.ScotLandYardGames.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Node> nodes;

    public Board() {
        this.nodes = new HashMap<>();
    }
    public void createBoard() {
        // Node 1 connections
        Node node1 = new Node(1);
        node1.addConnection(Transportation.TAXI, 10, 11,5);
        addNode(node1);

        // Node 2 connections
        Node node2 = new Node(2);
        node2.addConnection(Transportation.TAXI, 5, 6);
        addNode(node2);

        // Node 3 connections
        Node node3 = new Node(3);
        node3.addConnection(Transportation.TAXI, 8, 7, 12);
        addNode(node3);

        // Node 4 connections
        Node node4 = new Node(4);
        node4.addConnection(Transportation.TAXI, 8, 9);
        addNode(node4);

        // Node 5 connections
        Node node5 = new Node(5);
        node5.addConnection(Transportation.TAXI, 1, 2, 11, 6);
        addNode(node5);

        // Node 6 connections
        Node node6 = new Node(6);
        node6.addConnection(Transportation.TAXI, 2,7,5);
        node6.addConnection(Transportation.TRAIN, 10); // Special connection
        addNode(node6);

        // Node 7 connections
        Node node7 = new Node(7);
        node7.addConnection(Transportation.TAXI, 3, 6);
        node7.addConnection(Transportation.TRAIN, 9); // Special connection
        addNode(node7);

        // Node 8 connections
        Node node8 = new Node(8);
        node8.addConnection(Transportation.TAXI, 4, 9, 12, 3);
        addNode(node8);

        // Node 9 connections
        Node node9 = new Node(9);
        node9.addConnection(Transportation.TAXI, 4, 8, 10);
        node9.addConnection(Transportation.TRAIN, 7); // Special connection
        addNode(node9);

        // Node 10 connections
        Node node10 = new Node(10);
        node10.addConnection(Transportation.TAXI, 9, 1);
        node10.addConnection(Transportation.TRAIN, 6); // Special connection
        addNode(node10);

        // Node 11 connections
        Node node11 = new Node(11);
        node11.addConnection(Transportation.TAXI, 1, 5, 12);
        addNode(node11);

        // Node 12 connections
        Node node12 = new Node(12);
        node12.addConnection(Transportation.TAXI, 3, 8, 11);
        addNode(node12);
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void printBoard() {
        for (Node node : nodes.values()) {
            System.out.println("Node " + node.getId() + " Connections: " + node.getConnections());
        }
    }
}