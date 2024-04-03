package clientSoftware;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessREPL {
    private final ChessClient client;

    public ChessREPL(String serverUrl) {
        this.client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Welcome to the Chess Client!");

        Scanner scanner = new Scanner(System.in);

        var result = "";

        while (!result.equals("quit")) {
            System.out.print(RESET_BG_COLOR);
            System.out.print(SET_TEXT_COLOR_GREEN);
            System.out.print(">>> ");

            String prompt = scanner.nextLine();
            try {
                result = client.eval(prompt);
                System.out.print(SET_TEXT_COLOR_BLUE + result + "\n");
            } catch (Throwable e) {
                result = e.getMessage();
                System.out.print(SET_TEXT_COLOR_RED + result + "\n");
            }
        }
        System.out.println("Farewell!!");
    }
}
