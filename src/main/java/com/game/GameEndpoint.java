package com.game;

import java.io.IOException;
import java.util.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/game")
public class GameEndpoint {

    private static final Map<String, Session> players = new HashMap<>();
    private static final Map<String, String> choices = new HashMap<>();

    private static int round = 1;
    private static final int MAX_ROUNDS = 3;

    private static int scoreP1 = 0;
    private static int scoreP2 = 0;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        synchronized (players) {
            if (players.size() >= 2) {
                session.close();
                return;
            }

            String role = players.isEmpty() ? "player1" : "player2";
            players.put(role, session);

            session.getBasicRemote().sendText(
                "{\"type\":\"role\",\"role\":\"" + role + "\"}"
            );

            if (players.size() == 2) {
                broadcast(
                    "{\"type\":\"start\",\"round\":" + round + 
                    ",\"scoreP1\":" + scoreP1 + 
                    ",\"scoreP2\":" + scoreP2 + "}"
                );
            }
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        synchronized (players) {
            String role = getRole(session);

            if (msg.contains("choice")) {
                String choice = msg.split(":")[1].replace("\"", "").replace("}", "");
                choices.put(role, choice);

                if (choices.size() == 2) {
                    String p1 = choices.get("player1");
                    String p2 = choices.get("player2");

                    String result = decideWinner(p1, p2);

                    if (result.contains("Player 1")) scoreP1++;
                    if (result.contains("Player 2")) scoreP2++;

                    broadcast(
                        "{\"type\":\"round_result\"," +
                        "\"p1\":\"" + p1 + "\"," +
                        "\"p2\":\"" + p2 + "\"," +
                        "\"result\":\"" + result + "\"," +
                        "\"round\":" + round + "," +
                        "\"scoreP1\":" + scoreP1 + "," +
                        "\"scoreP2\":" + scoreP2 + "}"
                    );

                    choices.clear();
                    round++;

                    if (round <= MAX_ROUNDS) {
                        broadcast(
                            "{\"type\":\"next_round\",\"round\":" + round + "}"
                        );
                    } else {
                        String finalWinner =
                            scoreP1 > scoreP2 ? "Player 1 Wins 🏆" :
                            scoreP2 > scoreP1 ? "Player 2 Wins 🏆" :
                            "Match Draw 🤝";

                        broadcast(
                            "{\"type\":\"game_over\"," +
                            "\"final\":\"" + finalWinner + "\"," +
                            "\"scoreP1\":" + scoreP1 + "," +
                            "\"scoreP2\":" + scoreP2 + "}"
                        );

                        resetGame();
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        synchronized (players) {
            players.values().remove(session);
            resetGame();
        }
    }

    private void resetGame() {
        round = 1;
        scoreP1 = 0;
        scoreP2 = 0;
        choices.clear();
    }

    private String getRole(Session s) {
        return players.entrySet().stream()
                .filter(e -> e.getValue().equals(s))
                .map(Map.Entry::getKey)
                .findFirst().orElse("");
    }

    private void broadcast(String msg) throws IOException {
        for (Session s : players.values()) {
            s.getBasicRemote().sendText(msg);
        }
    }

    private String decideWinner(String p1, String p2) {
        if (p1.equals(p2)) return "Draw 🤝";
        if ((p1.equals("Rock") && p2.equals("Scissors")) ||
            (p1.equals("Paper") && p2.equals("Rock")) ||
            (p1.equals("Scissors") && p2.equals("Paper")))
            return "Player 1 Wins 🎉";
        return "Player 2 Wins 🎉";
    }
}
