package server.WebSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import service.WebSocketServices;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Objects;

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
            var gameData = service.getGameData(gameID);

            String colorUsername;
            if (cmd.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                colorUsername = gameData.whiteUsername();
            } else {
                colorUsername = gameData.blackUsername();
            }

            if (Objects.equals(colorUsername, username)) {
                var message = new LoadGame(gameData.game());
                cm.notifyRoot(gameID, authToken, message);

                var notification = new ServerNotification(username + " joined as " + cmd.getPlayerColor() + ".");
                cm.broadcast(gameID, authToken, notification);

            } else {
                var error = new ServerErrorNotification("Error: color reserved.");
                cm.notifyRoot(gameID, authToken, error);
            }
        } catch (Exception e) {
            var message = new ServerErrorNotification("Error:" + e.getMessage());
            cm.notifyRoot(gameID, authToken, message);
        }
    }

}
