package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.*;

import java.util.Objects;

public class ServicesDaemon {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public ServicesDaemon(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

    public String register(String username, String password, String email) throws BadRequestException, DataAccessException, UserTakenException {
        if ((username.isEmpty()) || (password.isEmpty()) || (email.isEmpty())) {
            throw new BadRequestException("Error: bad request");
        }
        var oldUser = this.user.getUser(username);
        if (oldUser != null) {
            throw new UserTakenException("Error: already taken");
        } else {
            UserData newUser = user.createUser(username, password, email);
            return auth.createAuth(newUser).authToken();
        }
    }

    public String login(String username, String password) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if ((username.isEmpty()) || (password.isEmpty())) {
            throw new BadRequestException("Error: bad request");
        }
        UserData currentUser = user.getUser(username);
        if ((currentUser == null) || (!Objects.equals(currentUser.password(), password))) {
            throw new UnauthorizedUserException("Error: unauthorized");
        } else {
            return auth.createAuth(currentUser).authToken();
        }
    }

    public void logout(String authToken) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        if (authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData currentAuth = auth.getAuth(authToken);
        if (currentAuth == null) {
            throw new UnauthorizedUserException("Error: unauthorized");
        } else {
            auth.removeAuth(currentAuth);
        }
    }

    public void clear() throws DataAccessException {
        auth.clear();
        games.clear();
        user.clear();
    }

}
