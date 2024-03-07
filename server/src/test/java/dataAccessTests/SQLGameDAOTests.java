package dataAccessTests;

import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    @Test
    public void createGameTest() {
        try {
            var games = new SQLGameDAO();
            assertDoesNotThrow(() -> games.createGame("new game!"));

            //games with the same name will return unique ids.
            assertNotEquals(games.createGame("new new game!"), games.createGame("new new game!"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getGameTest() {
        try {
            var games = new SQLGameDAO();

            int specificID = games.createGame("bobzgame1");
            assertFalse(games.getGame(specificID).isEmpty());
            //nonexistent game.
            assertTrue(games.getGame(576).isEmpty());

            games.clear();
            //setting ID to -1 returns all games.
            games.createGame("bobzgame2");
            games.createGame("bobzgame3");
            games.createGame("bobzgame4");
            games.createGame("bobzgame5");
            assertEquals(4, games.getGame(-1).size());

            games.clear();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void clearGamesTest() {
        try {
            var games = new SQLGameDAO();
            assertDoesNotThrow(games::clear);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
