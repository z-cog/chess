package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import server.RegisterRequest;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public int createGame(String gameName) throws DataAccessException {
        int gameID = 0;
        var serializedGame = new Gson().toJson(new ChessGame());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameName, game) VALUES (?,?)";
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameName);
                ps.setString(2, serializedGame);
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    gameID = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }

        return gameID;
    }

    public Collection<GameData> getGame(int gameID) throws DataAccessException {
        HashSet<GameData> listOfGames = new HashSet<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = (gameID == -1) ? "SELECT * FROM games" : "SELECT * FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                if (gameID != -1) {
                    ps.setInt(1, gameID);
                }
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var currentGameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var deserializedGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        listOfGames.add(new GameData(currentGameID, whiteUsername, blackUsername, gameName, deserializedGame));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }
        return listOfGames;
    }

    public void updateGame(GameData game) throws DataAccessException {
        //UPDATE games SET ALL = ALL WHERE id = game.id
        var serializedGame = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, game.whiteUsername());
                ps.setString(2, game.blackUsername());
                ps.setString(3, serializedGame);
                ps.setInt(4, game.gameID());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE games";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            String createStatement = """
                    CREATE TABLE IF NOT EXISTS  games (
                      `gameID` INT NOT NULL AUTO_INCREMENT,
                      `whiteUsername` varchar(256) DEFAULT NULL,
                      `blackUsername` varchar(256) DEFAULT NULL,
                      `gameName` varchar(256) NOT NULL,
                      `game` TEXT DEFAULT NULL,
                      PRIMARY KEY (`gameID`),
                      INDEX(gameName)
                    );
                    """;
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to configure database");
        }
    }
}
