package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    HashSet<GameData> games = new HashSet<>();

    public GameData createGame(String gameName) throws DataAccessException {
        dataBaseTest();
        return null;
    }

    public Collection<ChessGame> getGame(int gameID) throws DataAccessException {
        dataBaseTest();
        return null;
    }

    public GameData updatePlayer(String username, ChessGame.TeamColor clientColor, GameData gameData) {
        return null;
    }

    public void updateGame(GameData game) throws DataAccessException {
        dataBaseTest();
        for (var item : games) {
            if (Objects.equals(item.gameID(), game.gameID())) {
                games.remove(item);
                games.add(game);
                break;
            }
        }
    }

    public void clear() throws DataAccessException {
        dataBaseTest();
        games.clear();
    }

    private void dataBaseTest() throws DataAccessException {
        if (games == null) {
            throw new DataAccessException("Error: games database inaccessible");
        }
    }
}
