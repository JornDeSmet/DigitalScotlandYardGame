package com.boardgames.ScotLandYardGames.model;

import lombok.Data;

import java.util.*;

@Data
public class Game {
    private String gameId;
    private Board board;
    private List<Player> players;
    private Player turn;
    private String winner;
    private GameState gameState;
    private int round;

    private static final Set<Integer> REVEAL_ROUNDS = Set.of(3, 8, 13, 18, 22);

    public Game(List<String> playerNames) {
        this.gameId = UUID.randomUUID().toString();
        this.board = new Board();
        board.createBoard();

        Collections.shuffle(playerNames);
        players = new ArrayList<>();
        players.add(new Player(playerNames.get(0), true));
        for (int i = 1; i < playerNames.size(); i++) {
            players.add(new Player(playerNames.get(i), false));
        }

        this.turn = players.get(1);
        this.round = 1;
        this.gameState = GameState.DETECTIVE_TURN;
    }

    public void makeMove(String playerName, int nodeId, Transportation transport) {
        Player player = getPlayerByName(playerName);

        System.out.println("Player: " + playerName);
        System.out.println("Player Location: " + player.getLocation());
        System.out.println("Turn: " + turn.getName());
        System.out.println("Round: " + round);

        if (player.getLocation() == -1) {
            System.out.println(playerName + " is choosing their starting location: " + nodeId);

            if (board.getNode(nodeId) == null) {
                throw new IllegalArgumentException("Invalid starting location: Node does not exist.");
            }

            player.setLocation(nodeId);
            updateGameState();
            return;
        }


        Node currentNode = board.getNode(player.getLocation());
        Node targetNode = board.getNode(nodeId);

        System.out.println("Current Node: " + currentNode);
        System.out.println("Target Node: " + targetNode);
        if (currentNode != null) {
            System.out.println("Connections (TRAIN): " + Arrays.toString(currentNode.getConnections().get(Transportation.TRAIN)));
            System.out.println("Connections (TAXI): " + Arrays.toString(currentNode.getConnections().get(Transportation.TAXI)));
        }

        if (!player.equals(turn)) {
            throw new IllegalStateException("Not your turn!");
        }

        if (targetNode == null) {
            throw new IllegalArgumentException("Invalid node ID.");
        }

        if (currentNode == null || !currentNode.getConnections().containsKey(transport)) {
            throw new IllegalStateException("Invalid move: Transport not available from this node.");
        }

        List<Integer> connectionList = Arrays.asList(currentNode.getConnections().get(transport));
        if (!connectionList.contains(nodeId)) {
            throw new IllegalStateException("Invalid move: No such connection exists.");
        }

        player.useTicket(transport);
        player.setLocation(nodeId);

        if (player.isSuspect() && REVEAL_ROUNDS.contains(round)) {
            System.out.println("Mr. X reveals location at Node " + nodeId);
        }

        checkWinner(player, nodeId);
        updateGameState();
    }



    private void checkWinner(Player player, int nodeId) {
        Player suspect = getSuspect();
        if (!player.isSuspect() && nodeId == suspect.getLocation()) {
            winner = "Detectives";
        } else if (round > 22) {
            winner = suspect.getName();
        }
    }
    private void updateGameState() {
        if (winner != null) {
            Player suspect = getSuspect();
            gameState = winner.equals(suspect.getName()) ? GameState.SUSPECT_WON : GameState.DETECTIVES_WON;
        } else {
            int currentIndex = players.indexOf(turn);
            turn = players.get((currentIndex + 1) % players.size()); // Cycle through players
            gameState = turn.isSuspect() ? GameState.SUSPECT_TURN : GameState.DETECTIVE_TURN;
            round++;
        }
    }


    private Player getSuspect() {
        return players.stream()
                .filter(Player::isSuspect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No suspect found."));
    }


    public Player getPlayerByName(String name) {
        String sanitizedName = name.replace("\"", "").trim();
        return players.stream()
                .filter(player -> player.getName().replace("\"", "").trim().equals(sanitizedName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Player not found: " + sanitizedName + ". Available players: " +
                                players.stream()
                                        .map(player -> player.getName().replace("\"", "").trim())
                                        .toList()
                ));
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
