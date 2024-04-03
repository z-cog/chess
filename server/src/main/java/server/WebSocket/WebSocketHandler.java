package server.WebSocket;

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
        cm.add(cmd.getGameID(), cmd.getAuthString(), session);
        var message = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, "Yoo mr white");
        cm.broadcast(cmd.getGameID(), null, message);
    }

}
