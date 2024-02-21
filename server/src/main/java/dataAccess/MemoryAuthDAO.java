package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    HashSet<AuthData> auth = new HashSet<>();

    public AuthData createAuth(UserData user) throws DataAccessException {
        dataBaseTest();
        AuthData new_auth = new AuthData(UUID.randomUUID().toString(), user.username());
        auth.add(new_auth);
        return new_auth;
    }

    public Boolean verifyPassword(String password, UserData user) {
        return Objects.equals(user.password(), password);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        dataBaseTest();
        for (AuthData item : auth) {
            if (Objects.equals(item.authToken(), authToken)) {
                return item;
            }
        }
        return null;
    }

    public void removeAuth(String authToken) throws DataAccessException {
        dataBaseTest();
        for (AuthData item : auth) {
            if (Objects.equals(item.authToken(), authToken)) {
                auth.remove(item);
                break;
            }
        }
    }

    public void clear() throws DataAccessException {
        dataBaseTest();
        auth.clear();
    }

    private void dataBaseTest() throws DataAccessException {
        if (auth == null) {
            throw new DataAccessException("Error: auth database inaccessible");
        }
    }
}
