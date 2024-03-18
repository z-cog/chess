package clientSoftware;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessREPL {
    State state;
    ServerFacade server;

    public ChessREPL(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public void run() {
        this.state = State.PRELOGIN;
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Hello welcom 2 the chess gayme");

        Scanner scanner = new Scanner(System.in);

        var prompt = "";

        while (!prompt.equals("quit")) {
            System.out.print(SET_TEXT_COLOR_GREEN);
            System.out.print(">>> ");

            prompt = scanner.nextLine();
            if (Objects.equals(prompt, "help")) {
                System.out.print(SET_TEXT_COLOR_BLUE);
                System.out.println("Uhh idk lol");
            } else {
                if (state == State.PRELOGIN) {
                    switch (prompt) {
                        case "login" -> {
                            this.state = State.POSTLOGIN;
                            System.out.println("Dfaeadsf");
                        }
                        case "register" -> System.out.println("dasadff");
                        default -> this.unknownCommand();
                    }
                    ;
                } else if (state == State.POSTLOGIN) {
                    switch (prompt) {
                        case "login" -> this.state = State.GAMEPLAY;
                        case "register" -> System.out.println("dasadff");
                        default -> this.unknownCommand();
                    }
                    ;
                } else if (state == State.GAMEPLAY) {
                    prompt = "quit";

                    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
                    out.print(ERASE_SCREEN);
                    var board = new ChessBoard();
                    board.resetBoard();
                    ChessUI.printBoard(board, ChessGame.TeamColor.BLACK);
                    ChessUI.printBoard(board, ChessGame.TeamColor.WHITE);
                }
            }
        }

        System.out.println("Bye bye, thank you so much for to playing my game!!");
    }

    private void unknownCommand() {
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println("Unknown command!");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("type 'help' for list of commands.");
    }

}
