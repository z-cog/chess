package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO {

    SQLGameDAO() {
        //connect to games
    }

    public int createGame(String gameName) throws DataAccessException {
        dataBaseTest();
        //serialize new ChessGame();
        //INSERT INTO games (gameName, game) VALUES (gameName, see above)
        int gameID = 0;//SELECT CURRENT_ID()

        return gameID;
    }

    public Collection<GameData> getGame(int gameID) throws DataAccessException {
        dataBaseTest();
        HashSet<GameData> listOfGames = new HashSet<>();
        if (gameID == -1) {
            //SELECT ALL FROM games
            //put it in listOfGames
        } else {
            //SELECT (gameID, whiteUsername, blackUsername, gameName, game)
            //deserialize game
            //put it all in a new chessGame object
            //put it in listOfGames
        }
        return listOfGames;
    }

    public void updateGame(GameData game) throws DataAccessException {
        dataBaseTest();
        //UPDATE games SET ALL = ALL WHERE id = game.id
    }

    public void clear() throws DataAccessException {
        dataBaseTest();
        //TRUNCATE TABLE games
    }

    private void dataBaseTest() throws DataAccessException {
        //check connection.
        throw new DataAccessException("Error: games database inaccessible");
    }
}
