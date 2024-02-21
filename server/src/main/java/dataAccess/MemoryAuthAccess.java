package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthAccess implements AuthAccess {
    HashMap<String, AuthData> auth = new HashMap<>();

    public AuthData createAuth(UserData user) throws DataAccessException {
        AuthData new_auth = new AuthData(UUID.randomUUID().toString(), user.username());
        if (auth != null) {
            auth.put(user.username(), new_auth);
            return new_auth;
        } else {
            throw new DataAccessException("Error: auth database inaccessible");
        }
    }

    public Boolean verifyPassword(String password, UserData user) {
        return Objects.equals(user.password(), password);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void removeAuth(String authToken) throws DataAccessException {

    }
}
