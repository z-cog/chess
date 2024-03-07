package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ServicesDaemon;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTest {
    AuthDAO auth;
    GameDAO games;
    UserDAO user;
    ServicesDaemon service;

    public ListGamesTest() {
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
    public void emptyGameList() {
        try {
            String authToken = service.register("bob", "uhhhh", "please@hotmail.fix");
            assertTrue(service.listGames(authToken).isEmpty());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void nonEmptyGameList() {
        try {
            String authToken = service.register("Robert", "123456798", "Robert@Bobbert.bob");

            games.createGame("Lorem");
            games.createGame("Ipsum");
            games.createGame("Dolor");
            games.createGame("Amet");

            assertFalse(service.listGames(authToken).isEmpty());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void unauthorizedUser() {
        assertThrows(UnauthorizedUserException.class, () -> service.listGames("totally legit auth"));
    }
}
