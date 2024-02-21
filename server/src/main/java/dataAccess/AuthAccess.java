package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthAccess {
    AuthData createAuth(UserData user) throws DataAccessException;

    Boolean verifyPassword(String password, UserData user);

    AuthData getAuth(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;
}
