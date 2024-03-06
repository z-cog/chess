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
            var authDao = new SQLAuthDAO();
            var user = new UserData("bob", "bob1234", "bob@bob.bob");
            assertDoesNotThrow(() -> authDao.createAuth(user));
            //duplicate authTokens for users are allowed
            assertDoesNotThrow(() -> authDao.createAuth(user));
            //You can't really fail to make a new auth really, so I hope this is good enough for now.
            authDao.clear();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void getAuthTest() {
        try {
            var authDao = new SQLAuthDAO();
            //authToken not in database.
            assertNull(authDao.getAuth("fejkaldsf"));

            //authToken is in database.
            AuthData authData = authDao.createAuth(new UserData("bob", "bob1234", "bob@bob.bob"));
            assertNotNull(authDao.getAuth(authData.authToken()));

            authDao.clear();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void deleteAuthTest() {
        try {
            var authDao = new SQLAuthDAO();
            AuthData authData = authDao.createAuth(new UserData("bob", "bob1234", "bob@bob.bob"));
            assertDoesNotThrow(() -> authDao.removeAuth(authData));

            //removing a non-existent auth doesn't throw.
            assertDoesNotThrow(() -> authDao.removeAuth(authData));
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void clearAuthTest() {
        try {
            var authDao = new SQLAuthDAO();
            assertDoesNotThrow(authDao::clear);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
