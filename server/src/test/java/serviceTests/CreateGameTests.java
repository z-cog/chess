package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTests {
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
    public void createNewGame() {
        try {
            String authToken = service.register("bob", "I can't belive its not butter", "thisisduein3daysuhoh");
            int gameID = service.createGame(authToken, "uhhhh");
            assertNotNull(games.getGame(gameID));

            //Game name can be left blank
            int newGameID = service.createGame(authToken, "");
            assertNotNull(games.getGame(newGameID));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void unauthorizedUser() {
        assertThrows(UnauthorizedUserException.class, () -> service.createGame("#OnlyYouCanStopWildfires", "my secret game"));
    }
}
