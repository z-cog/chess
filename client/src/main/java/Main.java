import chess.*;
import clientSoftware.ChessREPL;
import clientSoftware.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var repl = new ChessREPL("http://localhost:" + "8080");
        repl.run();
    }
}