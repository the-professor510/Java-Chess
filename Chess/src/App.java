public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        //game.PlayGame();

        //game.testMoveGen();
        //game.resetBoard();
        //game.playAgainstRandMoveOpponent(-1);
        long epoch = System.currentTimeMillis();

        game.listPerft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, 1);
        game.board.printBoard();
        System.out.println(game.board.getFEN());

        long epoch2 = System.currentTimeMillis();

        System.out.println("It took " + (((epoch2-epoch)/1000)) + "s");
    }
}
