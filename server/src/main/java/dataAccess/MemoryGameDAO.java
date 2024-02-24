package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    HashSet<GameData> games = new HashSet<>();
    int currentID = 0;

    public GameData createGame(String gameName) throws DataAccessException {
        dataBaseTest();
        var newGame = new GameData(currentID, "", "", gameName, new ChessGame());
        this.games.add(newGame);
        this.currentID += 1;
        return newGame;
    }

    public Collection<GameData> getGame(int gameID) throws DataAccessException {
        dataBaseTest();
        HashSet<GameData> listOfGames = new HashSet<>();
        if (gameID == -1) {
            listOfGames.addAll(games);
        } else {
            for (var item : games) {
                if (Objects.equals(item.gameID(), gameID)) {
                    listOfGames.add(item);
                    break;
                }
            }
        }
        return listOfGames;
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
        this.currentID = 0;
    }

    private void dataBaseTest() throws DataAccessException {
        if (games == null) {
            throw new DataAccessException("Error: games database inaccessible");
        }
    }
}
