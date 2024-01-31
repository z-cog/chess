package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    private final HashMap<Integer, Boolean> blocked; //for each direction, is there a path?
    /*
    7 0 1
    6 % 2  % = the piece, and the numbers dictate direction.
    5 4 3
     */

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.blocked = new HashMap<>();
        for(int i = 0; i < 8; i++){
            this.blocked.put(i, false);
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

    /**
     * This function takes the following parameters, calculates legal moves, and adds them to an HashSet.
     *
     * @param start_pos The pieces starting position.
     * @param dir      the pieces direction, 0-8. 0 is north, 1 is north-east, and so forth.
     * @param dist     How far in a given direction the piece will move.
     * @param board    A passed in board, size 8x8.
     * @param moves    A passed in hash set list of all moves generated by the function.
     */
    private void MoveHelper(ChessPosition start_pos, Integer dir, Integer dist, ChessBoard board, HashSet<ChessMove> moves){
        int x = start_pos.getColumn();
        int y = start_pos.getRow();

        boolean logic;
        ChessPosition end_pos = switch (dir) {
            case 0 -> {
                logic = (y + dist <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y + dist, x);
            }
            case 1 -> {
                logic = (y + dist <= 8) && (x + dist <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y + dist, x + dist);
            }
            case 2 -> {
                logic = (x + dist <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y, x + dist);
            }
            case 3 -> {
                logic = (y - dist > 0) && (x + dist <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y - dist, x + dist);
            }
            case 4 -> {
                logic = (y - dist > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y - dist, x);
            }
            case 5 -> {
                logic = (y - dist > 0) && (x - dist > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y - dist, x - dist);
            }
            case 6 -> {
                logic = (x - dist > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y, x - dist);
            }
            case 7 -> {
                logic = (y + dist <= 8) && (x - dist > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y + dist, x - dist);
            }
            default -> {
                logic = false;
                yield start_pos;
            }
        };
        if(logic){
            if(board.getPiece(end_pos) == null){
                moves.add(new ChessMove(start_pos, end_pos, null));
            }else{
                this.blocked.replace(dir, true);

                if(board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                    moves.add(new ChessMove(start_pos, end_pos, null));
                }
            }
        }else{
            this.blocked.replace(dir, true);
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

        for(int i = 0; i<8;i++){
            blocked.put(i, false);
        }

        HashSet<ChessMove> moves = new HashSet<>();

        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        switch(type){
            case KING:
                for(int i = 0; i<8; i++){
                    MoveHelper(myPosition, i, 1, board, moves);
                }
                break;
            case QUEEN:
                for(int d = 1; d<8; d++) {
                    for (int i = 0; i < 8; i++) {
                        MoveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case BISHOP:
                for(int d = 1; d<8; d++) {
                    for (int i = 1; i < 8; i+=2) {
                        MoveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case ROOK:
                for(int d = 1; d<8; d++) {
                    for (int i = 0; i < 8; i+=2) {
                        MoveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case KNIGHT:
                ChessPosition end_pos;
                if(y+2 <= 8){
                    if(x+1 <= 8){
                        end_pos = new ChessPosition(y+2, x+1);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                    if(x-1 > 0){
                        end_pos = new ChessPosition(y+2, x-1);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }

                }

                if(y-2 > 0){
                    if(x+1 <= 8){
                        end_pos = new ChessPosition(y-2, x+1);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                    if(x-1 > 0){
                        end_pos = new ChessPosition(y-2, x-1);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                }

                if(x+2 <= 8){
                    if(y+1 <= 8){
                        end_pos = new ChessPosition(y+1, x+2);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                    if(y-1 > 0){
                        end_pos = new ChessPosition(y-1, x+2);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                }

                if(x-2 > 0){
                    if(y+1 <= 8){
                        end_pos = new ChessPosition(y+1, x-2);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                    if(y-1 > 0){
                        end_pos = new ChessPosition(y-1, x-2);
                        if(board.getPiece(end_pos) == null || board.getPiece(end_pos).getTeamColor() != this.pieceColor){
                            moves.add(new ChessMove(myPosition, end_pos, null));
                        }
                    }
                }
                break;
            case PAWN:
                int pawn_dir;
                ChessPosition pawn_end;

                if(this.pieceColor == ChessGame.TeamColor.WHITE){
                    pawn_dir = 1;
                }else{
                    pawn_dir = -1;
                }

                if((y+pawn_dir > 0) && (y+pawn_dir <= 8)){
                    pawn_end = new ChessPosition(y+pawn_dir, x);
                    if(board.getPiece(pawn_end) == null){
                        if(y+pawn_dir == 8 || y+pawn_dir == 1){
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.ROOK));
                        }else{
                            moves.add(new ChessMove(myPosition, pawn_end, null));
                        }

                        pawn_end = new ChessPosition(y+2*pawn_dir, x);
                        if(((y+2*pawn_dir > 0) && (y+2*pawn_dir <= 8)) && (y==2 || y==7) && (board.getPiece(pawn_end) == null)){ //worst logic I have ever written, but it works!
                            moves.add(new ChessMove(myPosition, pawn_end, null));
                        }
                    }
                }

                if(x+1 <= 8){
                    pawn_end = new ChessPosition(y+pawn_dir, x+1);
                    if(board.getPiece(pawn_end) != null && board.getPiece(pawn_end).getTeamColor() != this.pieceColor){
                        if(y+pawn_dir == 8 || y+pawn_dir == 1){
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.ROOK));
                        }else{
                            moves.add(new ChessMove(myPosition, pawn_end, null));
                        }
                    }
                }

                if(x-1 > 0){
                    pawn_end = new ChessPosition(y+pawn_dir, x-1);
                    if(board.getPiece(pawn_end) != null && board.getPiece(pawn_end).getTeamColor() != this.pieceColor){
                        if(y+pawn_dir == 8 || y+pawn_dir == 1){
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, pawn_end, PieceType.ROOK));
                        }else{
                            moves.add(new ChessMove(myPosition, pawn_end, null));
                        }
                    }
                }
                break;
            default:
                break;
        }


        return moves;
    }
}

