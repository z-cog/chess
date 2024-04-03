package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;

public class WebSocketServices {
    private final AuthDAO auth;
    private final GameDAO games;

    public WebSocketServices(AuthDAO auth, GameDAO games) {
        this.auth = auth;
        this.games = games;
    }

    public String authToUser(String authToken) throws DataAccessException, UnauthorizedUserException {
        var authData = auth.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedUserException("Error: unauthorized.");
        }
        return authData.username();
    }

    public GameData getGameData(int gameID) throws DataAccessException, BadRequestException {
        var gamesCollection = games.getGame(gameID);
        var currentGame = (GameData) gamesCollection.toArray()[0];
        if (currentGame == null) {
            throw new BadRequestException("Error: specified game does not exist.");
        }
        return currentGame;
    }

    public void updateGame(GameData data, ChessGame game, String whiteUsername, String blackUsername) throws DataAccessException {
        GameData updatedGameData = new GameData(data.gameID(), whiteUsername, blackUsername, data.gameName(), game);
        games.updateGame(updatedGameData);
    }
}
