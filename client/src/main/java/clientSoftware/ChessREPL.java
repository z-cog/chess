package clientSoftware;

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

        System.out.println(SET_BG_COLOR_BLACK);
        System.out.println(SET_TEXT_COLOR_BLUE);
        System.out.println("Hello welcom 2 the chess gayme");

        Scanner scanner = new Scanner(System.in);

        var prompt = "";

        while (!prompt.equals("quit")) {
            System.out.println();
            System.out.println(SET_BG_COLOR_BLACK);
            System.out.println(SET_TEXT_COLOR_GREEN);
            System.out.println(">>> ");

            prompt = scanner.nextLine();

            if (state == State.PRELOGIN) {
                switch (prompt) {
                    case "login" -> System.out.println("dasf");
                    case "register" -> System.out.println("dasadff");
                    default -> this.unknownCommand();
                }
                ;
            } else if (state == State.POSTLOGIN) {

            } else if (state == State.GAMEPLAY) {

            }
            if (Objects.equals(prompt, "help")) {

            }
        }
    }

    private void unknownCommand() {
        System.out.println(SET_TEXT_COLOR_RED);
        System.out.println("Unknown command!");
        System.out.println(SET_TEXT_COLOR_BLUE);
        System.out.println("type 'help' for list of commands.\n");
    }

}
