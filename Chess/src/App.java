//import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Board board = new Board();
        //board.printBoard();

        //board.startingPosition();
        //board.printBoard();

        board.readInFEN("rnbqkbnr/pppppppp/8/7N/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());

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

        //board.generatePawnMoves(1);
        //board.generatePawnMoves(-1);

        //board.generateKingMoves(1);
        //board.generateKingMoves(-1);

        //board.generateRookMoves(1);
        //board.generateRookMoves(-1);

        //board.generateBishopMoves(1);
        //board.generateBishopMoves(-1);

        //board.generateQueenMoves(1);
        //board.generateQueenMoves(-1);

        board.generateKnightMoves(1);





        //board.startingPosition();
        //board.printBoard();
        
    }
}
