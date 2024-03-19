package clientSoftware;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import ui.ChessUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static ui.EscapeSequences.ERASE_SCREEN;

public class ChessClient {
    private final ServerFacade facade;
    private State state;

    private final HashMap<Integer, GameData> games;

    public ChessClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
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
            String message = facade.register(params[0], params[1], params[2]);
            this.state = State.POSTLOGIN;
            return message;
        }
        throw new Exception("Expected: register <username> <password> <email>");
    }

    private String login(String... params) throws Exception {
        if (params.length == 2) {
            String message = facade.login(params[0], params[1]);
            this.state = State.POSTLOGIN;
            return message;
        }
        throw new Exception("Expected: login <username> <password>");
    }

    private String logout() throws Exception {
        String message = facade.logout();
        this.state = State.PRELOGIN;
        return message;
    }

    private String createGame(String... params) throws Exception {
        if (params.length == 1) {
            return facade.createGame(params[0]);
        }
        throw new Exception("Expected: create <gameName>");
    }

    private String listGames() throws Exception {
        this.games.clear();
        var gameList = facade.listGames();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < gameList.length; i++) {
            this.games.put(i, (GameData) gameList[i]);
            output.append(i).append(": ").append(((GameData) gameList[i]).gameName()).append("\n");
        }
        return output.toString();
    }

    private String joinGame(String... params) throws Exception {
        if (params.length == 2) {
            if (Objects.equals(params[1], "black") || Objects.equals(params[1], "white")) {
                var color = (params[1].equals("white")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                var gameID = games.get(Integer.parseInt(params[0])).gameID();
                String output = facade.joinGame(color, gameID);
                this.state = State.GAMEPLAY;
                return output;
            }
        }
        throw new Exception("Expected: join <gameID> black|white");
    }

    private String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            var gameID = games.get(Integer.parseInt(params[0])).gameID();
            String output = facade.joinGame(null, gameID);
            this.state = State.GAMEPLAY;
            return output;
        }
        throw new Exception("Expected: observe <ID>");
    }

    private String clear() throws Exception {
        return facade.clear();
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
