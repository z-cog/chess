package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.UserTakenException;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
    @Test
    public void registerNewUser() {
        AuthDAO auth = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();
        UserDAO user = new MemoryUserDAO();

        ServicesDaemon service = new ServicesDaemon(auth, games, user);

        //Registers a new user, "bob"
        assertDoesNotThrow(() -> service.register("bob", "bob1234", "bob@bob.bob"));

        //Fails, as username matches
        assertThrows(UserTakenException.class, () -> service.register("bob", "bob", "bob"));

    }
}
