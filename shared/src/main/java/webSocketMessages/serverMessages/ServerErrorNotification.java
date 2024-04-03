package webSocketMessages.serverMessages;

public class ServerErrorNotification extends ServerMessage {
    String errorMessage;

    public ServerErrorNotification(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}
