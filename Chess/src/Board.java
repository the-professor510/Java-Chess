import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Board {
    public final int HEIGTH = 8;
    public final int WIDTH = 8;

    private final int WHITE = 1;
    private final int BLACK = -1;

    /*
    * -ve for black, +ve for white
    *
    * 0) Empty
    * 1) Pawn
    * 2) Kinght
    * 3) Bishop
    * 4) Rook
    * 5) Queen
    * 6) King
    */
    
    public final int EMPTY = 0;
    private final int PAWN = 1;
    private final int KNIGHT = 2;
    private final int BISHOP = 3;
    private final int ROOK = 4;
    private final int QUEEN = 5;
    private final int KING = 6;

    private String FEN;
    private final String STARTINGFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private int[] lastMove; //[start sqaure, destination square, piece moved, last piece moved]
    private boolean whitesTurn; // true, white 
    private boolean[] castling = new boolean[4]; //[W Queenside, W Kingside, B Queenside, B Kingside]
    private int enPassant; // square that a pawn may do en Passant on, -1 if no piece can move their
    private int halfmove; // last move since pawn advance or piece capture
    private int fullmove; // increases everytime black plays a move

    private final int NOENPASSANT = -1;

    private final int[] board = new int[HEIGTH*WIDTH];

    public final int ENPASSANTMOVE = 64;
    public final int WHITEQUEENSIDECASTLE = 65;
    public final int WHITEKINGSIDECASTLE = 66;
    public final int BLACKQUEENSIDECASTLE = 67;
    public final int BLACKINGSIDECASTLE = 68;
    
    

    public Board(){
        for(int i =0; i<64; i++) {
            board[i] = 0;
        }
    }

    public void startingPosition(){
        readInFEN(STARTINGFEN);
    }


    //FEN is separated into the 8 ranks
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPP/RNBGKBNR w KQkq - 0 1
    // BOARD SETUP, WHOSE TURN, CASTLING AVAILABILITY, LAST PAWN MOVE, FIFTY-MOVE RULE, FULL-MOVE NUMBER

    public void readInFEN(String newFEN){

        FEN = newFEN;

        Scanner scannerFEN = new Scanner(newFEN);

        //gets the board layout
        Scanner text = new Scanner(scannerFEN.next());

        text.useDelimiter("/");

        String rank;

        int counter = 0;
        int numberPlaced = 0;
        while(text.hasNext()){

            rank = text.next();
            numberPlaced = 0;

            for(int i=0; i< rank.length(); i++){
                char c = rank.charAt(i);

                //Checks if the character represents a blank square
                if(Character.isDigit(c)){

                    //Converts the character to an integer
                    int int1 = (c -'0');

                    if((numberPlaced+int1)<=8) {
                        for (int j = 0; j<int1;j++){
                            //make it a blank square for the next number of squares
                            board[counter*8+(numberPlaced)+j] = EMPTY;
                        }
                        numberPlaced += int1;        
                    } else {
                        //error, break out the program
                        System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", too long reduce empty squares");
                        System.exit(0);    
                    }

                    
                // Checks if the character represents a white piece
                } else if(numberPlaced < WIDTH){
                    if( Character.isUpperCase(c)){
                        if(c == 'P'){
                            board[counter*WIDTH+(numberPlaced)] = (PAWN);
                        } else if (c == 'R') {
                            board[counter*WIDTH+(numberPlaced)] = (ROOK);
                        } else if (c == 'N') {
                            board[counter*WIDTH+(numberPlaced)] = (KNIGHT);
                        } else if (c == 'B') {
                            board[counter*WIDTH+(numberPlaced)] = (BISHOP);
                        } else if (c == 'Q') {
                            board[counter*WIDTH+(numberPlaced)] = (QUEEN);
                        } else if (c == 'K') {
                            board[counter*WIDTH+(numberPlaced)] = (KING);
                        } else {
                            //error the FEN given is not suitable, exit out of this 
                            System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", not recognised white piece");
                            System.exit(0);
                        }
                        numberPlaced += 1;

                    // Checks if the character representsa black piece
                    } else if (Character.isLowerCase(c)){
                        if(c == 'p'){
                            board[counter*WIDTH+(numberPlaced)] = (-PAWN);
                        } else if (c == 'r') {
                            board[counter*WIDTH+(numberPlaced)] = (-ROOK);
                        } else if (c == 'n') {
                            board[counter*WIDTH+(numberPlaced)] = (-KNIGHT);
                        } else if (c == 'b') {
                            board[counter*WIDTH+(numberPlaced)] = (-BISHOP);
                        } else if (c == 'q') {
                            board[counter*WIDTH+(numberPlaced)] = (-QUEEN);
                        } else if (c == 'k') {
                            board[counter*WIDTH+(numberPlaced)] = (-KING);
                        } else {
                            //error the FEN given is not suitable, exit out of this
                            System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", not recognised black piece");
                            System.exit(0); 
                        }
                        numberPlaced += 1;
                    } else {
                        //error the FEN given is not suitable exit out of this
                        System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", not recognised upper or lower casing");
                        System.exit(0);
                    }
                } else {
                    //error the FEN given is not suitable exit out of this
                    System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", too long reduce final character");
                    System.exit(0);
                }
            }
        
            counter +=1;
        }

        //read in the rest of the fen
        
        // whose move
        String turn = scannerFEN.next();
        whitesTurn = turn.equalsIgnoreCase("w");

        // castling availability
        String castlingString = scannerFEN.next();
        for(int i=0; i< castlingString.length(); i++){
            char c = castlingString.charAt(i);

            //[W Queenside, W Kingside, B Queenside, B Kingside]
            if( c == 'Q') {
                castling[0] = true;
            } else if (c == 'K') {
                castling[1] = true;
            } else if (c == 'q') {
                castling[2] = true;
            } else if (c == 'k') {
                castling[3] = true;
            } else {
                for( int j = 0; j<castling.length; j++) {
                    castling[i] = false;
                }                
            }
        }

        // pawn move
        String pawnMove = scannerFEN.next().strip();

        
        char c = Character.toLowerCase(pawnMove.charAt(0));        

        if( c != '-' && pawnMove.length()>=2){

            int row = pawnMove.charAt(1) - '0';
            int column = -1;
            switch (c) {
                case 'a' -> column = 0;
                case 'b' -> column = 1;
                case 'c' -> column = 2;
                case 'd' -> column = 3;
                case 'e' -> column = 4;
                case 'f' -> column = 5;
                case 'g' -> column = 6;
                case 'h' -> column = 7;
                default -> {
                    enPassant = NOENPASSANT;
                    System.out.println(pawnMove);
                }
            }

            enPassant = row*WIDTH + column - WIDTH;
        } else {
            enPassant = NOENPASSANT;
        }
        



        // full and half moves
        String halfMoveString = scannerFEN.next();
        String fullMoveString = scannerFEN.next();

        try{
            halfmove = Integer.parseInt(halfMoveString);
            fullmove = Integer.parseInt(fullMoveString);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
            halfmove = 0;
            fullmove = 0;
        }

        scannerFEN.close();

    }

    public void updateFEN(){

        StringBuilder generatedFEN = new StringBuilder();

        int numBlank = 0;

        for(int i = 0; i<HEIGTH;i++){
            numBlank = 0;
            for(int j=0; j<WIDTH; j++){
                int tempSquare = board[i*HEIGTH+j];

                if(tempSquare != EMPTY){
                    if(numBlank != 0){
                        generatedFEN.append(numBlank);
                    }
                    numBlank = 0;

                    switch (tempSquare) {
                        case PAWN -> generatedFEN.append("P");
                        case ROOK -> generatedFEN.append("R");
                        case KNIGHT -> generatedFEN.append("N");
                        case BISHOP -> generatedFEN.append("B");
                        case QUEEN -> generatedFEN.append("Q");
                        case KING -> generatedFEN.append("K");
                        case (-PAWN) -> generatedFEN.append("p");
                        case (-ROOK) -> generatedFEN.append("r");
                        case (-KNIGHT) -> generatedFEN.append("n");
                        case (-BISHOP) -> generatedFEN.append("b");
                        case (-QUEEN) -> generatedFEN.append("q");
                        case (-KING) -> generatedFEN.append("k");
                        default -> {
                        }
                    }
                } else {
                    numBlank +=1;
                }
            }
            if(numBlank != 0){
                generatedFEN.append(numBlank);
            }
            generatedFEN.append("/");
        }
        generatedFEN = new StringBuilder(generatedFEN.substring(0, generatedFEN.length() - 1));

        // generate if it is whites turn
        if (whitesTurn) {
            generatedFEN.append(" w");
        } else {
            generatedFEN.append(" b");
        }


        //castling
        generatedFEN.append(" ");
        String castlingString = "";
        if(!castling[0] && !castling[1] && !castling[2] && !castling[3]) {
            castlingString = "-";
        } else {
            if (castling[1]) {
                castlingString += "K";
            }
            if (castling[0]) {
                castlingString += "Q";
            }
            if (castling[3]) {
                castlingString += "k";
            }
            if (castling[2]) {
                castlingString += "q";
            }
        } 
        generatedFEN.append(castlingString);

        //enPassant
        if (enPassant != NOENPASSANT) {
            //convert the integer into chess notation
            int column = enPassant%WIDTH;
            int row = (enPassant - column)/HEIGTH;

            switch (column) {
                case 0 -> generatedFEN.append(" a");
                case 1 -> generatedFEN.append(" b");
                case 2 -> generatedFEN.append(" c");
                case 3 -> generatedFEN.append(" d");
                case 4 -> generatedFEN.append(" e");
                case 5 -> generatedFEN.append(" f");
                case 6 -> generatedFEN.append(" g");
                case 7 -> generatedFEN.append(" h");
            }
            switch (row) {
                case 0 -> generatedFEN.append("8");
                case 1 -> generatedFEN.append("7");
                case 2 -> generatedFEN.append("6");
                case 3 -> generatedFEN.append("5");
                case 4 -> generatedFEN.append("4");
                case 5 -> generatedFEN.append("3");
                case 6 -> generatedFEN.append("2");
                case 7 -> generatedFEN.append("1");
            }
        } else {
            generatedFEN.append(" -");
        }

        //half move
        generatedFEN.append(" ").append(halfmove);

        //full move
        generatedFEN.append(" ").append(fullmove);

        FEN = generatedFEN.toString();

    }

    public String getFEN(){
        return FEN;
    }

    private int[] findPiece(int piece) {
        
        int[] location = new int[64];
        int counter = 0;

        for(int i=0; i<HEIGTH*WIDTH; i++){
            if(board[i] == piece) {
                location[counter] = i;
                counter +=1;
            }
        }

        return Arrays.copyOf(location,counter);
    }

    /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generatePawnMoves(int input) {
        //want to find all the pawns for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*PAWN);
        for(int i: positions){
            //check if 1 in front is possible, if this then check if two infront is possible

            move[0] = i;

            //forward moves
            dSquare = i-(input*WIDTH);
            if(board[dSquare] == EMPTY){
                //able to make a move infront
                move[1] = dSquare;
                moves.add(move.clone());



                //is it withing the respective starting squares
                if ( i < (36 + (input * 20))) {
                    if (i >= (28 + (input * 20))) {
                        dSquare = i-(input*WIDTH*2);
                        if(board[dSquare] == EMPTY) {
                            //able to make a double move
                            move[1] = dSquare;
                            moves.add(move.clone());
                        }
                    }
                }
            }

            //taking
            if(i%WIDTH == 0) {
                // we can take on this square
                if (input == WHITE) {
                    //check to the right
                    dSquare = i - (input*(WIDTH-1));
                } else {
                    //check to the left
                    dSquare = i - (input*(WIDTH+1));
                }
                if(input*board[dSquare] < EMPTY) {
                    // we can take in this direction
                    move[1] = dSquare;
                    moves.add(move.clone());
                }
            } else if(i%WIDTH == (WIDTH-1)) {
                // we can take on this square
                if (input == BLACK) {
                    //check to the right
                    dSquare = i - (input*(WIDTH-1));
                } else {
                    //check to the left
                    dSquare = i - (input*(WIDTH+1));
                }
                if(input*board[dSquare] < EMPTY) {
                    // we can take in this direction
                    move[1] = dSquare;
                    moves.add(move.clone());
                }
            } else {
                //check to the right
                dSquare = i - (input*(WIDTH-1));
                if(input*board[dSquare] < EMPTY) {
                    // we can take in this direction
                    move[1] = dSquare;
                    moves.add(move.clone());
                }

                //check to the left
                dSquare = i - (input*(WIDTH+1));
                if(input*board[dSquare] < EMPTY) {
                    // we can take on this square
                    move[1] = dSquare;
                    moves.add(move.clone());
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }

    /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generateKingMoves(int input) {

        //want to find all the kings for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*KING);

        for(int i: positions) {
            //check all 8 squares around the king
            move[0] = i;

            if(i%WIDTH == 0) {
                //on the left
                if( i == 0 ){
                    //top left
                    for (int x=0; x<=1;x++) {
                        for (int y = 0; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else if (i == (WIDTH*(WIDTH-1))) {
                    //bottom left
                    for (int x=0; x<=1;x++) {
                        for (int y = -1; y<=0; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else {
                    //left but check all other than those to the left
                    for (int x=0; x<=1;x++) {
                        for (int y = -1; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                }
            } else if (i%WIDTH == (WIDTH-1)) {
                //on the right
                if(i ==(WIDTH-1)) {
                    //top right
                    for (int x=-1; x<=0;x++) {
                        for (int y = 0; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else if(i==63) {
                    //bottom right
                    for (int x=-1; x<=0;x++) {
                        for (int y = -1; y<=0; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else {
                    //right but check all other than those to the left
                    for (int x=-1; x<=0;x++) {
                        for (int y = -1; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                }
            } else {
                if(i<WIDTH) {
                    //bottom but not on left or right
                    for (int x=-1; x<=1;x++) {
                        for (int y = 0; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else if (i>55) {
                    //top but not on left or right
                    for (int x=-1; x<=1;x++) {
                        for (int y = -1; y<=0; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                } else {
                    //not on any edge
                    for (int x=-1; x<=1;x++) {
                        for (int y = -1; y<=1; y++) {

                            dSquare = i + WIDTH*y + x;

                            if(input*board[dSquare] <= EMPTY) {
                                if (!(isAttacked(input, dSquare))){
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    }
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;

    }

    /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generateRookMoves(int input) {

        //want to find all of the rooks for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*ROOK);


        for(int i: positions){
            move[0] = i;

            //need to check along the 4 straights until we hit a friendly piece, an opponent, or the edge of the board
            // Above
            dSquare = i - WIDTH;
            while (dSquare > 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= WIDTH;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the left
            dSquare = i - 1;
            while (dSquare >= (i-(i%WIDTH))) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= 1;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the right
            dSquare = i + 1;
            while (dSquare < (i-(i%WIDTH)+WIDTH)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += 1;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the bottom
            dSquare = i + WIDTH;
            while (dSquare < 64) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += WIDTH;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }
        }

        int size = moves.size();
        
        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }

        /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generateBishopMoves(int input) {

        //want to find all of the rooks for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*BISHOP);

        for(int i: positions){
            move[0] = i;

            //need to check along the 4 straights until we hit a friendly piece, an opponent, or the edge of the board
            // up to left
            dSquare = i - WIDTH - 1;
            while (dSquare%WIDTH > 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= (1+WIDTH);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // up to right
            dSquare = i - WIDTH + 1;
            while (dSquare > 0 && (dSquare%WIDTH) < (WIDTH-1)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= (WIDTH-1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // down to right
            dSquare = i + WIDTH + 1;
            while (dSquare < 64 && (dSquare%WIDTH) < (WIDTH-1)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += (WIDTH +1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // down to left
            dSquare = i + WIDTH -1;
            while (dSquare < 64 && dSquare%WIDTH > 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += (WIDTH-1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }

        /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generateQueenMoves(int input) {

        //want to find all of the rooks for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*QUEEN);

        for(int i: positions){
            move[0] = i;

            //need to check along the 4 straights until we hit a friendly piece, an opponent, or the edge of the board
            // Above
            dSquare = i - WIDTH;
            while (dSquare > 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= WIDTH;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the left
            dSquare = i - 1;
            while (dSquare >= (i-(i%WIDTH))) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= 1;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the right
            dSquare = i + 1;
            while (dSquare < (i-(i%WIDTH)+WIDTH)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += 1;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // To the bottom
            dSquare = i + WIDTH;
            while (dSquare < 64) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += WIDTH;
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            //need to check along the 4 straights until we hit a friendly piece, an opponent, or the edge of the board
            // up to left
            dSquare = i - WIDTH - 1;
            while (dSquare%WIDTH >= 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= (1+WIDTH);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // up to right
            dSquare = i - WIDTH + 1;
            while (dSquare > 0 && (dSquare%WIDTH) < (WIDTH-1)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare -= (WIDTH-1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // down to right
            dSquare = i + WIDTH + 1;
            while (dSquare < 64 && (dSquare%WIDTH) < (WIDTH-1)) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += (WIDTH +1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }

            // down to left
            dSquare = i + WIDTH -1;
            while (dSquare < 64 && dSquare%WIDTH > 0) {
                //check to see if the destination square is able to be moved to
                if(board[dSquare] == EMPTY){
                    move[1] = dSquare;
                    moves.add(move.clone());
                    dSquare += (WIDTH-1);
                } else if (board[dSquare]*input < EMPTY) {
                    move[1] = dSquare;
                    moves.add(move.clone());
                    break;
                } else {
                    break;
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }

        /*
     * inputs: [player(white = 1, black = -1)]
     * outputs: [[start square, destination square]]
     */
    public int[][] generateKnightMoves(int input) {

        //want to find all of the rooks for the correct colour
        int dSquare;
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        int[] positions = findPiece(input*KNIGHT);

        for(int i: positions) {
            move[0] = i;
            for (int x = -1; x<=1; x+=2){
                for (int y = -2; y<=2; y+=4) {
                    dSquare = i + WIDTH*y + x;
                    //need to limit so that I only find if the dSquare is free when we haven't crossed over the board
                    if(dSquare%WIDTH > i%WIDTH ) {
                        if ( i%WIDTH == 0 && dSquare%WIDTH == (WIDTH-1)) {
                            continue;
                        } else {
                            if(dSquare>0 && dSquare<64) {
                                if(input*board[dSquare] <= EMPTY) {
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    } else if (dSquare%WIDTH < i%WIDTH ) {
                        if ( i%WIDTH == (WIDTH-1) && dSquare%WIDTH == 0) {
                            continue;
                        } else {
                            if(dSquare>0 && dSquare<64) {
                                if(input*board[dSquare] <= EMPTY) {
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    } else {
                        if(dSquare>0 && dSquare<64) {
                            if(input*board[dSquare] <= EMPTY) {
                                // we can take in this direction
                                move[1] = dSquare;
                                moves.add(move.clone());
                            }
                        }
                    }                    
                }
            }

            for (int x = -2; x<=2; x+=4){
                for (int y = -1; y<=1; y+=2) {
                    dSquare = i + WIDTH*y + x;

                    if(dSquare%WIDTH > i%WIDTH ) {
                        if ( i%WIDTH == 0 && dSquare%WIDTH == (WIDTH-2)) {
                            continue;
                        } else if ( i%WIDTH == 1 && dSquare%WIDTH == (WIDTH-1)) {
                            continue;
                        } else {
                            if(dSquare>0 && dSquare<64) {
                                if(input*board[dSquare] <= EMPTY) {
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    } else if (dSquare%WIDTH < i%WIDTH ) {
                        if ( i%WIDTH == (WIDTH-2) && dSquare%WIDTH == 0) {
                            continue;
                        } else if ( i%WIDTH == (WIDTH -1) && dSquare%WIDTH == 1) {
                            continue;
                        } else {
                            if(dSquare>0 && dSquare<64) {
                                if(input*board[dSquare] <= EMPTY) {
                                    // we can take in this direction
                                    move[1] = dSquare;
                                    moves.add(move.clone());
                                }
                            }
                        }
                    } else {
                        if(dSquare>0 && dSquare<64) {
                            if(input*board[dSquare] <= EMPTY) {
                                // we can take in this direction
                                move[1] = dSquare;
                                moves.add(move.clone());
                            }
                        }
                    }  
                    
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }


    public int[][] generateEnPassant(int input) {

        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        //check if we have a en passant move available
        if(enPassant == NOENPASSANT) {
            return new int[][]{{}};
        } else {
            move[1] = ENPASSANTMOVE;
            //check to see if there is a pawn in the position
            int sSquare = enPassant + input*(WIDTH+1);
            int row = (sSquare - sSquare%WIDTH)/WIDTH;
            if( row == ((enPassant - enPassant%WIDTH)/WIDTH+ input)) {
                if (board[sSquare] == input*PAWN) {
                    move[0] = sSquare;
                    moves.add(move.clone());
                }
            }

            sSquare = enPassant+ input*(WIDTH-1);
            row = (sSquare - sSquare%WIDTH)/WIDTH;
            if( row == ((enPassant - enPassant%WIDTH)/WIDTH+ input)) {
                if (board[sSquare] == input*PAWN) {
                    move[0] = sSquare;
                    moves.add(move.clone());
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }

        return output;
    }

    public int[][] generateCastling(int input) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] move = new int[2];

        // check to see if it is possible for castling

        if (input == WHITE){
            //check to see if white can castle
            if (castling[1]) {
                //check if between the king and kingside rook is clear
                // To the left

                // check if between the king and right rook is empty

                int startSquare = findPiece(input*KING)[0];
                //boolean canCastle;
                for (int i = startSquare+1; i<(WIDTH*WIDTH); i++){
                    if(board[i] != EMPTY || isAttacked(input,board[i])){
                        //canCastle = false;
                        break;
                    } else {
                        move[1] = WHITEKINGSIDECASTLE;
                        moves.add(move.clone());
                    }
                }
            }
            if (castling[2]) {
                // check if between the king and queenside rook is clear
                int startSquare = findPiece(input*KING)[0];
                for (int i = startSquare-1; i>(WIDTH*(WIDTH-1)+1); i--){
                    if(board[i] != EMPTY || isAttacked(input,board[i])){
                        break;
                    } else {
                        move[1] = WHITEQUEENSIDECASTLE;
                        moves.add(move.clone());
                    }
                }
            }
        } else {
            // check to see if black can castle
            if (castling[3]) {
                //check if between the king and kingside rook is clear
                int startSquare = findPiece(input*KING)[0];
                for (int i = startSquare+1; i<(WIDTH-1); i++){
                    if(board[i] != EMPTY || isAttacked(input,board[i])){
                        break;
                    } else {
                        move[1] = BLACKINGSIDECASTLE;
                        moves.add(move.clone());
                    }
                }
            }
            if (castling[4]) {
                // check if between the king and queenside rook is clear
                int startSquare = findPiece(input*KING)[0];
                for (int i = startSquare-1; i>0; i--){
                    if(board[i] != EMPTY || isAttacked(input,board[i])){
                        break;
                    } else {
                        move[1] = BLACKQUEENSIDECASTLE;
                        moves.add(move.clone());
                    }
                }
            }
        }

        int size = moves.size();

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }

        return output;
    }

    public void generateMoves(int input) {
        int[][] allMoves;

        int[][] pMoves = this.generatePawnMoves(input);
        int[][] rMoves = this.generateRookMoves(input);
        int[][] nMoves = this.generateKnightMoves(input);
        int[][] bMoves = this.generateBishopMoves(input);
        int[][] qMoves = this.generateQueenMoves(input);
        int[][] kMoves = this.generateKingMoves(input);
        int[][] ePMoves = this.generateEnPassant(input);
        int[][] castlingMoves = this.generateCastling(input);

    }

    /*
     * checks if this square would put the king in check, i.e. if this square is under attack from the other player
     * inputs: int colour - the colour of the king which we want to check if it would be in check
     *         int square - the square of the piece which we want to check if it would be in check
     * outputs: boolean - if the king would be in check in this position
     */
    public boolean isAttacked (int colour, int square) {
        //check around this square if it is attacked by any pieces

        int dSquare = 0;
        
        //can we be taken by a knight
        for (int x = -1; x<=1; x+=2) {
            for (int y = -2; y <= 2; y += 4) {
                dSquare = square + WIDTH * y + x;
                //need to limit so that I only find if the dSquare is free when we haven't crossed over the board

                if (dSquare > 0 && dSquare < 64) {
                    // the destination square is inside the board
                    // and means we don't have to check if the square is at the top or bottom
                    if (square % WIDTH == 0) {
                        // we are on the left edge
                        if (x < 0) {
                            continue;
                        } else {
                            if (board[dSquare] == -colour * KNIGHT) {
                                // we can be taken in this direction
                                return true;
                            }
                        }
                    } else if (square % WIDTH == WIDTH-1) {
                        //we are on the right edge
                        if (x > 0) {
                            continue;
                        } else {
                            if (board[dSquare] == -colour * KNIGHT) {
                                // we can be taken in this direction
                                return true;
                            }
                        }
                    } else {
                        //we are in the centre of the board
                        if (board[dSquare] == -colour * KNIGHT) {
                            // we can be taken in this direction
                            return true;
                        }
                    }
                }
            }
        }



        for (int x = -2; x<=2; x+=4){
            for (int y = -1; y<=1; y+=2) {

                dSquare = square + WIDTH*y + x;


                if (dSquare > 0 && dSquare < 64) {
                    // the destination square is inside the board
                    // and means we don't have to check if the square is at the top or bottom
                    if (square % WIDTH <= 1) {
                        // we are on the left edge
                        if (x < 0) {
                            continue;
                        } else {
                            if (board[dSquare] == -colour * KNIGHT) {
                                // we can be taken in this direction
                                return true;
                            }
                        }
                    } else if (square % WIDTH >= WIDTH-2) {
                        //we are on the right edge
                        if (x > 0) {
                            continue;
                        } else {
                            if (board[dSquare] == -colour * KNIGHT) {
                                // we can be taken in this direction
                                return true;
                            }
                        }
                    } else {
                        //we are in the centre of the board
                        if (board[dSquare] == -colour * KNIGHT) {
                            // we can be taken in this direction
                            return true;
                        }
                    }
                }
            }
        }


        //can we be taken on the diagonals and to the side

        // From above
        dSquare = square - WIDTH;
        while (dSquare > 0) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*ROOK || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare -= WIDTH;
        }

        // To the left
        dSquare = square - 1;
        while (dSquare >= (square-(square%WIDTH))) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*ROOK || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare -= 1;
        }

        // To the right
        dSquare = square + 1;
        while (dSquare < (square-(square%WIDTH)+WIDTH)) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*ROOK || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare += 1;
        }

        // To the bottom
        dSquare = square + WIDTH;
        while (dSquare < 64) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*ROOK || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare += WIDTH;
        }

        //need to check along the 4 straights until we hit a friendly piece, an opponent, or the edge of the board
        // up to right
        dSquare = square - WIDTH + 1;
        while (dSquare > 0 && (dSquare%WIDTH) < (WIDTH-1)) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*BISHOP || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare -= (WIDTH-1);
        }

        // up to left
        dSquare = square - WIDTH - 1;
        while (dSquare%WIDTH > 0) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*BISHOP || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare -= (WIDTH+1);
        }

        // down to left
        dSquare = square + WIDTH - 1;
        while (dSquare < 64 && dSquare%WIDTH > 0) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*BISHOP || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare += (WIDTH -1);
        }

        // down to right
        dSquare = square + WIDTH +1;
        while (dSquare < 64 && (dSquare%WIDTH) < (WIDTH-1)) {
            //check to see if the destination square is able to be moved to
            if (board[dSquare] == -colour*BISHOP || board[dSquare] == -colour*QUEEN) {
                return true;
            } else if(board[dSquare] != EMPTY){
                break;
            }
            dSquare += (WIDTH+1);
        }

        //check to see if a pawn can take

        if(square%WIDTH == 0) {
            // we are on the left of the board
            if (colour == WHITE) {
                //check to the right
                dSquare = square - (colour*(WIDTH-1));
            } else {
                //check to the left
                dSquare = square - (colour*(WIDTH+1));
            }
            if (dSquare > 0 && dSquare < 64) {
                if (-colour * board[dSquare] == PAWN) {
                    // we can take in this direction
                    return true;
                }
            }
        } else if(square%WIDTH == WIDTH-1) {
            // we are on the right of the board
            if (colour == BLACK) {
                // check to the right
                dSquare = square - (colour * (WIDTH - 1));
            } else {
                // check to the left
                dSquare = square - (colour * (WIDTH + 1));
            }
            if (dSquare > 0 && dSquare < 64) {
                if (-colour * board[dSquare] == PAWN) {
                    // we can take in this direction
                    return true;
                }
            }
        } else {
            //check to the right
            dSquare = square - (colour*(WIDTH-1));
            if (dSquare > 0 && dSquare < 64) {
                if (-colour * board[dSquare] == PAWN) {
                    // we can take in this direction
                    return true;
                }
            }

            //check to the left
            dSquare = square - (colour*(WIDTH +1));
            if (dSquare > 0 && dSquare < 64) {
                if (-colour * board[dSquare] == PAWN) {
                    // we can take on this square
                    return true;
                }
            }
        }

        //check to see if the king can capture

        if(square%WIDTH == 0) {
            //on the left
            if( square == 0 ){
                //top left
                for (int x=0; x<=1;x++) {
                    for (int y = 0; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else if (square == WIDTH*(WIDTH-1)) {
                //bottom left
                for (int x=0; x<=1;x++) {
                    for (int y = -1; y<=0; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else {
                //left but check all other than those to the left
                for (int x=0; x<=1;x++) {
                    for (int y = -1; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            }
        } else if (square%WIDTH == (WIDTH-1)) {
            //on the right
            if(square == (WIDTH-1)) {
                //top right
                for (int x=-1; x<=0;x++) {
                    for (int y = 0; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else if(square== (WIDTH*WIDTH -1)) {
                //bottom right
                for (int x=-1; x<=0;x++) {
                    for (int y = -1; y<=0; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else {
                //right but check all other than those to the left
                for (int x=-1; x<=0;x++) {
                    for (int y = -1; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            }
        } else {
            if(square<WIDTH) {
                //top but not on left or right
                for (int x=-1; x<=1;x++) {
                    for (int y = 0; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else if (square> (WIDTH * (WIDTH-1) -1)) {
                //bottom but not on left or right
                for (int x=-1; x<=1;x++) {
                    for (int y = -1; y<=0; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            } else {
                //not on any edge
                for (int x=-1; x<=1;x++) {
                    for (int y = -1; y<=1; y++) {

                        dSquare = square + WIDTH*y + x;

                        if(-colour*board[dSquare] == KING) {
                            // we can take in this direction
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void updateLastMove(int[] move) {
        lastMove = move;
    }

    public int[] getLastMove() {
        return lastMove;
    }

    public void updateEnPassant(int square) {
        enPassant = square;
    }

    public int getEnPassant() {
        return enPassant;
    }

    ///*
    // * inputs int[2]: [start square, destination square]
    // * outputs int[4]: [start square, destination square, piece moved, piece at destination square]
    //*/

    public int[] makeMove(int[] inputs) {
        int start = inputs[0];
        int destination = inputs[1];

        int sSquare = EMPTY;
        int dSquare = EMPTY;

        switch (destination) {
            case ENPASSANTMOVE -> {
                //make an en passant move
                sSquare = board[start];
                dSquare = enPassant;
                updateSquare(start, EMPTY); //start square is empty
                updateSquare(start - WIDTH * (board[start] / Math.abs(board[start])), EMPTY); //square with pawn on it is made empty
                updateSquare(enPassant, sSquare); //square behind pawn is filled

                enPassant = NOENPASSANT;
            }
            //return new int[]{start, destination,sSquare,dSquare};
            case WHITEKINGSIDECASTLE -> {
                //make white king side castle
                updateSquare(start, EMPTY); //make king square empty
                updateSquare(WIDTH * WIDTH, EMPTY); // make rook square empty
                updateSquare(start + 2, KING); // move king two to the right
                updateSquare(start + 1, ROOK); // move rook two to the left
                if (castling[1]) {
                    sSquare = 1;
                }
                if (castling[2]) {
                    dSquare = 1;
                }

                castling[1] = false;
                castling[2] = false;

            }
            case WHITEQUEENSIDECASTLE -> {
                //make white king side castle
                updateSquare(start, EMPTY); //make king square empty
                updateSquare(WIDTH * (WIDTH - 1), EMPTY); // make rook square empty
                updateSquare(start - 2, KING); // move king two to the right
                updateSquare(start - 1, ROOK); // move rook two to the left

                if (castling[1]) {
                    sSquare = 1;
                }
                if (castling[2]) {
                    dSquare = 1;
                }

                castling[1] = false;
                castling[2] = false;
            }
            case BLACKINGSIDECASTLE -> {
                //make white king side castle
                updateSquare(start, EMPTY); //make king square empty
                updateSquare(WIDTH, EMPTY); // make rook square empty
                updateSquare(start + 2, -KING); // move king two to the right
                updateSquare(start + 1, -ROOK); // move rook two to the left

                if (castling[3]) {
                    sSquare = 1;
                }
                if (castling[4]) {
                    dSquare = 1;
                }

                castling[3] = false;
                castling[4] = false;
            }
            case BLACKQUEENSIDECASTLE -> {
                //make white king side castle
                updateSquare(start, EMPTY); //make king square empty
                updateSquare(0, EMPTY); // make rook square empty
                updateSquare(start - 2, -KING); // move king two to the right
                updateSquare(start - 1, -ROOK); // move rook two to the left

                if (castling[3]) {
                    sSquare = 1;
                }
                if (castling[4]) {
                    dSquare = 1;
                }

                castling[3] = false;
                castling[4] = false;
            }
            default -> {
                sSquare = board[start];
                dSquare = board[destination];
                updateSquare(start, EMPTY);
                updateSquare(destination, sSquare);

                //need to check if it means that castling cannot happen
                if(sSquare == KING) {
                    castling[0] = false;
                    castling[1] = false;
                } else if (sSquare == -KING) {
                    castling[3] = false;
                    castling[4] = false;
                }
            }
        }

        return new int[]{start,destination, sSquare, dSquare,};
    }

    /*
     * inputs int[4]: [start square, destination square, piece moved, piece at destination square]
     */
    public void unMakeMove(int[] inputs){
        int start = inputs[0];
        int destination = inputs[1];
        int pieceMoved = inputs[2];
        int pieceAtDestination = inputs[3];

        int sSquare = EMPTY;
        int dSquare = EMPTY;

        //NOT COMPLETE NEED GO
        switch (destination) {
            case ENPASSANTMOVE -> {
                //unmake an en passant move

                enPassant = pieceAtDestination; //for en passant pieceAtDestination the en Passant square

                updateSquare(start, pieceMoved); //start square is old pawn
                updateSquare(start - WIDTH * (board[start] / Math.abs(board[start])), -pieceMoved); //square with pawn on it is made empty
                updateSquare(enPassant, EMPTY); //square behind pawn is filled
            }
            //return new int[]{start, destination,sSquare,dSquare};
            case WHITEKINGSIDECASTLE -> {
                //unmake white king side castle
                updateSquare(start, KING); //make king square empty
                updateSquare(WIDTH * WIDTH, ROOK); // make rook square empty
                updateSquare(start + 2, EMPTY); // move king two to the right
                updateSquare(start + 1, EMPTY); // move rook two to the left

                if (pieceMoved == 1) {
                    castling[1] = true;
                }
                if (pieceAtDestination ==1) {
                    castling[2] = true;
                }
            }
            case WHITEQUEENSIDECASTLE -> {
                //unmake white king side castle
                updateSquare(start, KING); //make king square empty
                updateSquare(WIDTH * (WIDTH - 1), ROOK); // make rook square empty
                updateSquare(start - 2, EMPTY); // move king two to the right
                updateSquare(start - 1, EMPTY); // move rook two to the left

                if (pieceMoved == 1) {
                    castling[1] = true;
                }
                if (pieceAtDestination ==1) {
                    castling[2] = true;
                }
            }
            case BLACKINGSIDECASTLE -> {
                //unmake white king side castle
                updateSquare(start, -KING); //make king square empty
                updateSquare(WIDTH, -ROOK); // make rook square empty
                updateSquare(start + 2, EMPTY); // move king two to the right
                updateSquare(start + 1, EMPTY); // move rook two to the left

                if (pieceMoved == 1) {
                    castling[3] = true;
                }
                if (pieceAtDestination ==1) {
                    castling[4] = true;
                }
            }
            case BLACKQUEENSIDECASTLE -> {
                //unmake white king side castle
                updateSquare(start, -KING); //make king square empty
                updateSquare(0, -KING); // make rook square empty
                updateSquare(start - 2, EMPTY); // move king two to the right
                updateSquare(start - 1, EMPTY); // move rook two to the left

                if (pieceMoved == 1) {
                    castling[3] = true;
                }
                if (pieceAtDestination ==1) {
                    castling[4] = true;
                }
            }
            default -> {
                sSquare = board[start];
                dSquare = board[destination];

                updateSquare(sSquare, pieceMoved);
                updateSquare(dSquare, pieceAtDestination);

            }
        }
    }

    private void updateSquare(int square, int newPiece){
        board[square] = (newPiece);
    }

    public void printBoard(){

        System.out.println();
        StringBuilder line;
        for(int i=0; i<WIDTH; i++){
            line = new StringBuilder();
            for(int j=0; j<WIDTH; j++){
                switch (board[WIDTH * i + j]) {
                    case PAWN -> line.append(" P ");
                    case KNIGHT -> line.append(" N ");
                    case BISHOP -> line.append(" B ");
                    case ROOK -> line.append(" R ");
                    case QUEEN -> line.append(" Q ");
                    case KING -> line.append(" K ");
                    case (-PAWN) -> line.append(" p ");
                    case (-KNIGHT) -> line.append(" n ");
                    case (-BISHOP) -> line.append(" b ");
                    case (-ROOK) -> line.append(" r ");
                    case (-QUEEN) -> line.append(" q ");
                    case (-KING) -> line.append(" k ");
                    default -> line.append(" . ");
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }
}