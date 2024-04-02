package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    protected UserGameCommand.CommandType commandType;
    private final int gameID;
    private final ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }

    public int getGameID() {
        return this.gameID;
    }

    public ChessMove getMove() {
        return this.move;
    }
}
