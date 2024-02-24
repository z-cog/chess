package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTest {
    AuthDAO auth = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();
    UserDAO user = new MemoryUserDAO();
    ServicesDaemon service = new ServicesDaemon(auth, games, user);

    @BeforeEach
    public void init() {
        try {
            service.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void emptyGameList() {
        try {
            String authToken = service.register("bob", "uhhhh", "please@hotmail.fix");
            assertTrue(service.listGames(authToken).isEmpty());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
