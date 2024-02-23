package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.BadRequestException;
import service.UserTakenException;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
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
    public void registerNewUser() {
        //Registers a new user, "bob"
        assertDoesNotThrow(() -> service.register("bob", "bob1234", "bob@bob.bob"));
    }

    @Test
    public void registerTakenUser() {

        assertDoesNotThrow(() -> service.register("bob", "bob1234", "bob@bob.bob"));

        //Fails, as username matches
        assertThrows(UserTakenException.class, () -> service.register("bob", "bob5678", "bob@mike.bob.com"));

        //Succeeds, passwords and emails don't need to be unique
        assertDoesNotThrow(() -> service.register("mike", "bob1234", "bob@bob.bob"));

    }

    @Test
    public void registerBadRequest() {
        //Fails, as an empty string is a bad request.
        assertThrows(BadRequestException.class, () -> service.register("", "", ""));
    }

}
