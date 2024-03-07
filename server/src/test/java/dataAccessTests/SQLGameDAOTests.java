package dataAccessTests;

import chess.ChessGame;
import dataAccess.SQLGameDAO;
import model.GameData;
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
    public void updateGameTest() {
        try {
            var games = new SQLGameDAO();

            int specificID = games.createGame("bobzgame1");

            var gameCollection = games.getGame(specificID);
            GameData oldGame = (GameData) gameCollection.toArray()[0];
            games.updateGame(new GameData(specificID, "bob", "eric", oldGame.gameName(), oldGame.game()));

            assertEquals("bob", ((GameData) games.getGame(specificID).toArray()[0]).whiteUsername());
            assertEquals("eric", ((GameData) games.getGame(specificID).toArray()[0]).blackUsername());

            //update nonexistent game does not throw.
            assertDoesNotThrow(() -> games.updateGame(new GameData(45654, null, null, "fake game", new ChessGame())));

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
