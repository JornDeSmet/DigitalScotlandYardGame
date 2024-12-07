package com.boardgames.ScotLandYardGames.controller;

import com.boardgames.ScotLandYardGames.model.Game;
import com.boardgames.ScotLandYardGames.model.dto.GameMessage;
import com.boardgames.ScotLandYardGames.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/join")
    public Game join(@RequestBody String player){
        Game game = gameService.joingame(player);
        if (game != null) {
            simpMessagingTemplate.convertAndSend("/topic/game-created", "Game created! Game ID: " + game.getGameId());
        } else {
            simpMessagingTemplate.convertAndSendToUser(player, "/queue/reply", "Waiting for more players to join...");
        }
        return ResponseEntity.ok(game).getBody();
    }

    @PostMapping("/makemove")
    public ResponseEntity<Game> makeMove(@RequestBody GameMessage gameMessage){
        Game game = gameService.gamePlay(gameMessage);
        simpMessagingTemplate.convertAndSend("/topic/game-progress" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
