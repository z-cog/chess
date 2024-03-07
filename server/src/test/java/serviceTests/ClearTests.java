package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {
    AuthDAO auth;
    GameDAO games;
    UserDAO user;
    ServicesDaemon service;

    public ClearTests() {
        try {
            auth = new SQLAuthDAO();
            games = new SQLGameDAO();
            user = new SQLUserDAO();
            service = new ServicesDaemon(auth, games, user);
        } catch (Exception e) {
            System.out.println("Error, compilation failed: " + e.getMessage());
        }
    }

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
