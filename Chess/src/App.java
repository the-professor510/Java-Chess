public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Board board = new Board();
        //board.printBoard();

        //board.startingPosition();
        //board.printBoard();

        board.readInFEN("rnbqkbnr/pppppppp/kN1R3Q/3pp3/8/PPPPPPPP/8/RNBQKBNR w KQkq - 0 1");
        board.printBoard();
        board.updateFEN();
        System.out.println(board.getFEN());

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
        System.out.println(board.getFEN());




        //board.startingPosition();
        //board.printBoard();
        
    }
}
