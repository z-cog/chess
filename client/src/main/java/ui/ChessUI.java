package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessUI {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = " ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        var board = new ChessBoard();
        board.resetBoard();
        board.addPiece(new ChessPosition(5, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        printBoard(board, ChessGame.TeamColor.BLACK);
        printBoard(board, ChessGame.TeamColor.WHITE);


    }

    public static void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawChessBoard(out, board, color);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor color) {
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
                if (squareColor) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS / 2));
                printPiece(out, board.getPiece(new ChessPosition(i, j)));
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
            case KING -> {
                output = "K";
            }
            case QUEEN -> {
                output = "Q";
            }
            case BISHOP -> {
                output = "B";
            }
            case KNIGHT -> {
                output = "N";
            }
            case ROOK -> {
                output = "R";
            }
            case PAWN -> {
                output = "P";
            }
            default -> {
                output = EMPTY;
            }
        }

        out.print(output);
    }

    static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}