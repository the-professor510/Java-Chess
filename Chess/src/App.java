//import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();

        //game.testMoveGen();
        //game.resetBoard();
        //game.playAgainstRandMoveOpponent(-1);

        game.listPerft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, 1);
    }
}
