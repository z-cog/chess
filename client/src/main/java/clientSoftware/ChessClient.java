package clientSoftware;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import clientSoftware.webSocket.ServerMessageHandler;
import clientSoftware.webSocket.WebSocketFacade;
import model.GameData;
import ui.ChessUI;
import webSocketMessages.serverMessages.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageHandler {
    private final ServerFacade facade;
    private WebSocketFacade ws;
    private final String url;
    private State state;
    private ChessGame.TeamColor color;
    private ChessGame currentGame;
    private int gameID;
    private final HashMap<Integer, GameData> games;

    public ChessClient(String serverUrl) {
        this.facade = new ServerFacade(serverUrl);
        this.ws = null;
        this.url = serverUrl;
        this.state = State.PRELOGIN;
        this.color = null;
        this.currentGame = null;
        this.gameID = -3;
        this.games = new HashMap<>();
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
                this.gameID = gameID;
                this.ws = new WebSocketFacade(this.url, facade.authToken, this);
                ws.joinPlayer(gameID, color);
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
            this.color = ChessGame.TeamColor.WHITE;
            this.gameID = gameID;
            this.ws = new WebSocketFacade(this.url, facade.authToken, this);
            ws.joinObserver(gameID);
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
        if ((params.length == 2 || params.length == 3) && params[0].length() == 2 && params[1].length() == 2) {
            var start = convertToPosition(params[0]);
            var end = convertToPosition(params[1]);
            var move = getChessMove(params, start, end);
            ws.makeMove(this.gameID, move);
            return "";
        }
        throw new Exception("""
                Expected: move <startPosition> <endPosition> <promotionPiece>
                Where: promotionPiece = queen|knight|bishop|rook|none
                       default: none.""");
    }

    private static ChessMove getChessMove(String[] params, ChessPosition start, ChessPosition end) {
        ChessPiece.PieceType promotionPiece = null;

        if (params.length == 3) {
            switch (params[2]) {
                case "queen" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case "knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                case "rook" -> promotionPiece = ChessPiece.PieceType.ROOK;
                case "bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
            }
        }
        return new ChessMove(start, end, promotionPiece);
    }

    private String leaveGame() throws Exception {
        ws.leaveGame(gameID);
        this.color = null;
        this.currentGame = null;
        this.gameID = -3;
        this.state = State.POSTLOGIN;
        this.ws = null;
        return "Disconnected from websocket";
    }

    private String resignGame() throws Exception {
        ws.resignGame(gameID);
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
        System.out.println();
        var type = message.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            ChessGame game = ((LoadGame) message).getGame();
            this.currentGame = game;
            ChessUI.printBoard(game.getBoard(), color);
        } else if (type == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(SET_TEXT_COLOR_WHITE + ((ServerNotification) message).getMessage());
        } else {
            System.out.println(SET_TEXT_COLOR_RED + ((ServerErrorNotification) message).getMessage());
        }
        System.out.print(RESET_BG_COLOR);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(">>> ");
    }
}
