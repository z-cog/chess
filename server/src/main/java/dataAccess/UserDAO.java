package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
