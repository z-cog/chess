package clientSoftware.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    public Session session;
    private final ServerMessageHandler client;
    private final String authToken;

    public WebSocketFacade(String serverUrl, String authToken, ServerMessageHandler chessClient) throws Exception {
        try {
            this.authToken = authToken;
            this.client = chessClient;
            serverUrl = serverUrl.replace("http", "ws");
            URI uri = new URI(serverUrl + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage response = new Gson().fromJson(message, ServerMessage.class);
                    switch (response.getServerMessageType()) {
                        case LOAD_GAME -> client.notify(new Gson().fromJson(message, LoadGame.class));
                        default -> client.notify(new Gson().fromJson(message, ServerNotification.class));
                    }
                }
            });
        } catch (Exception e) {
            throw new Exception("Error: problem connecting WebSocket");
        }
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor color) throws Exception {
        try {
            var action = new JoinPlayer(this.authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception e) {
            throw new Exception("Error: problem joining game via WebSocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}