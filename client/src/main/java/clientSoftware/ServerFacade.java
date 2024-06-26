package clientSoftware;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {
    String authToken;
    String url;

    public ServerFacade(String serverUrl) {
        this.authToken = null;
        this.url = serverUrl;
    }

    public String register(String username, String email, String password) throws Exception {
        URI uri = new URI(url + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);

        var body = Map.of("username", username, "password", password, "email", email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var output = new Gson().fromJson(inputStreamReader, Map.class);
            this.authToken = (String) output.get("authToken");
            return "New user " + username + " successfully registered. Logged in.";
        }
    }

    public String login(String username, String password) throws Exception {
        URI uri = new URI(url + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);

        var body = Map.of("username", username, "password", password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var output = new Gson().fromJson(inputStreamReader, Map.class);
            this.authToken = (String) output.get("authToken");

            return "Logged in as " + username + ".";
        }
    }

    public String logout() throws Exception {
        URI uri = new URI(url + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var output = new Gson().fromJson(inputStreamReader, Map.class);
            this.authToken = (String) output.get("authToken");

            return "Logged out successfully.";
        }
    }

    public Object[] listGames() throws Exception {
        record ListGameResponse(GameData[] games) {
        }
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            var gameList = new Gson().fromJson(inputStreamReader, ListGameResponse.class);
            return gameList.games();
        }
    }

    public String createGame(String gameName) throws Exception {
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        var body = Map.of("gameName", gameName);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return "Created new game " + gameName + ".";
        }
    }

    public String joinGame(ChessGame.TeamColor playerColor, int gameID) throws Exception {
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        http.setDoOutput(true);

        http.addRequestProperty("authorization", authToken);

        var teamColor = (playerColor == null) ? "" : playerColor;
        var body = Map.of("playerColor", teamColor, "gameID", gameID);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            if (playerColor == null) {
                return "Successfully joined as observer.";
            } else {
                return "Successfully joined as " + playerColor + ".";
            }
        }
    }

    public String clear() throws Exception {
        URI uri = new URI(url + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        http.setDoOutput(false);

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            this.authToken = null;
            return "Database cleared.";
        }
    }
}
