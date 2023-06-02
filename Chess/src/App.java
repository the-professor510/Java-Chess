//import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Board board = new Board();
        //board.printBoard();

        //board.startingPosition();
        //board.printBoard();

        board.readInFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());
        //Move[] move = board.generateMoves(1);
        //for (Move moves : move) {
            //System.out.println(moves.getStartSquare() + " " + moves.getDestinationSquare() );
            //System.out.println(moves.getPromotionPiece());

        //}


        long epoch = System.currentTimeMillis();

        App.randMove(0,board,1);

        long epoch2 = System.currentTimeMillis();


        System.out.println("\nTime to run: " + (epoch2-epoch) + "ms");
        
    }

    public static void randMove(int depth, Board board, int turn){
        if(depth < 6){
            //gen moves, play them and then call this method with plus 1 to depth and invert the turn
            Move[] move = board.generateMoves(turn);
            for(Move singelMove: move){
                board.makeMove(singelMove);
                randMove(depth+1, board, -turn);
                board.unMakeMove(singelMove);
            }
        }
    }
}
