package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.BadRequestException;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
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
    public void loginExistingUser() {
        assertDoesNotThrow(() -> service.register("mike", "averystrongpassword", "yo@email.gov"));

        assertDoesNotThrow(() -> service.login("mike", "averystrongpassword"));

    }

    @Test
    public void loginNonexistentUser() {
        assertThrows(UnauthorizedUserException.class, () -> service.login("mike", "averystrongpassword"));
    }

    @Test
    public void loginWrongCredentials() {
        assertDoesNotThrow(() -> service.register("mike", "averystrongpassword", "yo@email.gov"));

        assertThrows(UnauthorizedUserException.class, () -> service.login("mike", "AStrongerPassword1234"));
    }

    @Test
    public void loginBadRequest() {
        assertThrows(BadRequestException.class, () -> service.login("", ""));
    }

    @Test
    public void loginRemovesOldAuth() {
        try {
            String oldAuth = service.register("bob", "bob", "bob@bobmail.bob");
            assertNotNull(auth.getAuth(oldAuth));

            String newAuth = service.login("bob", "bob");
            assertNotNull(auth.getAuth(newAuth));
            assertNull(auth.getAuth(oldAuth));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
