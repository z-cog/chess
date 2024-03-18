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
    public void registerTest() {
        assertDoesNotThrow(() -> facade.register("bob", "bob", "bob"));

        assertThrows(Exception.class, () -> facade.register("", "", "dasfeasdfea"));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


}
