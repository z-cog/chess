package dataAccess;

import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Objects;

public class SQLUserDAO implements UserDAO {
    BCryptPasswordEncoder encoder;

    public SQLUserDAO() throws DataAccessException {
        encoder = new BCryptPasswordEncoder();
        configureDatabase();
    }

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        var hashedPassword = encoder.encode(password); //but hashed!
        UserData newUser = new UserData(username, hashedPassword, email);
        //INSERT INTO user (username, password, email) VALUES (newUser.username, newUser.hashedPassword, newUser.email)
        return newUser;
    }

    public UserData getUser(String username) throws DataAccessException {
        //SELECT FROM user WHERE username = username
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var password = "Passwords are not returned for security reasons.";
                        var email = rs.getString("email");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }
        return null;
    }

    public Boolean checkPassword(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var hashedPassword = rs.getString("password");
                        return encoder.matches(password, hashedPassword);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: database inaccessible");
        }
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
                      `username` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      `email` varchar(256) NOT NULL,
                      PRIMARY KEY (`username`),
                      INDEX(email)
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
