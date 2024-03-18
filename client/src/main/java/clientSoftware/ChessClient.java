package clientSoftware;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_SCREEN;

public class ChessClient {
    private final ServerFacade server;
    private State state;

    private HashMap<Integer, String> games;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.state = State.PRELOGIN;
        this.games = new HashMap<>();
    }

    public String eval(String input) throws Exception {
        var tokens = input.toLowerCase().split(" ");
        var prompt = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (Objects.equals(prompt, "quit")) {
            return prompt;
        } else if (Objects.equals(prompt, "help")) {
            return help();
        } else {
            if (state == State.PRELOGIN) {
                return switch (prompt) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> "Unknown command! Type 'help' for list of commands.\n";
                };
            } else if (state == State.POSTLOGIN) {
                return switch (prompt) {
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    default -> "Unknown command! Type 'help' for list of commands.\n";
                };
            } else {
                System.out.print(ERASE_SCREEN);
                var board = new ChessBoard();
                board.resetBoard();
                ChessUI.printBoard(board, ChessGame.TeamColor.BLACK);
                ChessUI.printBoard(board, ChessGame.TeamColor.WHITE);
                return "quit";
            }
        }
    }

    private String register(String... params) throws Exception {
        if (params.length == 3) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws Exception {
        if (params.length == 2) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String logout() throws Exception {
        this.state = State.PRELOGIN;
        return "success!";
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 1) {
            System.out.print(Arrays.toString(params));
            this.state = State.GAMEPLAY;
            return "dfa";
        }
        throw new Exception("Expected: create <gameName>");
    }

    private String listGames() throws Exception {
        this.games.clear();
        var gameList = new String[]{"fea", "feaadsf", "dfea"};
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < gameList.length; i++) {
            this.games.put(i, gameList[i]);
            output.append(gameList[i]).append("\n");
        }
        return output.toString();
    }

    private String joinGame(String... params) throws Exception {
        if (params.length == 1) {
            System.out.print(Arrays.toString(params));
            this.state = State.GAMEPLAY;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            System.out.print(Arrays.toString(params));
            this.state = State.GAMEPLAY;

            return "Observing game ";
        }
        throw new Exception("Expected: observe <ID>");
    }

    private String help() {
        if (state == State.PRELOGIN) {
            return """
                    - login
                    - register
                    - help
                    - quit
                    """;
        } else if (state == State.POSTLOGIN) {
            return """
                    - logout
                    - create
                    - list
                    - join
                    - observe
                    - help
                    - quit
                    """;
        } else {
            return "- begin";
        }
    }
}
