package dataAccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    HashSet<UserData> user = new HashSet<>();

    public UserData createUser(String username, String password, String email) throws DataAccessException {
        dataBaseTest();
        UserData newUser = new UserData(username, password, email);
        user.add(newUser);
        return newUser;
    }

    public UserData getUser(String username) throws DataAccessException {
        dataBaseTest();
        for (var item : user) {
            if (Objects.equals(item.username(), username)) {
                return item;
            }
        }
        return null;
    }

    public void clear() throws DataAccessException {
        dataBaseTest();
        user.clear();
    }

    private void dataBaseTest() throws DataAccessException {
        if (user == null) {
            throw new DataAccessException("Error: user database inaccessible");
        }
    }
}
