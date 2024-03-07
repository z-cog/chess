package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.BadRequestException;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    AuthDAO auth;
    GameDAO games;
    UserDAO user;
    ServicesDaemon service;

    public LoginTests() {
        try {
            auth = new SQLAuthDAO();
            games = new SQLGameDAO();
            user = new SQLUserDAO();
            service = new ServicesDaemon(auth, games, user);
        } catch (Exception e) {
            System.out.println("Error, compilation failed: " + e.getMessage());
        }
    }

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

}
