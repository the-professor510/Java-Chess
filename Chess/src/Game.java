public class Game {
    private Board board;
    private boolean turn; // +ve for white, -ve for black
    private boolean won;

    public Game() {
        board = new Board();
        turn = true;
        won = false;
    }

        
}
