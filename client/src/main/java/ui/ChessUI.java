package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessUI {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = " ";

    public static void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawChessBoard(out, board, color, null, null);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

    }

    public static void highlight(ChessGame game, ChessPosition selectedPiece, ChessGame.TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        var moves = game.validMoves(selectedPiece);
        var board = game.getBoard();
        
        drawChessBoard(out, board, color, moves, selectedPiece);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor color, Collection<ChessMove> moves, ChessPosition selectedPiece) {
        String[] header = {EMPTY, "a", "b", "c", "d", "e", "f", "g", "h", EMPTY};
        if (color == ChessGame.TeamColor.BLACK) {
            header = new String[]{EMPTY, "h", "g", "f", "e", "d", "c", "b", "a", EMPTY};
        }

        printHeader(out, header);
        int begin = color == ChessGame.TeamColor.BLACK ? 1 : BOARD_SIZE_IN_SQUARES;
        int order = color == ChessGame.TeamColor.BLACK ? 1 : -1;

        boolean squareColor = true;


        for (int i = begin; (0 < i) && (i < BOARD_SIZE_IN_SQUARES + 1); i += order) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_TEXT_BOLD);

            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
            out.print(i);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));

            for (int j = 9 - begin; (0 < j) && (j < BOARD_SIZE_IN_SQUARES + 1); j += (-1) * order) {
                out.print(SET_TEXT_COLOR_GREEN);
                var currentSquare = new ChessPosition(i, j);
                if (currentSquare.equals(selectedPiece)) {
                    out.print(SET_BG_COLOR_YELLOW);
                } else {
                    boolean validMove = isValidMove(moves, selectedPiece, currentSquare);
                    if (squareColor) {
                        if (validMove) {
                            out.print(SET_BG_COLOR_GREEN);
                        } else {
                            out.print(SET_BG_COLOR_WHITE);
                        }
                    } else {
                        if (validMove) {
                            out.print(SET_BG_COLOR_DARK_GREEN);
                        } else {
                            out.print(SET_BG_COLOR_BLACK);
                        }
                    }
                }
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
                printPiece(out, board.getPiece(currentSquare));
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
                squareColor = !squareColor;
            }

            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_TEXT_BOLD);

            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
            out.print(i);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));

            squareColor = !squareColor;

            setBlack(out);
            out.println();
        }

        printHeader(out, header);
        out.println();
    }

    private static boolean isValidMove(Collection<ChessMove> moves, ChessPosition selectedPiece, ChessPosition currentSquare) {
        boolean validMove = false;
        if (moves != null) {
            validMove = (moves.contains(new ChessMove(selectedPiece, currentSquare, null)) ||
                    moves.contains(new ChessMove(selectedPiece, currentSquare, ChessPiece.PieceType.QUEEN)) ||
                    moves.contains(new ChessMove(selectedPiece, currentSquare, ChessPiece.PieceType.PAWN)) ||
                    moves.contains(new ChessMove(selectedPiece, currentSquare, ChessPiece.PieceType.ROOK)) ||
                    moves.contains(new ChessMove(selectedPiece, currentSquare, ChessPiece.PieceType.BISHOP)));
        }
        return validMove;
    }

    private static void printHeader(PrintStream out, String[] header) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);

        for (int i = 0; i < 10; i++) {
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
            out.print(header[i]);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
        }

        setBlack(out);
        out.println();
    }

    private static void printPiece(PrintStream out, ChessPiece piece) {
        if (piece == null) {
            out.print(EMPTY);
            return;
        }

        var type = piece.getPieceType();
        String output;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_MAGENTA);
        }

        switch (type) {
            case KING -> output = "K";
            case QUEEN -> output = "Q";
            case BISHOP -> output = "B";
            case KNIGHT -> output = "N";
            case ROOK -> output = "R";
            case PAWN -> output = "P";
            default -> output = EMPTY;
        }

        out.print(output);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}