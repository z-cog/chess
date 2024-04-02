package server.WebSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import service.WebSocketServices;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;

@WebSocket
public class WebSocketHandler {
    private final WebSocketServices service;

    public WebSocketHandler(WebSocketServices service) {
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
//        switch (command.getCommandType()) {
//            case JOIN_PLAYER -> ""
//        }
    }

}
