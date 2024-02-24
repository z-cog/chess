package dataAccess;


import chess.ChessGame;
import model.*;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(String gameName) throws DataAccessException;

    Collection<GameData> getGame(int gameID) throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
}
