package com.boardgames.ScotLandYardGames.service;

import com.boardgames.ScotLandYardGames.model.Game;
import com.boardgames.ScotLandYardGames.model.dto.GameMessage;
import com.boardgames.ScotLandYardGames.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Service
public class GameService {

    private final Map<String, Game> games;
    private final List<String> waitingPlayers;

    public GameService() {
        games = new ConcurrentHashMap<>();
        waitingPlayers = new ArrayList<>();
    }

    public synchronized Game joingame(String player) {

        if (waitingPlayers.contains(player)) {
            throw new IllegalStateException("Player is already waiting to join a game.");
        }

        waitingPlayers.add(player);

        if (waitingPlayers.size() == 3) {
            Game newGame = new Game(new ArrayList<>(waitingPlayers));
            games.put(newGame.getGameId(), newGame);
            waitingPlayers.clear();
            GameStorage.getInstance().setGame(newGame);
            return newGame;
        }
        return null;
    }

    public synchronized Game gamePlay(GameMessage gameMessage) {
        if (!GameStorage.getInstance().getGames().containsKey(gameMessage.getGameId())) {
            throw new IllegalStateException("game not found");
        }
        Game game = GameStorage.getInstance().getGames().get(gameMessage.getGameId());
        game.makeMove(gameMessage.getPlayer(), gameMessage.getNodeId(), gameMessage.getTransportation());

        GameStorage.getInstance().setGame(game);
        return game;
    }





}