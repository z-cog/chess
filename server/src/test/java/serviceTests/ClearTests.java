package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {
    AuthDAO auth = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();
    UserDAO user = new MemoryUserDAO();
    ServicesDaemon service = new ServicesDaemon(auth, games, user);

    @Test
    public void clearTest() {
        try {
            //This is just putting something into the UserDAO
            service.register("blah", "blah", "blah");

            assertNotNull(user.getUser("blah"));

            assertDoesNotThrow(() -> service.clear());

            assertNull(user.getUser("blah"));

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
