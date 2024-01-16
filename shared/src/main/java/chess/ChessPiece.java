package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    private HashMap<Integer, Boolean> dir_blocked; //for each direction, is there a path?
    /*
    7 0 1
    6 % 2  % = the piece, and the numbers dictate direction.
    5 4 3
     */

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.dir_blocked = new HashMap<Integer, Boolean>();
        for(int i = 0; i < 8; i++){
            this.dir_blocked.put(i, false);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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

    private void moveHelper(ChessPosition strt_pos, int dir, int dist, ChessBoard board, ArrayList<ChessMove> moves) {
        int x = strt_pos.getColumn();
        int y = strt_pos.getRow();

        boolean logic = false; //if dir is invalid, then the move is invalid as well
        ChessPosition end_pos = new ChessPosition(y,x); //If all else fails, it will try to move to its starting position
        if(dir==0){ //North
            logic = ((y + dist <= 8) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y + dist, x);

        }else if(dir==1){ //North-east
            logic = ((y + dist <= 8) && (x + dist <= 8) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y + dist, x + dist);


        }else if(dir==2){ //East
            logic = ((x + dist <= 8) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y, x + dist);


        }else if(dir==3){ //South-east
            logic = ((x + dist <= 8) && (y - dist > 0) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y - dist, x + dist);


        }else if(dir==4){ //South
            logic = ((y - dist > 0) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y - dist, x);


        }else if(dir==5){ //South-west
            logic = ((x - dist > 0) && (y - dist > 0) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y - dist, x - dist);


        }else if(dir==6){ //West
            logic = ((x - dist > 0) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y, x - dist);


        }else if(dir==7){ //North-west
            logic = ((y + dist <= 8) && (x - dist > 0) && !this.dir_blocked.get(dir));
            end_pos = new ChessPosition(y + dist, x - dist);


        }


        if(logic) {
            if (board.getPiece(end_pos) == null) {
                moves.add(new ChessMove(strt_pos, end_pos, null));
            } else {
                this.dir_blocked.replace(dir, true);
                if(board.getPiece(end_pos).pieceColor != this.pieceColor){
                    moves.add(new ChessMove(strt_pos, end_pos, null));
                }
            }
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        for(int i = 0; i < 8; i++){
            this.dir_blocked.replace(i, false);
        }

        ArrayList<ChessMove> moves = new ArrayList<>();

        switch(this.type){
            case KING:
                for(int i = 0; i < 8; i++) {
                    moveHelper(myPosition, i, 1, board, moves);
                }
                break;

            case QUEEN:
                for(int i = 1; i<=8; i++) {
                    for(int j = 0; j<8; j++) { //Sets direction to all.
                        moveHelper(myPosition, j, i, board, moves);
                    }
                }
                break;

            case BISHOP:
                for(int i = 1; i<=8; i++) {
                    for(int j = 1; j<8; j+=2) { //Sets direction to ne, se, sw, and nw
                        moveHelper(myPosition, j, i, board, moves);
                    }
                }
                break;

            case KNIGHT:
                break;

            case ROOK:
                for(int i = 1; i<=8; i++) {
                    for(int j = 0; j<8; j+=2) { //Sets direction to all.
                        moveHelper(myPosition, j, i, board, moves);
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
