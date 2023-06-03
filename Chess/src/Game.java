public class Game {
    private final Board board;
    private int turn; // +ve for white, -ve for black
    private boolean won;

    private int numberOfMoves = 0;

    public Game() {
        board = new Board();
        turn = 1;
        won = false;
    }

    public void testMoveGen(){

        board.readInFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.readInFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());
        Move[] move = board.generateMoves(-1);
        System.out.println(move.length);
        for (Move moves : move) {
            System.out.println(convertToNotation(moves));
            //System.out.println(moves.getPromotionPiece());
        }


        /*KJH
        long epoch = System.currentTimeMillis();

        randMove(2,board,1);

        long epoch2 = System.currentTimeMillis();

        System.out.println("\nNumber of Moves Found: "+ numberOfMoves);
        System.out.println("\nTime to run: " + (epoch2-epoch) + "ms");


         epoch = System.currentTimeMillis();

        randMove(3,board,1);

         epoch2 = System.currentTimeMillis();

        System.out.println("\nNumber of Moves Found: "+ numberOfMoves);
        System.out.println("\nTime to run: " + (epoch2-epoch) + "ms");


         epoch = System.currentTimeMillis();

        randMove(4,board,1);

         epoch2 = System.currentTimeMillis();

        System.out.println("\nNumber of Moves Found: "+ numberOfMoves);
        System.out.println("\nTime to run: " + (epoch2-epoch) + "ms");*/

    }

    public void randMove(int depth, Board board, int turn){
        if(depth > 0){
            //gen moves, play them and then call this method with plus 1 to depth and invert the turn
            Move[] move = board.generateMoves(turn);
            for(Move singelMove: move){
                board.makeMove(singelMove);
                numberOfMoves +=1;
                randMove(depth-1, board, -turn);
                board.unMakeMove(singelMove);
            }
        }
    }


    public String convertToNotation(Move move){
        //takes a move in the representation that board uses it and converts it into common chess notation
        String notation = "";

        notation += squareToNotation(move.getStartSquare());

        if (move.getDestinationPiece() != board.EMPTY) {
            notation += "x";
        }

        notation += squareToNotation(move.getDestinationSquare());

        //if (move[4])



        return notation;
    }

    public String squareToNotation(int square) {
        // takes a single square and returns the chess notation of it
        int file = square%board.WIDTH;
        int rank = (square - file)/ board.HIEGHT;

        String notation = "";

        switch (file) {
            case 0 -> notation += "a";
            case 1 -> notation += "b";
            case 2 -> notation += "c";
            case 3 -> notation += "d";
            case 4 -> notation += "e";
            case 5 -> notation += "f";
            case 6 -> notation += "g";
            case 7 -> notation += "h";
            default -> notation += "z";
        }

        //notation += (rank+1);

        switch (rank) {
            case 0 -> notation += "8";
            case 1 -> notation += "7";
            case 2 -> notation += "6";
            case 3 -> notation += "5";
            case 4 -> notation += "4";
            case 5 -> notation += "3";
            case 6 -> notation += "2";
            case 7 -> notation += "1";
            default -> notation += "z";
        }
        return notation;
    }
}
