import java.util.Scanner;

public class Game {
    public final Board board;
    private int turn; // +ve for white, -ve for black
    private boolean won;

    private int numberOfMoves = 0;

    public Game() {
        board = new Board();
        turn = 1;
        won = false;
    }

    //Play a local 2 Player game between black and white
    public void PlayGame() {

        resetBoard();
        Move[] moves = board.generateMoves(turn);
        String moveList = "";

        Scanner reader = new Scanner(System.in);
        String s1 = null;

        while (moves.length > 0 && board.halfMove <50) {
            board.printBoard();
            System.out.println(listMoves(moves));


            int num = -1;

            try {
                //try to execute the folowing lines
                System.out.println("Enter an int between 0 and " + moves.length + " to select the move from the above list you would like to play");
                System.out.print("Enter you int: ");

                s1 = reader.nextLine();
                num = Integer.parseInt(s1);


                //If everything went fine, break the loop and move on.
                //break;

            } catch (NumberFormatException e) {

                //If the method Integer.parseInt throws the exception, catch and print the message.
                System.out.println("Not a valid input, please try again.");

            }

            //reader.close();

            while (num < 0 || num > moves.length) {
                //reader = new Scanner(System.in);
                s1 = null;

                try {
                    //try to execute the folowing lines
                    System.out.print("Enter you int: ");

                    s1 = reader.nextLine();
                    num = Integer.parseInt(s1);


                    //If everything went fine, break the loop and move on.
                    //break;
                } catch (NumberFormatException e) {
                    //If the method Integer.parseInt throws the exception, catch and print the message.
                    System.out.println("Not a valid input, please try again.");
                }
                //reader.close();
            }

            board.makeMove(moves[num]);

            turn = -turn;

            moves = board.generateMoves(turn);
        }

        board.printBoard();

        if (board.isInCheck(1)) {
            System.out.print("Black Wins");
        } else if(board.isInCheck(-1)){
            System.out.print("White Wins");
        } else{
            System.out.print("Stalemate");
        }
    }

    public void resetBoard(){
        board.readInFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public void playAgainstRandMoveOpponent(int colour) {

        if(colour == -1){
            board.printBoard();
            Move[] move = board.generateMoves(-colour);
            int randomNum = (int) (Math.random() * move.length);
            board.makeMove(move[randomNum]);
            System.out.println(convertToNotation(move[randomNum]));
            System.out.println("\n");
        }

        Move[] move = board.generateMoves(colour);
        if(move.length == 0){
            if(board.isInCheck(colour)){
                System.out.print("Opponent Wins");
            }else{
                System.out.print("Stalemate");
            }
            return;
        }

        Scanner reader = new Scanner(System.in);
        String s1 = null;

        while(!(won)){
            board.printBoard();
            System.out.println(listMoves(move));


            int num =-1;

            try {
                //try to execute the folowing lines
                System.out.println("Enter an int between 0 and " + move.length+ " to select the move from the above list you would like to play");
                System.out.print("Enter you int: ");

                s1 = reader.nextLine();
                num = Integer.parseInt(s1);


                //If everything went fine, break the loop and move on.
                //break;

            } catch (NumberFormatException e) {

                //If the method Integer.parseInt throws the exception, catch and print the message.
                System.out.println("Not a valid input, please try again.");

            }

            //reader.close();

            while(num<0 || num>move.length){
                //reader = new Scanner(System.in);
                s1 = null;

                try {
                    //try to execute the folowing lines
                    System.out.print("Enter you int: ");

                    s1 = reader.nextLine();
                    num = Integer.parseInt(s1);



                    //If everything went fine, break the loop and move on.
                    //break;
                } catch (NumberFormatException e) {
                    //If the method Integer.parseInt throws the exception, catch and print the message.
                    System.out.println("Not a valid input, please try again.");
                }
                //reader.close();
            }

            board.makeMove(move[num]);

            move = board.generateMoves(-colour);
            if(move.length == 0){
                if(board.isInCheck(-colour)){
                    System.out.print("Player Wins");
                }else{
                    System.out.print("Stalemate");
                }
                return;
            }

            board.printBoard();
            move = board.generateMoves(-colour);
            int randomNum = (int) (Math.random() * move.length);
            board.makeMove(move[randomNum]);
            System.out.println(convertToNotation(move[randomNum]));
            System.out.println("\n");


            move = board.generateMoves(colour);
            if(move.length == 0){
                if(board.isInCheck(colour)){
                    System.out.print("Opponent Wins");
                }else{
                    System.out.print("Stalemate");
                }
                return;
            }

        }
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

    public void listPerft(String FEN, int depth, int colour){
        board.readInFEN(FEN);
        //board.printBoard();

        board.printBoard();

        long epoch = System.currentTimeMillis();
        long numMoves = perft(depth, board, colour, 0);
        long epoch2 = System.currentTimeMillis();


        System.out.println("Total time: " + (epoch2-epoch) + "ms");
        System.out.println("Total: " + numMoves);
        //board.printBoard();

        Move[] move = board.generateMoves(colour);
        //System.out.println(move.length);
        for (Move moves : move) {
            if(board.makeMove(moves)) {
                System.out.println(convertToNotation(moves) + ": " + perft(depth - 1, board, -colour, 0));
                //board.printBoard();
                //System.out.println(board.getFEN());
                board.unMakeMove(moves);
            }
            //System.out.println(moves.getStartSquare());
            //System.out.println(moves.getDestinationPiece());
            //board.printBoard();
            //System.out.println(board.getFEN());
            //System.out.println(moves.getPromotionPiece());
        }

    }

    public int perft(int depth, Board board, int turn, int counter){
        if (depth == 0) {
            return (counter+1);
        }

        //System.out.println("runs");
        //System.out.println(depth);
        if(depth > 0){
            //gen moves, play them and then call this method with plus 1 to depth and invert the turn
            Move[] move = board.generateMoves(turn);
            for(Move singelMove: move){
                if(board.makeMove(singelMove)) {
                    //numberOfMoves +=1;
                    //board.printBoard();
                    counter = perft(depth - 1, board, -turn, counter);
                    //board.printBoard();

                    //System.out.println(counter);
                    board.unMakeMove(singelMove);
                }
            }
        }
        return counter;
    }

    public String listMoves(Move[] moves){
        StringBuilder moveList = new StringBuilder();

        for(Move move: moves){
            moveList.append(convertToNotation(move)).append(" ");
        }
        return moveList.toString();
    }


    public String convertToNotation(Move move){
        //takes a move in the representation that board uses it and converts it into common chess notation
        String notation = "";

        notation += squareToNotation(move.getStartSquare());


        notation += squareToNotation(move.getDestinationSquare());

        switch (move.getColourToPlay()*move.getPromotionPiece()) {
            case 2 -> notation += "n";
            case 3 -> notation += "b";
            case 4 -> notation += "r";
            case 5 -> notation += "q";
            default -> notation += "";
        }

        //if (move[4])



        return notation;
    }

    public String squareToNotation(int square) {
        // takes a single square and returns the chess notation of it
        int file = square%board.WIDTH;
        int rank = (square - file)/ board.HEIGHT;

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
