package clientSoftware;

import chess.ChessGame;
import chess.ChessPosition;
import clientSoftware.webSocket.ServerMessageHandler;
import model.GameData;
import ui.ChessUI;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerNotification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageHandler {
    private final ServerFacade facade;
    private State state;
    private ChessGame.TeamColor color;
    private ChessGame currentGame;
    private final HashMap<Integer, GameData> games;

    public ChessClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
        this.state = State.PRELOGIN;
        this.games = new HashMap<>();
        this.color = null;
        this.currentGame = null;
    }

    public String eval(String input) throws Exception {
        var tokens = input.toLowerCase().split(" ");
        var prompt = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (Objects.equals(prompt, "quit")) {
            return prompt;
        } else if (Objects.equals(prompt, "help") || Objects.equals(prompt, "")) {
            return help();
        } else {
            if (state == State.PRELOGIN) {
                return switch (prompt) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "letitallburn" -> clear();
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
                return switch (prompt) {
                    case "redraw" -> redrawBoard();
                    case "highlight" -> highlight(params);
                    case "move" -> makeMove(params);
                    case "leave" -> leaveGame();
                    case "resign" -> resignGame();
                    default -> "Unknown command! Type 'help' for list of commands.\n";
                };
            }
        }
    }

    private String register(String... params) throws Exception {
        if (params.length == 3) {
            String message = facade.register(params[0], params[2], params[1]);
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
                this.color = color;
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

    private String redrawBoard() {
        var board = this.currentGame.getBoard();
        ChessUI.printBoard(board, this.color);
        return "";
    }

    private String highlight(String[] params) throws Exception {
        if (params.length == 1 && params[0].length() == 2) {
            var position = convertToPosition(params[0]);
            ChessUI.highlight(this.currentGame, position, this.color);
            return "";
        }
        throw new Exception("Expected: highlight <piecePosition>");
    }

    private String makeMove(String[] params) throws Exception {
        return "";
    }

    private String leaveGame() {
        return "";
    }

    private String resignGame() {
        return "";
    }

    private String clear() throws Exception {
        return facade.clear();
    }

    private ChessPosition convertToPosition(String string) throws Exception {
        int row = convertToInt(string.charAt(1));
        int col = convertToInt(string.charAt(0));
        return new ChessPosition(row, col);
    }

    private Integer convertToInt(Character c) throws Exception {
        if ((96 < c) && (c < 105)) {
            return c - 96;
        } else if ((64 < c) && (c < 73)) {
            return c - 64;
        } else if ((48 < c) && (c < 57)) {
            return c - 48;
        } else {
            throw new Exception("Invalid format for chess position\nExample: a1");
        }
    }


    private String help() {
        if (state == State.PRELOGIN) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
        } else if (state == State.POSTLOGIN) {
            return """
                    - logout
                    - create <gameName>
                    - list
                    - join <gameID> black|white
                    - observe <gameID>
                    - help
                    - quit
                    """;
        } else {
            return """
                    - redraw
                    - highlight <piecePosition>
                    - move <startPosition> <endPosition>
                    - leave
                    - resign
                    - help
                    - quit
                    """;
        }
    }

    public void notify(ServerMessage message) {
        var type = message.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            ChessGame game = ((LoadGame) message).getGame();
            this.currentGame = game;
            ChessUI.printBoard(game.getBoard(), color);
        } else {
            var messageColor = SET_TEXT_COLOR_WHITE;
            if (type == ServerMessage.ServerMessageType.ERROR) {
                messageColor = SET_TEXT_COLOR_RED;
            }
            System.out.println(messageColor + ((ServerNotification) message).getMessage());
        }
    }
}
