import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import clientSoftware.ChessREPL;
import ui.ChessUI;

public class Main {
    public static void main(String[] args) {
//        var repl = new ChessREPL("http://localhost:" + "8080");
//        repl.run();


        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
        ChessUI.printBoard(game.getBoard(), ChessGame.TeamColor.WHITE);

        ChessUI.highlight(game, new ChessPosition(2, 1), null);

    }
}