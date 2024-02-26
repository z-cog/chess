package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    public ChessBoard(ChessBoard other) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = other.getPiece(pos);
                if (piece != null) {
                    this.addPiece(pos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        String outString = "";
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) { //hehe, c++, hehehe
                outString += "|";
                if (squares[r][c] == null) {
                    outString += " ";
                } else {
                    switch (squares[r][c].getPieceType()) {
                        case KING:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "K";
                            } else {
                                outString += "k";
                            }
                            break;

                        case QUEEN:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "Q";
                            } else {
                                outString += "q";
                            }
                            break;

                        case BISHOP:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "B";
                            } else {
                                outString += "b";
                            }
                            break;

                        case KNIGHT:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "N";
                            } else {
                                outString += "n";
                            }
                            break;

                        case ROOK:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "R";
                            } else {
                                outString += "r";
                            }
                            break;

                        case PAWN:
                            if (squares[r][c].getTeamColor() == ChessGame.TeamColor.WHITE) {
                                outString += "P";
                            } else {
                                outString += "p";
                            }
                            break;

                        default:
                            outString += "#";
                            break;
                    }
                }
                outString += "|";
            }
            outString += "\n";
        }
        return outString;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Given a move, moves a piece.
     * note: checking for valid moves is done in ChessGame.
     *
     * @param move The move to make
     */
    public void movePiece(ChessMove move) {
        var piece = getPiece(move.getStartPosition());
        if (piece != null) {
            this.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() == null) {
                this.addPiece(move.getEndPosition(), piece);
            } else {
                this.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //White
        for (int i = 1; i < 9; i++) {
            this.addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        this.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));


        //Black
        for (int i = 1; i < 9; i++) {
            this.addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        this.addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

    }

}
