package clientSoftware;

import chess.ChessBoard;
import chess.ChessGame;
import ui.ChessUI;

import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_SCREEN;

public class ChessClient {
    private final ServerFacade server;
    private State state;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.state = State.PRELOGIN;
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
                    default -> "Unknown command! Type 'help' for list of commands.";
                };
            } else if (state == State.POSTLOGIN) {
                return switch (prompt) {
                    case "logout" -> logout();
                    case "create" -> createGame();
                    case "list" -> listGames();
                    case "join" -> joinGame();
                    case "observe" -> observeGame();
                    default -> "Unknown command! Type 'help' for list of commands.";
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

    private String logout(String... params) throws Exception {
        if (params.length == 2) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 2) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String listGames(String... params) throws Exception {
        if (params.length == 2) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String joinGame(String... params) throws Exception {
        if (params.length == 2) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            System.out.print(Arrays.toString(params));
            this.state = State.POSTLOGIN;
            return "dfa";
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
        } else {
            return """
                    - logout
                    - create
                    - list
                    - join
                    - observe
                    - help
                    - quit
                    """;
        }
    }
}
