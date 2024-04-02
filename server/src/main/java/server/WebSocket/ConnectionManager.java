package server.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.computeIfAbsent(gameID, k -> new HashMap<>());
        connections.get(gameID).put(authToken, connection);
    }

    public void remove(int gameID, String authToken) {
        if (connections.get(gameID) != null) {
            connections.get(gameID).remove(authToken);
        }
    }

    public void broadcast(int gameID, String excludeAuth, ServerMessage message) throws IOException {
        var currentIDConnection = connections.get(gameID);

        var removeList = new ArrayList<Connection>();
        for (var c : currentIDConnection.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuth)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            currentIDConnection.remove(c.authToken);
        }
    }
}
