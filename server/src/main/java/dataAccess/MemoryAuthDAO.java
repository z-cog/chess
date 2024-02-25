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
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), user.username());
        dataBaseTest();
        auth.add(newAuth);
        return newAuth;
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

    public void removeAuth(AuthData currentAuth) throws DataAccessException {
        dataBaseTest();
        auth.remove(currentAuth);
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
