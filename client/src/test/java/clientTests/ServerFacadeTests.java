package clientTests;

import clientSoftware.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    @Test
    public void clearDatabase() {
        assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void registerNewUser() {
        assertDoesNotThrow(() -> facade.register("bob", "bob", "bob"));
    }

    @Test
    public void registerExistingUser() {
        assertDoesNotThrow(() -> facade.register("bob", "bob", "dasfeasdfea"));
        assertThrows(Exception.class, () -> facade.register("bob", "bob", "dasfeasdfea"));
    }

    @Test
    public void loginExistingUser() {
        assertDoesNotThrow(() -> facade.register("bob", "bob", "dasfeasdfea"));
        assertDoesNotThrow(() -> facade.login("bob", "dasfeasdfea"));
    }

    @Test
    public void loginNonexistentUserThrows() {
        assertThrows(Exception.class, () -> facade.login("uh idk", "please let me in"));
    }

    @Test
    public void logoutExistingUser() {
        assertDoesNotThrow(() -> facade.register("bob", "bob", "dasfeasdfea"));
        assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void logoutNonexistentUserThrows() {
        assertThrows(Exception.class, () -> facade.logout());
    }

    @Test
    public void createNewGame() {
        assertDoesNotThrow(() -> facade.register("Rupert", "bob@bobsbob.bob", "17bottlesofpop"));
        assertDoesNotThrow(() -> facade.createGame("Rupert's Game"));
    }

    @Test
    public void malformedGameRequestThrows() {
        assertThrows(Exception.class, () -> facade.createGame(null));
    }

    @Test
    public void listGamesValidAuth() {
        assertDoesNotThrow(() -> facade.register("Rupert", "bob@bobsbob.bob", "17bottlesofpop"));
        assertDoesNotThrow(() -> facade.createGame("bob's world"));
        assertDoesNotThrow(() -> facade.listGames());
    }

    @Test
    public void listGamesInvalidAuth() {
        assertThrows(Exception.class, () -> facade.listGames());
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


}
