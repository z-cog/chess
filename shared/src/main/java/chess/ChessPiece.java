package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        switch(this.type){
            case KING:

                break;

            case QUEEN:
                break;

            case BISHOP:
                for(int i = 1; i<=8; i++){
                    if(((x-i) > 0) && ((y+i) <= 8)){
                        moves.add(new ChessMove(myPosition, new ChessPosition(y+i, x-i), null));
                    }
                    if(((x+i) <= 8) && ((y+i) <= 8)){
                        moves.add(new ChessMove(myPosition, new ChessPosition(y+i, x+i), null));
                    }
                    if(((x+i) <= 8) && ((y-i) > 0)){
                        moves.add(new ChessMove(myPosition, new ChessPosition(y-i, x+i), null));
                    }
                    if(((x-i) > 0) && ((y-i) > 0)){
                        moves.add(new ChessMove(myPosition, new ChessPosition(y-i, x-i), null));
                    }
                }
                break;

            case KNIGHT:
                break;

            case ROOK:
                for(int i = 1; i <= 8; i++) {
                    if(i != x){
                        ChessPosition col_iter = new ChessPosition(y, i);
                        moves.add(new ChessMove(myPosition, col_iter, null));
                    }
                    if(i != y){
                        ChessPosition row_iter = new ChessPosition(i, x);
                        moves.add(new ChessMove(myPosition, row_iter, null));
                    }
                }
                break;

            case PAWN:
                break;

            default:
                break;

        }
        return moves;
    }
}
