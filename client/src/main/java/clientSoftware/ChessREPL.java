package clientSoftware;

import chess.ChessGame;
import clientSoftware.webSocket.ServerMessageHandler;
import ui.ChessUI;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerNotification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessREPL implements ServerMessageHandler {
    private final ChessClient client;
    private ChessGame.TeamColor color;

    public ChessREPL(String serverUrl) {
        this.client = new ChessClient(serverUrl);
        this.color = ChessGame.TeamColor.WHITE;
    }

    public void run() {
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Welcome to the Chess Client!");

        Scanner scanner = new Scanner(System.in);

        var result = "";

        while (!result.equals("quit")) {
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

    public void notify(ServerMessage message) {
        var type = message.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            ChessGame game = ((LoadGame) message).getGame();
            ChessUI.printBoard(game.getBoard(), color);
        } else {
            var messageColor = SET_TEXT_COLOR_WHITE;
            if (type == ServerMessage.ServerMessageType.ERROR) {
                messageColor = SET_TEXT_COLOR_RED;
            }
            System.out.println(messageColor + ((ServerNotification) message).getMessage());
        }
    }

    public void setTeam(ChessGame.TeamColor color) {
        this.color = color;
    }
}
