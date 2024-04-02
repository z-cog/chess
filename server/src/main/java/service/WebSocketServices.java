package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class WebSocketServices {
    private final AuthDAO auth;
    private final GameDAO games;
    private final UserDAO user;

    public WebSocketServices(AuthDAO auth, GameDAO games, UserDAO user) {
        this.auth = auth;
        this.games = games;
        this.user = user;
    }

}
