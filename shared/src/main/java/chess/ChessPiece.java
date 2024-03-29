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
        for (int i = 0; i < 8; i++) {
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
     * @param startPos The pieces starting position.
     * @param dir      the pieces direction, 0-8. 0 is north, 1 is north-east, and so forth.
     * @param dist     How far in a given direction the piece will move.
     * @param board    A passed in board, size 8x8.
     * @param moves    A passed in hash set list of all moves generated by the function.
     */
    private void moveHelper(ChessPosition startPos, Integer dir, Integer dist, ChessBoard board, HashSet<ChessMove> moves) {
        int x = startPos.getColumn();
        int y = startPos.getRow();

        boolean logic;
        ChessPosition endPos = switch (dir) {
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
                yield startPos;
            }
        };
        if (logic) {
            if (board.getPiece(endPos) == null) {
                moves.add(new ChessMove(startPos, endPos, null));
            } else {
                this.blocked.replace(dir, true);

                if (board.getPiece(endPos).getTeamColor() != this.pieceColor) {
                    moves.add(new ChessMove(startPos, endPos, null));
                }
            }
        } else {
            this.blocked.replace(dir, true);
        }

    }

    /**
     * See above, but only for the knight.
     *
     * @param startPos where the piece begins.
     * @param dir      direction the piece should go.
     * @param board    the board (???).
     * @param moves    set of valid moves.
     */
    private void knightMoveHelper(ChessPosition startPos, int dir, ChessBoard board, Collection<ChessMove> moves) {
        int x = startPos.getColumn();
        int y = startPos.getRow();
        boolean logic;
        ChessPosition endPos = switch (dir) {
            case 0 -> {
                logic = (y + 2 <= 8) && (x - 1 > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y + 2, x - 1);
            }
            case 1 -> {
                logic = (y + 2 <= 8) && (x + 1 <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y + 2, x + 1);
            }
            case 2 -> {
                logic = (y + 1 <= 8) && (x + 2 <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y + 1, x + 2);
            }
            case 3 -> {
                logic = (y - 1 > 0) && (x + 2 <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y - 1, x + 2);
            }
            case 4 -> {
                logic = (y - 2 > 0) && (x + 1 <= 8) && !this.blocked.get(dir);
                yield new ChessPosition(y - 2, x + 1);
            }
            case 5 -> {
                logic = (y - 2 > 0) && (x - 1 > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y - 2, x - 1);
            }
            case 6 -> {
                logic = (y - 1 > 0) && (x - 2 > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y - 1, x - 2);
            }
            case 7 -> {
                logic = (y + 1 <= 8) && (x - 2 > 0) && !this.blocked.get(dir);
                yield new ChessPosition(y + 1, x - 2);
            }
            default -> {
                logic = false;
                yield startPos;
            }
        };
        if (logic) {
            if ((board.getPiece(endPos) == null || board.getPiece(endPos).getTeamColor() != this.pieceColor)) {
                moves.add(new ChessMove(startPos, endPos, null));
            }
        } else {
            this.blocked.replace(dir, true);
        }
    }

    /**
     * I'm so tired of writing doc strings :(
     * Promotes the pawn
     *
     * @param startPos where it is
     * @param pawnEnd  where it's going
     * @param pawnDir  its direction
     * @param moves    collection of valid moves
     */
    private void pawnPromotionManager(ChessPosition startPos, ChessPosition pawnEnd, int pawnDir, Collection<ChessMove> moves) {
        int y = startPos.getRow();
        if (y + pawnDir == 8 || y + pawnDir == 1) {
            moves.add(new ChessMove(startPos, pawnEnd, PieceType.QUEEN));
            moves.add(new ChessMove(startPos, pawnEnd, PieceType.KNIGHT));
            moves.add(new ChessMove(startPos, pawnEnd, PieceType.BISHOP));
            moves.add(new ChessMove(startPos, pawnEnd, PieceType.ROOK));
        } else {
            moves.add(new ChessMove(startPos, pawnEnd, null));
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

        for (int i = 0; i < 8; i++) {
            blocked.put(i, false);
        }

        HashSet<ChessMove> moves = new HashSet<>();

        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        switch (type) {
            case KING:
                for (int i = 0; i < 8; i++) {
                    moveHelper(myPosition, i, 1, board, moves);
                }
                break;
            case QUEEN:
                for (int d = 1; d < 8; d++) {
                    for (int i = 0; i < 8; i++) {
                        moveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case BISHOP:
                for (int d = 1; d < 8; d++) {
                    for (int i = 1; i < 8; i += 2) {
                        moveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case ROOK:
                for (int d = 1; d < 8; d++) {
                    for (int i = 0; i < 8; i += 2) {
                        moveHelper(myPosition, i, d, board, moves);
                    }
                }
                break;
            case KNIGHT:
                for (int i = 0; i < 8; i++) {
                    knightMoveHelper(myPosition, i, board, moves);
                }
                break;
            case PAWN:
                int pawnDir;
                ChessPosition pawnEnd;

                if (this.pieceColor == ChessGame.TeamColor.WHITE) {
                    pawnDir = 1;
                } else {
                    pawnDir = -1;
                }

                if ((y + pawnDir > 0) && (y + pawnDir <= 8)) {
                    pawnEnd = new ChessPosition(y + pawnDir, x);
                    if (board.getPiece(pawnEnd) == null) {
                        pawnPromotionManager(myPosition, pawnEnd, pawnDir, moves);
                        pawnEnd = new ChessPosition(y + 2 * pawnDir, x);
                        if (((y + 2 * pawnDir > 0) && (y + 2 * pawnDir <= 8)) && (y == 2 || y == 7) && (board.getPiece(pawnEnd) == null)) { //worst logic I have ever written, but it works!
                            moves.add(new ChessMove(myPosition, pawnEnd, null));
                        }
                    }
                }

                if (x + 1 <= 8) {
                    pawnEnd = new ChessPosition(y + pawnDir, x + 1);
                    if (board.getPiece(pawnEnd) != null && board.getPiece(pawnEnd).getTeamColor() != this.pieceColor) {
                        pawnPromotionManager(myPosition, pawnEnd, pawnDir, moves);
                    }
                }

                if (x - 1 > 0) {
                    pawnEnd = new ChessPosition(y + pawnDir, x - 1);
                    if (board.getPiece(pawnEnd) != null && board.getPiece(pawnEnd).getTeamColor() != this.pieceColor) {
                        pawnPromotionManager(myPosition, pawnEnd, pawnDir, moves);
                    }
                }
                break;
            default:
                break;
        }


        return moves;
    }
}

