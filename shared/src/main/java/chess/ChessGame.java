package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    private final HashMap<TeamColor, ChessPosition> kingPos;

    private boolean gameOver;

    public ChessGame() {
        turn = TeamColor.WHITE; //first turn is always white.
        gameOver = false;
        board = new ChessBoard();
        kingPos = new HashMap<>();
        kingPos.put(TeamColor.WHITE, null);
        kingPos.put(TeamColor.BLACK, null);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private void findKings() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                var piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (piece.getTeamColor() == TeamColor.WHITE) { //IntelliJ freaks out if I just put piece.getTeamColor() into the HashMap
                        this.kingPos.put(TeamColor.WHITE, new ChessPosition(i, j));
                    } else {
                        this.kingPos.put(TeamColor.BLACK, new ChessPosition(i, j));
                    }
                }
            }
        }
    }

    private Collection<ChessPosition> scanBoard(TeamColor teamColor) {
        HashSet<ChessPosition> positions = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                var piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        positions.add(new ChessPosition(i, j));
                    }
                }
            }
        }
        return positions;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece != null) {
            var color = piece.getTeamColor();
            var validPieceMoves = new HashSet<ChessMove>();
            var oldBoard = new ChessBoard(this.board);
            var pieceMoves = piece.pieceMoves(oldBoard, startPosition);
            for (var move : pieceMoves) {
//                    System.out.println(board);
                this.board.movePiece(move);
                if (!isInCheck(color)) {
                    validPieceMoves.add(move);
                }
                this.setBoard(oldBoard);
            }
            return validPieceMoves;

        }
        return null;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var start = move.getStartPosition();
        if (board.getPiece(start).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It is not the pieces turn!");
        }
        var validMoves = validMoves(start);
        if (validMoves != null && validMoves.contains(move)) {
            if (gameOver) {
                throw new InvalidMoveException("The game is over!");
            }
            board.movePiece(move);
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Move is invalid!");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        findKings();

        Collection<ChessPosition> enemyPos;

        if (teamColor == TeamColor.WHITE) {
            enemyPos = this.scanBoard(TeamColor.BLACK);
        } else {
            enemyPos = this.scanBoard(TeamColor.WHITE);
        }
        ChessPosition currentKingPos = this.kingPos.get(teamColor);

        if (!enemyPos.isEmpty() && currentKingPos != null) {
            for (var pos : enemyPos) {
                var possibleMoves = board.getPiece(pos).pieceMoves(board, pos);
                if (possibleMoves.contains(new ChessMove(pos, currentKingPos, null))
                        || possibleMoves.contains(new ChessMove(pos, currentKingPos, ChessPiece.PieceType.QUEEN))) { //pawns can always promote to a queen.
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        var piecePos = scanBoard(teamColor);
        for (ChessPosition pos : piecePos) {
            var validPieceMoves = validMoves(pos);
            if (validPieceMoves != null && !validMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard(board); //Prevents pointer nonsense.
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public void setGameOver(boolean val) {
        gameOver = val;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }
}
