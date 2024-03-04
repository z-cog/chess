package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    SQLAuthDAO() {
        //connect to auth table
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
        dataBaseTest();
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), user.username());
        //INSERT INTO auth (authToken, username) VALUES (newAuth.authToken, newAuth.username
        return newAuth;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        dataBaseTest();
        //SELECT authToken from auth
        return null;
    }

    public void removeAuth(AuthData currentAuth) throws DataAccessException {
        dataBaseTest();
        //DELETE FROM auth WHERE authToken = currentAuth
    }

    public void clear() throws DataAccessException {
        dataBaseTest();
        //TRUNCATE TABLE auth
    }

    private void dataBaseTest() throws DataAccessException {
        //insert code to check if connection worked. Maybe not needed??
        throw new DataAccessException("Error: auth database inaccessible");
    }

}
