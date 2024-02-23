package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(UserData user) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void removeAuth(AuthData currentAuth) throws DataAccessException;

    void clear() throws DataAccessException;
}
