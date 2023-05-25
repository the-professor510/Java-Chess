public class Game {
    private final Board board;
    private boolean turn; // +ve for white, -ve for black
    private boolean won;

    public Game() {
        board = new Board();
        turn = true;
        won = false;
    }


    public String convertToNotation(int[] move){
        //takes a move in the representation that board uses it and converts it into common chess notation
        String notation = "";

        notation += squareNumbertoNotation(move[0]);

        if (move[4] != board.EMPTY) {
            notation += "x";
        }

        notation += squareNumbertoNotation(move[1]);

        //if (move[4])



        return notation;
    }

    public String squareNumbertoNotation(int square) {
        // takes a single square and returns the chess notation of it
        int file = square%board.WIDTH;
        int rank = (square - file)/ board.HEIGTH;

        String notation = "";

        switch (file){
            case 0:
                notation += "a";
                break;
            case 1:
                notation += "b";
                break;
            case 2:
                notation += "c";
                break;
            case 3:
                notation += "d";
                break;
            case 4:
                notation += "e";
                break;
            case 5:
                notation += "f";
                break;
            case 6:
                notation += "g";
                break;
            case 7:
                notation += "h";
                break;
            default:
                notation += "z";
                break;
        }

        //notation += (rank+1);

        switch (rank) {
            case 0:
                notation += "8";
                break;
            case 1:
                notation += "7";
                break;
            case 2:
                notation += "6";
                break;
            case 3:
                notation += "5";
                break;
            case 4:
                notation += "4";
                break;
            case 5:
                notation += "3";
                break;
            case 6:
                notation += "2";
                break;
            case 7:
                notation += "1";
                break;
            default:
                notation += "z";
                break;
        }
        return notation;
    }
}
