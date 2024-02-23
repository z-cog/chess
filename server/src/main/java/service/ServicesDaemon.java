package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ServicesDaemon {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public ServicesDaemon(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

    public void clear() throws DataAccessException {
        auth.clear();
        games.clear();
        user.clear();
    }

}
