package server.WebSocket;

import chess.ChessGame;
import chess.ChessMove;
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
            case JOIN_OBSERVER -> joinObserver(session, new Gson().fromJson(message, JoinObserver.class));
            case MAKE_MOVE -> makeMove(session, new Gson().fromJson(message, MakeMove.class));
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

    private void joinObserver(Session session, JoinObserver cmd) throws Exception {
        int gameID = cmd.getGameID();
        String authToken = cmd.getAuthString();
        cm.add(gameID, authToken, session);
        try {
            String username = service.authToUser(authToken);
            var gameData = service.getGameData(gameID);
            if (gameData.game() != null) {
                var message = new LoadGame(gameData.game());
                cm.notifyRoot(gameID, authToken, message);
                var notification = new ServerNotification(username + " joined as observer.");
                cm.broadcast(gameID, authToken, notification);
            } else {
                var error = new ServerErrorNotification("Error: game does not exist.");
                cm.notifyRoot(gameID, authToken, error);
            }
        } catch (Exception e) {
            var message = new ServerErrorNotification("Error:" + e.getMessage());
            cm.notifyRoot(gameID, authToken, message);
        }
    }

    private void makeMove(Session session, MakeMove cmd) throws Exception {
        int gameID = cmd.getGameID();
        ChessMove move = cmd.getMove();
        String authToken = cmd.getAuthString();
        try {
            cm.check(gameID, authToken, session);
            String username = service.authToUser(authToken);
            var gameData = service.getGameData(gameID);
            var currentGame = gameData.game();
            var piece = gameData.game().getBoard().getPiece(move.getStartPosition());
            if (piece != null) {
                if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && Objects.equals(username, gameData.whiteUsername())) ||
                        (piece.getTeamColor() == ChessGame.TeamColor.BLACK && Objects.equals(username, gameData.blackUsername()))) {
                    currentGame.makeMove(move);
                    service.updateGame(currentGame, gameData);

                    var message = new LoadGame(currentGame);
                    cm.broadcast(gameID, null, message);

                    var notification = new ServerNotification(username + " moved " + piece.getPieceType() + " " + move + ".");
                    cm.broadcast(gameID, authToken, notification);

                    if (currentGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                        var whiteCheckmate = new ServerNotification(gameData.whiteUsername() + " is in checkmate! No other moves can be made.");
                        cm.broadcast(gameID, null, whiteCheckmate);
                    } else if (currentGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                        var blackCheckmate = new ServerNotification(gameData.blackUsername() + " is in checkmate! No other moves can be made.");
                        cm.broadcast(gameID, null, blackCheckmate);
                    } else if (currentGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                        var whiteCheck = new ServerNotification(gameData.whiteUsername() + " is in check!");
                        cm.broadcast(gameID, null, whiteCheck);
                    } else if (currentGame.isInCheck(ChessGame.TeamColor.BLACK)) {
                        var blackCheck = new ServerNotification(gameData.blackUsername() + " is in check!");
                        cm.broadcast(gameID, null, blackCheck);
                    }
                } else {
                    var error = new ServerErrorNotification("Error: unauthorized user.");
                    cm.notifyRoot(gameID, authToken, error);
                }
            } else {
                var error = new ServerErrorNotification("Error: no piece selected.");
                cm.notifyRoot(gameID, authToken, error);
            }

        } catch (Exception e) {
            var message = new ServerErrorNotification("Error: " + e.getMessage());
            cm.notifyRoot(gameID, authToken, message);
        }
    }

}
