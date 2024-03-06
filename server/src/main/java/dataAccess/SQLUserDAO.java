package dataAccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Objects;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        var hashedPassword = password; //but hashed!
        UserData newUser = new UserData(username, hashedPassword, email);
        //INSERT INTO user (username, password, email) VALUES (newUser.username, newUser.hashedPassword, newUser.email)
        return newUser;
    }

    public UserData getUser(String username) throws DataAccessException {
        //SELECT FROM user WHERE username = username
        return null;
    }

    public Boolean checkPassword(String username, String password) throws DataAccessException {
        return false;
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE user";
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
                    CREATE TABLE IF NOT EXISTS  user (
                      `id` INT NOT NULL AUTO_INCREMENT,
                      `username` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      `email` varchar(256) NOT NULL,
                      PRIMARY KEY (`id`),
                      INDEX(username)
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
