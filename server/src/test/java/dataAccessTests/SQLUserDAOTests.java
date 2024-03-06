package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLUserDAOTests {

    @Test
    void createUserTest() {
        try {
            var user = new SQLUserDAO();
            //create user
            assertDoesNotThrow(() -> user.createUser("bob", "bob1234", "bob@bob.bob"));
            //creating a duplicate user throws, as username is primary key.
            //shouldn't ever happen as service prevents it, but you never know.
            assertThrows(DataAccessException.class, () -> user.createUser("bob", "bob1234", "bob@bob.bob"));

            user.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getUserTest() {
        try {
            var user = new SQLUserDAO();
            user.createUser("bob", "bob1234", "bob@bob.bob");
            //get existing user.
            assertNotNull(user.getUser("bob"));
            //get non-existent user.
            assertNull(user.getUser("some random username"));

            user.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkPasswordTest() {
        try {
            var user = new SQLUserDAO();

            user.createUser("bob", "bob1234", "bob@bob.bob");
            //correct password
            assertTrue(user.checkPassword("bob", "bob1234"));

            //incorrect password
            assertFalse(user.checkPassword("bob", "gib me password please"));

            user.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void clearUserTest() {
        try {
            var user = new SQLUserDAO();
            assertDoesNotThrow(user::clear);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
