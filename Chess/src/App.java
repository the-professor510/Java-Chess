public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        //game.PlayGame();

        //game.testMoveGen();
        //game.resetBoard();
        //game.playAgainstRandMoveOpponent(-1);
        long epoch = System.currentTimeMillis();

        game.listPerft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", 6, 1);
        game.board.printBoard();
        System.out.println(game.board.getFEN());

        long epoch2 = System.currentTimeMillis();
        System.out.println("It took " + (epoch2-epoch) + "ms");

        int total = 0;
        int numAverges =10;
        for (int i =0; i<10; i++) {
            epoch = System.currentTimeMillis();
            game.listPerft("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 5, 1);
            epoch2 = System.currentTimeMillis();

            total += (epoch2-epoch);
        }

        System.out.println("Average run took " + (total/numAverges) + "ms");

    }
}
