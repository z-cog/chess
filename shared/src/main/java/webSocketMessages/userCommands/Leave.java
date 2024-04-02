package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    protected UserGameCommand.CommandType commandType;
    private final int gameID;

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return this.gameID;
    }
}
