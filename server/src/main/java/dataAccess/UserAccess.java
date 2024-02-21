package dataAccess;

import model.UserData;

public interface UserAccess {
    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void removeUser(String username) throws DataAccessException;
}
