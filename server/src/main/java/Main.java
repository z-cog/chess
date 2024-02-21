import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class Main {
    public static void main(String[] args) {
        var auth = new MemoryAuthDAO();
        var user = new MemoryUserDAO();
        var games = new MemoryGameDAO();
    }
}