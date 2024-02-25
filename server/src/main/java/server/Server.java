package server;

import com.google.gson.Gson;
import dataAccess.*;
import service.BadRequestException;
import service.ServicesDaemon;
import service.UnauthorizedUserException;
import service.UserTakenException;
import spark.*;

import java.util.Map;

public class Server {
    AuthDAO auth = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();
    UserDAO user = new MemoryUserDAO();
    ServicesDaemon service = new ServicesDaemon(auth, games, user);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.delete("/db", this::clear);
        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(Exception e, Request request, Response response) {
        var body = new Gson().toJson(Map.of("message", String.format(e.getMessage()), "success", false));
        response.type("application/json");
        if (e.getClass() == BadRequestException.class) {
            response.status(400);
        } else if (e.getClass() == UnauthorizedUserException.class) {
            response.status(401);
        } else if (e.getClass() == UserTakenException.class) {
            response.status(403);
        } else {
            response.status(500);
        }

        response.body(body);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request request, Response response) throws DataAccessException {
        service.clear();
        response.status(200);
        return new Gson().toJson("");
    }
}
