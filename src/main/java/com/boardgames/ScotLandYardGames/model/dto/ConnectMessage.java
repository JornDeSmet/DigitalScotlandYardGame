package com.boardgames.ScotLandYardGames.model.dto;

import lombok.Data;

@Data
public class ConnectMessage {
    private String type;
    private String player;

    public ConnectMessage(String type, String player) {
        this.type = type;
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public String getType() {
        return type;
    }
}