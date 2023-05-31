public class Move {

    private final int startSquare;
    private final int destinationSquare;

    private final int destinationPiece;

    private final int enPassantSquare;
    private final int promotionPiece;

    private final int halfMove;
    private final int fullMove;

    private final boolean whiteKingCastle;
    private final boolean whiteQueenCastle;
    private final boolean blackKingCastle;
    private final boolean blackQueenCastle;

    private final int colourToPlay;

    public Move(int start, int destination, int dPiece, int ePSquare,int pPiece, int hm, int fm, boolean wkc, boolean wqc, boolean bkc, boolean bqc, int turn) {
        startSquare = start;
        destinationSquare = destination;
        destinationPiece = dPiece;
        enPassantSquare = ePSquare;
        promotionPiece = pPiece;
        halfMove = hm;
        fullMove = fm;
        whiteKingCastle = wkc;
        whiteQueenCastle = wqc;
        blackKingCastle = bkc;
        blackQueenCastle = bqc;
        colourToPlay = turn;
    }

    public int getStartSquare() {
        return startSquare;
    }
    public int getDestinationSquare() {
        return destinationSquare;
    }
    public int getDestinationPiece() {
        return destinationPiece;
    }
    public int getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getPromotionPiece() {
        return promotionPiece;
    }

    public int getHalfMove() {
        return halfMove;
    }
    public int getFullMove() {
        return fullMove;
    }

    public boolean isWhiteKingCastle() {
        return whiteKingCastle;
    }
    public boolean isWhiteQueenCastle() {
        return whiteQueenCastle;
    }
    public boolean isBlackKingCastle() {
        return blackKingCastle;
    }
    public boolean isBlackQueenCastle() {
        return blackQueenCastle;
    }
    public int getColourToPlay() {
        return colourToPlay;
    }
}
