import server.Server;
import server.WebSocket.Connection;
import server.WebSocket.ConnectionManager;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {

//        var server = new Server();
//        var port = server.run(8080);
//        System.out.println("Started test HTTP server on " + port);
//
//        var scanner = new Scanner(System.in);
//        var yo = scanner.nextLine();
//
//        server.stop();

        var yo = new ConnectionManager();

        ConcurrentHashMap<Integer, HashMap<String, Boolean>> connections = new ConcurrentHashMap<>();
        connections.computeIfAbsent(5, k -> new HashMap<>());
        var curr = connections.get(5);

        curr.put("sdaf", true);
        curr.remove("sdaf");
    }
}