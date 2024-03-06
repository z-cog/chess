package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ServicesDaemon {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public ServicesDaemon(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

    private AuthData checkAuth(String authToken) throws DataAccessException, UnauthorizedUserException {
        AuthData currentAuth = auth.getAuth(authToken);
        if (currentAuth == null) {
            throw new UnauthorizedUserException("Error: unauthorized");
        }
        return currentAuth;
    }

    public String register(String username, String password, String email) throws BadRequestException, DataAccessException, UserTakenException {
        if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        var oldUser = this.user.getUser(username);
        if (oldUser != null) {
            throw new UserTakenException("Error: already taken");
        } else {
            UserData newUser = user.createUser(username, password, email);
            return auth.createAuth(newUser).authToken();
        }
    }

    public String login(String username, String password) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        UserData currentUser = user.getUser(username);
        if ((currentUser == null) || (!user.checkPassword(username, password))) {
            throw new UnauthorizedUserException("Error: unauthorized");
        } else {
            return auth.createAuth(currentUser).authToken();
        }
    }

    public void logout(String authToken) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData currentAuth = checkAuth(authToken);
        auth.removeAuth(currentAuth);
    }

    public Collection<GameData> listGames(String authToken) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        checkAuth(authToken);
        return games.getGame(-1);

    }

    public int createGame(String authToken, String gameName) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        checkAuth(authToken);
        return games.createGame(gameName);
    }

    public void joinGame(String authToken, int gameID, ChessGame.TeamColor teamColor) throws BadRequestException, DataAccessException, UnauthorizedUserException, UserTakenException {
        if (authToken == null || authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = checkAuth(authToken);
        Collection<GameData> gameCollection = games.getGame(gameID);
        if (gameCollection == null || gameCollection.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        GameData currentGame = (GameData) gameCollection.toArray()[0];
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (currentGame.whiteUsername() != null) {
                throw new UserTakenException("Error: already taken");
            }
            games.updateGame(new GameData(gameID, authData.username(), currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        } else if (teamColor == ChessGame.TeamColor.BLACK) {
            if (currentGame.blackUsername() != null) {
                throw new UserTakenException("Error: already taken");
            }
            games.updateGame(new GameData(gameID, currentGame.whiteUsername(), authData.username(), currentGame.gameName(), currentGame.game()));
        }
    }

    public void clear() throws DataAccessException {
        auth.clear();
        games.clear();
        user.clear();
    }

}
