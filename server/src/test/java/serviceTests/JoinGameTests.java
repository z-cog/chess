package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BadRequestException;
import service.ServicesDaemon;
import service.UnauthorizedUserException;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTests {
    AuthDAO auth = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();
    UserDAO user = new MemoryUserDAO();
    ServicesDaemon service = new ServicesDaemon(auth, games, user);

    @BeforeEach
    public void init() {
        try {
            service.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void joinExistingGame() {
        try {
            String authToken = service.register("bob", "bob", "bpb");
            int gameID = service.createGame(authToken, "Bobb's");
            assertDoesNotThrow(() -> service.joinGame(authToken, gameID, ChessGame.TeamColor.WHITE));
            //Bob is playing alone :(
            assertDoesNotThrow(() -> service.joinGame(authToken, gameID, ChessGame.TeamColor.BLACK));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void joinNonexistentGame() {
        try {
            String authToken = service.register("bob", "bob", "bpb");
            assertThrows(BadRequestException.class, () -> service.joinGame(authToken, 3, ChessGame.TeamColor.WHITE));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void joinAsNullDoesNotCrash() {
        try {
            //Eventually, we will add the framework for joining as a spectator.
            String authToken = service.register("Rupert", "RupertDoesNotLikeBeingInAPassword", ":(");
            int gameID = service.createGame(authToken, "TheLandOfDore");
            assertDoesNotThrow(() -> service.joinGame(authToken, gameID, null));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}