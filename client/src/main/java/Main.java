import chess.ChessGame;
import clientSoftware.ChessREPL;
import com.google.gson.Gson;
import webSocketMessages.userCommands.JoinPlayer;

public class Main {
    public static void main(String[] args) {
        var repl = new ChessREPL("http://localhost:" + "8080");
        repl.run();
    }
}