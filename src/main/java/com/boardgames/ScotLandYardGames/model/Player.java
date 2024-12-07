package com.boardgames.ScotLandYardGames.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class Player {
    private String name;
    private boolean isSuspect;
    private int location;
    private Map<Transportation, Integer> tickets;

    public Player(String name, boolean isSuspect) {
        this.name = name;
        this.isSuspect = isSuspect;
        this.location = -1;
        this.tickets = new HashMap<>();

        if (isSuspect) {
            tickets.put(Transportation.TAXI, Integer.MAX_VALUE);
            tickets.put(Transportation.TRAIN, Integer.MAX_VALUE);
        } else {
            tickets.put(Transportation.TAXI, 10);
            tickets.put(Transportation.TRAIN, 4);
        }
    }


    public boolean hasTickets(Transportation transport) {
        return tickets.getOrDefault(transport, 0) > 0;
    }

    public void useTicket(Transportation transport) {
        if (!hasTickets(transport)) {
            throw new IllegalStateException("Not enough tickets for this move.");
        }
        tickets.put(transport, tickets.get(transport) - 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuspect() {
        return isSuspect;
    }

    public void setSuspect(boolean suspect) {
        isSuspect = suspect;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Map<Transportation, Integer> getTickets() {
        return tickets;
    }

    public void setTickets(Map<Transportation, Integer> tickets) {
        this.tickets = tickets;
    }
    @Override
    public String toString() {
        return "Player{name='" + name + "', role=" + (isSuspect ? "Suspect" : "Detective") + "}";
    }

}
