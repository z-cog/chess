package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTests {
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
    public void logoutExistingUser() {
        try {
            String authToken = service.register("bob", "bob", "bob");
            assertDoesNotThrow(() -> service.logout(authToken));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void logoutNonexistentUser() {
        assertThrows(UnauthorizedUserException.class, () -> service.logout("bobbert"));
    }
}
