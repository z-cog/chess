package server.WebSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import service.WebSocketServices;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {
    private final WebSocketServices service;
    private final ConnectionManager cm;

    public WebSocketHandler(WebSocketServices service) {
        this.service = service;
        this.cm = new ConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, new Gson().fromJson(message, JoinPlayer.class));
        }
    }

    private void joinPlayer(Session session, JoinPlayer cmd) throws Exception {
        int gameID = cmd.getGameID();
        String authToken = cmd.getAuthString();
        cm.add(gameID, authToken, session);
        try {
            String username = service.authToUser(authToken);
            ChessGame game = service.getGame(gameID);

            var message = new LoadGame(game);
            cm.notifyRoot(gameID, authToken, message);

            var notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, " joined as " + cmd.getPlayerColor() + ".");
            cm.broadcast(gameID, null, notification);
        } catch (Exception e) {
            var message = new ServerNotification(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            cm.notifyRoot(gameID, authToken, message);
        }
    }

}
