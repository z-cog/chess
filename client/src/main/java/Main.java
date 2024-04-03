import clientSoftware.ChessREPL;

public class Main {
    public static void main(String[] args) {
        var repl = new ChessREPL("http://localhost:" + "8080");
        repl.run();
    }
}