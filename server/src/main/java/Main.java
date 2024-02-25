import server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var myOBJ = new Scanner(System.in);
        var server = new Server();

        server.run(8080);

        String inputLmao = myOBJ.nextLine();
        System.out.print(inputLmao);
        server.stop();
    }
}