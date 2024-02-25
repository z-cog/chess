package server;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
}
