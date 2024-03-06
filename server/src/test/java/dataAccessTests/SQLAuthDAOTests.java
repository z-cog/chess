package dataAccessTests;

import dataAccess.SQLAuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLAuthDAOTests {
    @Test
    public void createAuthTest() {
        try {
            var auth = new SQLAuthDAO();
            var user = new UserData("bob", "bob1234", "bob@bob.bob");
            assertDoesNotThrow(() -> auth.createAuth(user));

            //duplicate authTokens for users are allowed
            assertDoesNotThrow(() -> auth.createAuth(user));
            
            auth.clear();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void getAuthTest() {
        try {
            var auth = new SQLAuthDAO();
            //authToken not in database.
            assertNull(auth.getAuth("fejkaldsf"));

            //authToken is in database.
            AuthData authData = auth.createAuth(new UserData("bob", "bob1234", "bob@bob.bob"));
            assertNotNull(auth.getAuth(authData.authToken()));

            auth.clear();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void deleteAuthTest() {
        try {
            var auth = new SQLAuthDAO();
            AuthData authData = auth.createAuth(new UserData("bob", "bob1234", "bob@bob.bob"));
            assertDoesNotThrow(() -> auth.removeAuth(authData));

            //removing a non-existent auth doesn't throw.
            assertDoesNotThrow(() -> auth.removeAuth(authData));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void clearAuthTest() {
        try {
            var auth = new SQLAuthDAO();
            assertDoesNotThrow(auth::clear);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
