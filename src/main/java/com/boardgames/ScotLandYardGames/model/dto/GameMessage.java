package com.boardgames.ScotLandYardGames.model.dto;

import com.boardgames.ScotLandYardGames.model.Transportation;
import lombok.Data;

@Data
public class GameMessage {
    private String player;
    private String gameId;
    private int nodeId;
    private Transportation transportation;


    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }
}
