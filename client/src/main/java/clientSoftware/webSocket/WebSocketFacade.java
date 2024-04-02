package clientSoftware.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

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
                public void onMessage(String message) {
                    ServerMessage response = new Gson().fromJson(message, ServerMessage.class);
                    client.notify(response);
                }
            });
        } catch (Exception e) {
            throw new Exception("Error: problem connecting WebSocket");
        }
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor color) throws Exception {
        try {
            System.out.println("yoooo it broken");
        } catch (Exception e) {
            throw new Exception("Error: problem joining game via WebSocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}