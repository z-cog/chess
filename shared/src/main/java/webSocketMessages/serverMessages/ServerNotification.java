package webSocketMessages.serverMessages;


public class ServerNotification extends ServerMessage {
    String message;

    public ServerNotification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
