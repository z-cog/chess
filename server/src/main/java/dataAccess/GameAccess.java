package dataAccess;


import chess.ChessGame;
import model.*;

import java.util.Collection;

public interface GameAccess {
    GameData createGame(String gameName) throws DataAccessException;

    Collection<ChessGame> getGame(int gameID) throws DataAccessException;

    void updatePlayer(ChessGame.TeamColor clientColor, GameData gameData) throws DataAccessException;

    void clearGames() throws DataAccessException;
}
