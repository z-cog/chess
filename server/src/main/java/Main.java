import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.ServicesDaemon;

public class Main {
    public static void main(String[] args) {
        var auth = new MemoryAuthDAO();
        var games = new MemoryGameDAO();
        var user = new MemoryUserDAO();

        var service = new ServicesDaemon(auth, games, user);
        try {

            String authToken = service.register("bob", "bob", "bpb");
            int gameID = service.createGame(authToken, "Bobb's");
            service.joinGame(authToken, gameID, ChessGame.TeamColor.WHITE);

        } catch (Exception e) {
            System.out.print("uh oh");
        }
    }
}