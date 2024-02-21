package dataAccess;


import chess.ChessGame;
import model.*;

import java.util.Collection;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;

    AuthData createAuth(UserData user) throws DataAccessException;

    Boolean verifyPassword(String password, UserData user) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    Collection<ChessGame> getGame(int gameID) throws DataAccessException;

    GameData createGame(String gameName) throws DataAccessException;

    void updatePlayer(ChessGame.TeamColor clientColor, GameData gameData) throws DataAccessException;

    void clearDatabase() throws DataAccessException;
}
