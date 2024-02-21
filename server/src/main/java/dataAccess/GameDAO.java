package dataAccess;


import chess.ChessGame;
import model.*;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(String gameName) throws DataAccessException;

    Collection<ChessGame> getGame(int gameID) throws DataAccessException;

    GameData updatePlayer(String username, ChessGame.TeamColor clientColor, GameData gameData);

    void updateGame(GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
}
