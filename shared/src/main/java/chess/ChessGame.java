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

    private final HashMap<TeamColor, ChessPosition> king_pos;

    public ChessGame() {
        turn = TeamColor.WHITE; //first turn is always white.
        board = new ChessBoard();
        king_pos = new HashMap<>();
        king_pos.put(TeamColor.WHITE, null);
        king_pos.put(TeamColor.BLACK, null);
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

    private void findKings(){
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j ++){
                var piece = board.getPiece(new ChessPosition(i, j));
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (piece.getTeamColor() == TeamColor.WHITE) { //IntelliJ freaks out if I just put piece.getTeamColor() into the HashMap
                        this.king_pos.put(TeamColor.WHITE, new ChessPosition(i, j));
                    }else{
                        this.king_pos.put(TeamColor.BLACK, new ChessPosition(i, j));
                    }
                }
            }
        }
    }

    private Collection<ChessPosition> scanBoard(TeamColor teamColor){
        HashSet<ChessPosition> positions = new HashSet<>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j ++){
                var piece = board.getPiece(new ChessPosition(i, j));
                if(piece != null){
                    if(piece.getTeamColor() == teamColor){
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
        if(piece != null) {
            var color = piece.getTeamColor();
            if(!isInCheck(color) && piece.getPieceType() != ChessPiece.PieceType.KING) {
                return piece.pieceMoves(board, startPosition);
            }else{
                var valid_moves = new HashSet<ChessMove>();
                var old_board = this.getBoard();
                for(var move : piece.pieceMoves(old_board, startPosition)){
                    board.movePiece(move);
                    if(!isInCheck(color)){
                        valid_moves.add(move);
                    }
                    this.setBoard(old_board);
                }
                return valid_moves;
            }
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
        if(!validMoves(start).isEmpty()){
            var x = 5;
        }else{
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
        if(this.king_pos.get(teamColor) == null){
            this.findKings();
        }
        Collection<ChessPosition> enemy_pos;

        if(teamColor == TeamColor.WHITE){
            enemy_pos = this.scanBoard(TeamColor.BLACK);
        }else{
            enemy_pos = this.scanBoard(TeamColor.WHITE);
        }
        ChessPosition king_pos = this.king_pos.get(teamColor);

        if(!enemy_pos.isEmpty() && king_pos != null){
            for(var pos : enemy_pos){
                var possible_moves = board.getPiece(pos).pieceMoves(board, pos);
                if(possible_moves.contains(new ChessMove(pos, king_pos, null))
                || possible_moves.contains(new ChessMove(pos, king_pos, ChessPiece.PieceType.QUEEN))){ //pawns can always promote to a queen.
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
            var piece_pos = scanBoard(teamColor);
            for(ChessPosition pos : piece_pos){
                if(!validMoves(pos).isEmpty()){
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
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
