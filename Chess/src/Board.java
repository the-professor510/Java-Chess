import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.border.EmptyBorder;

public class Board {
    private final int HEIGTH = 8;
    private final int WIDTH = 8;

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
    private final int EMPTY = 0;
    private final int PAWN = 1;
    private final int KNIGHT = 2;
    private final int BISHOP = 3;
    private final int ROOK = 4;
    private final int QUEEN = 5;
    private final int KING = 6;

    private String FEN;
    private final String STARTINGFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private int[] board = new int[HEIGTH*WIDTH];

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
                } else if(numberPlaced < 8){
                    if( Character.isUpperCase(c)){
                        if(c == 'P'){
                            board[counter*8+(numberPlaced)] = (PAWN);
                        } else if (c == 'R') {
                            board[counter*8+(numberPlaced)] = (ROOK);
                        } else if (c == 'N') {
                            board[counter*8+(numberPlaced)] = (KNIGHT);
                        } else if (c == 'B') {
                            board[counter*8+(numberPlaced)] = (BISHOP);
                        } else if (c == 'Q') {
                            board[counter*8+(numberPlaced)] = (QUEEN);
                        } else if (c == 'K') {
                            board[counter*8+(numberPlaced)] = (KING);
                        } else {
                            //error the FEN given is not suitable, exit out of this 
                            System.out.println("Given FEN is not suitable on rank " + (counter+1) + ", not recognised white piece");
                            System.exit(0);
                        }
                        numberPlaced += 1;

                    // Checks if the character representsa black piece
                    } else if (Character.isLowerCase(c)){
                        if(c == 'p'){
                            board[counter*8+(numberPlaced)] = (-PAWN);
                        } else if (c == 'r') {
                            board[counter*8+(numberPlaced)] = (-ROOK);
                        } else if (c == 'n') {
                            board[counter*8+(numberPlaced)] = (-KNIGHT);
                        } else if (c == 'b') {
                            board[counter*8+(numberPlaced)] = (-BISHOP);
                        } else if (c == 'q') {
                            board[counter*8+(numberPlaced)] = (-QUEEN);
                        } else if (c == 'k') {
                            board[counter*8+(numberPlaced)] = (-KING);
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
    }

    public void updateFEN(){

        String generatedFEN = new String();

        int numBlank = 0;

        for(int i = 0; i<HEIGTH;i++){
            numBlank = 0;
            for(int j=0; j<WIDTH; j++){
                int tempSquare = board[i*HEIGTH+j];

                if(tempSquare != EMPTY){
                    if(numBlank != 0){
                        generatedFEN += numBlank;
                    }
                    numBlank = 0;
                    
                    switch (tempSquare) {
                        case PAWN:
                            generatedFEN += "P";
                            break;
                        case ROOK:
                            generatedFEN += "R";
                            break;
                        case KNIGHT:
                            generatedFEN += "N";
                            break;
                        case BISHOP:
                            generatedFEN += "B";
                            break;
                        case QUEEN:
                            generatedFEN += "Q";
                            break;
                        case KING:
                            generatedFEN += "K";
                            break;
                        case (-PAWN):
                            generatedFEN += "p";
                            break;
                        case (-ROOK):
                            generatedFEN += "r";
                            break;
                        case (-KNIGHT):
                            generatedFEN += "n";
                            break;
                        case (-BISHOP):
                            generatedFEN += "b";
                            break;
                        case (-QUEEN):
                            generatedFEN += "q";
                            break;
                        case (-KING):
                            generatedFEN += "k";
                            break;
                        default:
                            generatedFEN += "";
                    }
                } else {
                    numBlank +=1;
                }
            }
            if(numBlank != 0){
                generatedFEN += numBlank;
            }
            generatedFEN+= "/";
        }
        FEN = generatedFEN.substring(0, generatedFEN.length()-1);
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
        //want to find all of the pawns for the correct colour
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
                //able to make to make a move infront
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
            if(i%8 == 0) {
                if (input == WHITE) {
                    //check to the right
                    dSquare = i - (input*7);
                    if(input*board[dSquare] < 0) {
                        // we can take in this direction
                        move[1] = dSquare;
                        moves.add(move.clone());
                    }
                } else {
                    //check to the left
                    dSquare = i - (input*9);
                    if(input*board[dSquare] < 0) {
                        // we can take on this square
                        move[1] = dSquare;
                        moves.add(move.clone());
                    }
                }
            } else if(i%8 == 7) {
                if (input == BLACK) {
                    //check to the right
                    dSquare = i - (input*7);
                    if(input*board[dSquare] < 0) {
                        // we can take in this direction
                        move[1] = dSquare;
                        moves.add(move.clone());
                    }
                } else {
                    //check to the left
                    dSquare = i - (input*9);
                    if(input*board[dSquare] < 0) {
                        // we can take on this square
                        move[1] = dSquare;
                        moves.add(move.clone());
                    }
                }
            } else {
                //check to the right
                dSquare = i - (input*7);
                if(input*board[dSquare] < 0) {
                    // we can take in this direction
                    move[1] = dSquare;
                    moves.add(move.clone());
                }

                //check to the left
                dSquare = i - (input*9);
                if(input*board[dSquare] < 0) {
                    // we can take on this square
                    move[1] = dSquare;
                    moves.add(move.clone());
                }
            }
        }

        int size = moves.size();
        System.out.println("\n" + input);

        for(int i = 0; i< size; i++){
            System.out.print(Arrays.toString(moves.get(i)));
        }

        int[][] output = new int[size][2];
        for(int i= 0; i<size; i++) {
            output[i] = moves.get(i);
        }
        
        return output;
    }


    ///*
    // * inputs int[2]: [start square, destination square]
    // * outputs int[4]: [start square, desitination square, piece moved, piece at destination square]
    //*/

    public int[] makeMove(int[] inputs) {
        int start = inputs[0];
        int destination = inputs[1];

        int sSquare = board[start];
        int dSquare = board[destination];

        int pieceMoved = sSquare;
        int pieceAtDestination = dSquare;

        updateSquare(sSquare, EMPTY);
        updateSquare(dSquare, pieceMoved);

        int[] output = {start,destination,pieceMoved,pieceAtDestination};

        return output;
    }

    /*
     * inputs int[4]: [start square, desitination square, piece moved, piece at destination square]
     */
    public void unMakeMove(int[] inputs){
        int start = inputs[0];
        int destination = inputs[1];
        int pieceMoved = inputs[2];
        int pieceAtDestination = inputs[3];

        int sSquare = board[start];
        int dSquare = board[destination];

        updateSquare(sSquare, pieceMoved);
        updateSquare(dSquare, pieceAtDestination);
    }

    private void updateSquare(int square, int newPiece){
        square = (newPiece);
    }

    public void printBoard(){

        System.out.println("");
        String line = new String("");
        for(int i=0; i<8; i++){
            line = "";
            for(int j=0; j<8; j++){
                //System.out.println(board[8*i+j].getIsWhite());
                switch (board[8*i + j]) {
                    case EMPTY:
                        line = line + " . ";
                        break;
                    case PAWN:
                        line += " P ";
                        break;
                    case KNIGHT:
                        line += " N ";
                        break;
                    case BISHOP:
                        line += " B ";
                        break;
                    case ROOK:
                        line += " R ";
                        break;
                    case QUEEN:
                        line += " Q ";
                        break;
                    case KING:
                        line += " K ";
                        break;
                    case (-PAWN):
                        line += " p ";
                        break;
                    case (-KNIGHT):
                        line += " n ";
                        break;
                    case (-BISHOP):
                        line += " b ";
                        break;
                    case (-ROOK):
                        line += " r ";
                        break;
                    case (-QUEEN):
                        line += " q ";
                        break;
                    case (-KING):
                        line += " k ";
                        break;
                    default:
                        line += " . ";
                }
            }
            System.out.println(line);
        }
        System.out.println("");
    }
}



