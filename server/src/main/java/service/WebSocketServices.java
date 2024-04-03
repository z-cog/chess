package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class WebSocketServices {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public WebSocketServices(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

    public String authToUser(String authToken) throws DataAccessException, UnauthorizedUserException {
        var authData = auth.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedUserException("Error: unauthorized.");
        }
        return authData.username();
    }

    public ChessGame getGame(int gameID) throws DataAccessException, BadRequestException {
        var gamesCollection = games.getGame(gameID);
        var currentGame = (ChessGame) gamesCollection.toArray()[0];
        if (currentGame == null) {
            throw new BadRequestException("Error: specified game does not exist.");
        }
        return currentGame;
    }

}
