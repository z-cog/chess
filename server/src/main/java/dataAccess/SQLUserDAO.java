package dataAccess;

import model.UserData;

import java.util.Objects;

public class SQLUserDAO implements UserDAO {
    SQLUserDAO() {
        //connect to user
    }

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        dataBaseTest();
        var hashedPassword = password; //but hashed!
        UserData newUser = new UserData(username, hashedPassword, email);
        //INSERT INTO user (username, password, email) VALUES (newUser.username, newUser.hashedPassword, newUser.email)
        return newUser;
    }

    public UserData getUser(String username) throws DataAccessException {
        dataBaseTest();
        //SELECT FROM user WHERE username = username
        return null;
    }

    public Boolean checkPassword(String username, String password) throws DataAccessException {
        return false;
    }

    public void clear() throws DataAccessException {
        //TRUNCATE TABLE user
    }

    private void dataBaseTest() throws DataAccessException {
        //Put code here to check database connection.
        throw new DataAccessException("Error: user database inaccessible");
    }
}
