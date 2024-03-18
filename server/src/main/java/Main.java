import server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        var server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        var scanner = new Scanner(System.in);
        var yo = scanner.nextLine();

        server.stop();

    }
}