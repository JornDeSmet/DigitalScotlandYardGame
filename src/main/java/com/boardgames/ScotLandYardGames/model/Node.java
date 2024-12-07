package com.boardgames.ScotLandYardGames.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class Node {
    private int id;
    private Map<Transportation, Integer[]> connections;

    public Node(int id) {
        this.id = id;
        this.connections = new HashMap<>();
    }

    public void addConnection(Transportation type, Integer... destinations) {
        connections.put(type, destinations);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Transportation, Integer[]> getConnections() {
        return connections;
    }

    public void setConnections(Map<Transportation, Integer[]> connections) {
        this.connections = connections;
    }
    @Override
    public String toString() {
        return "Node{id=" + id + ", connections=" + connections + "}";
    }
}
