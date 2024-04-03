package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private final int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = UserGameCommand.CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return this.gameID;
    }
}
