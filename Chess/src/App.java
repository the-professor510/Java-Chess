//import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Board board = new Board();
        //board.printBoard();

        //board.startingPosition();
        //board.printBoard();

        board.readInFEN("rnbqkbnr/pppppppp/8/8/4q3/7R/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());
        Move[] move = board.generateKnightMoves(1);
        for (Move moves : move) {
            System.out.println(moves.getStartSquare() + " " + moves.getDestinationSquare() );

        }
        System.out.println();


        for (Move moves: move) {
            try {
                System.out.println(moves.getStartSquare() + " " + moves.getDestinationSquare());
                board.makeMove(moves);
                board.updateFEN();
                board.printBoard();
                board.unMakeMove(moves);
                //System.out.println(tempMove.getDestinationPiece());
                board.updateFEN();
                //board.printBoard();
            } catch (NullPointerException e){
                System.out.println("NoMoves");
            }
        }


        /*
        int[] testing = {8,24};
        int[] move = board.makeMove(testing);

        System.out.println(testing[0]+" ,"+testing[1]);
        System.out.println(move[0] + " ," +move[1] + " ,"+move[2] + " ,"+move[3]);
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());

        board.unMakeMove(move);
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN()); */
        //int[][] moves = {{}};
        //int[] move = {};

        /*
        long epoch = System.currentTimeMillis();

        for(int i=0; i<100; i++) {
            board.generatePawnMoves(1);
            //board.generatePawnMoves(-1);

            board.generateKingMoves(1);
            //board.generateKingMoves(-1);

            board.generateRookMoves(1);
            //board.generateRookMoves(-1);

            board.generateBishopMoves(1);
            //board.generateBishopMoves(-1);

            board.generateQueenMoves(1);
            //board.generateQueenMoves(-1);

            board.generateKnightMoves(1);

            board.generateEnPassant(1);
        }
        long epoch2 = System.currentTimeMillis();
        //for(int[] i: moves) {
        //    System.out.println("[" + i[0]+ " ," + i[1] + "]");
        //}





        System.out.println("\nTime to run: " + (epoch2-epoch) + "ms");
        // */


        /*
        for(int i=0; i<64; i++) {
            System.out.println("\n");
            System.out.println(i);
            System.out.println(new Game().squareNumbertoNotation(i));
            System.out.println(board.isAttacked(1, i));
            System.out.println(board.isAttacked(-1, i));
        }*/

        //board.startingPosition();

        //board.printBoard();
        
    }
}
