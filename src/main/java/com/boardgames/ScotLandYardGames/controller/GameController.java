package com.boardgames.ScotLandYardGames.controller;

import com.boardgames.ScotLandYardGames.model.Game;
import com.boardgames.ScotLandYardGames.model.dto.ConnectMessage;
import com.boardgames.ScotLandYardGames.model.dto.GameMessage;
import com.boardgames.ScotLandYardGames.service.GameService;
import com.boardgames.ScotLandYardGames.storage.GameStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping
@RestController
public class GameController {

    @Autowired
    private ObjectMapper objectMapper;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/game.join")
    public Game join(@RequestBody ConnectMessage connectMessage) throws JsonProcessingException {
        Game game = gameService.joingame(connectMessage.getPlayer());

        if (game != null) {
            System.out.println("User connected: " + connectMessage.getPlayer());
            Map<String, Object> payload = new HashMap<>();
            payload.put("Message", "Game created!");
            payload.put("GameId", game.getGameId());
            payload.put("Users", game.getPlayers());
            payload.put("Turn", game.getTurn());
            payload.put("Round", game.getRound());

            simpMessagingTemplate.convertAndSend("/topic/game-created", objectMapper.writeValueAsString(payload));
            System.out.println("Game started: " + game.getGameId());
        } else {
            System.out.println("User connected: " + connectMessage.getPlayer());
            Map<String, String> payload = new HashMap<>();
            payload.put("username", connectMessage.getPlayer().replaceAll("^\"|\"$", ""));
            payload.put("message", "Please wait for more players to join...");
            simpMessagingTemplate.convertAndSend("/topic/reply", objectMapper.writeValueAsString(payload));
            return null;
        }
        return game;
    }


    @MessageMapping("/game.move")
    public Game makeMove(@RequestBody GameMessage gameMessage) throws JsonProcessingException {
        Game game = gameService.gamePlay(gameMessage);
        Map<String, Object> payload = new HashMap<>();
        payload.put("Message", "Game updated!");
        payload.put("GameId", game.getGameId());
        payload.put("Users", game.getPlayers());
        payload.put("Turn", game.getTurn());
        payload.put("Round", game.getRound());

        if (game.getWinner() != null) {
            payload.put("Winner", game.getWinner());
            payload.put("GameOver", true);
            simpMessagingTemplate.convertAndSend("/topic/game-over", objectMapper.writeValueAsString(payload));
        } else {
            simpMessagingTemplate.convertAndSend("/topic/game-progress", objectMapper.writeValueAsString(payload));
        }
        return game;
    }


    @MessageMapping("/game.destinations")
    public void getValidDestinations(@RequestBody GameMessage gameMessage) throws JsonProcessingException {
        Game game = GameStorage.getInstance().getGames().get(gameMessage.getGameId());
        if (game == null) {
            throw new IllegalStateException("Game not found");
        }

        List<Integer> validDestinations = game.getValidDestinations(gameMessage.getPlayer(), gameMessage.getTransportation());
        Map<String, Object> payload = new HashMap<>();
        payload.put("Message", "Valid Destinations");
        payload.put("GameId", game.getGameId());
        payload.put("Player", gameMessage.getPlayer());
        payload.put("Transportation", gameMessage.getTransportation());
        payload.put("Destinations", validDestinations);

        simpMessagingTemplate.convertAndSend("/topic/valid-destinations", objectMapper.writeValueAsString(payload));
    }
}
