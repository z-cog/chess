package webSocketMessages.serverMessages;


public class ServerNotification extends ServerMessage {
    String message;

    public ServerNotification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
