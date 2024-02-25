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
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return exceptionHandler(new Exception(msg), req, res);
        });

        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object exceptionHandler(Exception e, Request request, Response response) {
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
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request request, Response response) throws DataAccessException {
        service.clear();
        response.status(200);
        return "{}";
    }

    private Object register(Request request, Response response) throws BadRequestException, DataAccessException, UserTakenException {
        var requestObject = new Gson().fromJson(request.body(), RegisterRequest.class);
        String authToken = service.register(requestObject.username(), requestObject.password(), requestObject.email());
        response.status(200);
        return new Gson().toJson(Map.of("username", requestObject.username(), "authToken", authToken));
    }

    private Object login(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        var requestObject = new Gson().fromJson(request.body(), LoginRequest.class);
        String authToken = service.login(requestObject.username(), requestObject.password());
        response.status(200);
        return new Gson().toJson(Map.of("username", requestObject.username(), "authToken", authToken));
    }

    private Object logout(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        var authToken = new Gson().fromJson(request.headers("authorization"), String.class);
        service.logout(authToken);
        response.status(200);
        return "{}";
    }

    private Object listGames(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        var authToken = new Gson().fromJson(request.headers("authorization"), String.class);
        var gamesList = service.listGames(authToken);
        response.status(200);
        return new Gson().toJson(Map.of("games", gamesList));
    }

    private Object createGame(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedUserException {
        var authToken = new Gson().fromJson(request.headers("authorization"), String.class);
        var requestObject = new Gson().fromJson(request.body(), CreateGameRequest.class);
        int gameID = service.createGame(authToken, requestObject.gameName());
        response.status(200);
        return new Gson().toJson(Map.of("gameID", gameID));
    }

    private Object joinGame(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedUserException, UserTakenException {
        var authToken = new Gson().fromJson(request.headers("authorization"), String.class);
        var requestObject = new Gson().fromJson(request.body(), JoinGameRequest.class);
        service.joinGame(authToken, requestObject.gameID(), requestObject.playerColor());
        response.status(200);
        return "{}";
    }
}
