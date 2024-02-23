package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.*;

public class ServicesDaemon {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public ServicesDaemon(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var oldUser = this.user.getUser(username);
        if (oldUser != null) {
            throw new UserTakenException("Error: already taken");
        } else {
            var newUser = user.createUser(username, password, email);
            return auth.createAuth(newUser);
        }
    }

    public void clear() throws DataAccessException {
        auth.clear();
        games.clear();
        user.clear();
    }

}
