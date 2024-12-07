package com.boardgames.ScotLandYardGames.model.dto;

import lombok.Data;

@Data
public class ConnectMessage {
    private String player;

    public ConnectMessage(String player) {
        this.player = player;
    }

}