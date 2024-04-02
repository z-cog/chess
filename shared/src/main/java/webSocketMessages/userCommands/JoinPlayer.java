package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    protected CommandType commandType;
    private final int gameID;

    private final ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return this.gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return this.playerColor;
    }
}
