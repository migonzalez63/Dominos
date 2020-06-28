package dominos;

import java.util.Deque;
import java.util.Scanner;

/**
 * ConsoleDisplay serves to create a console based version of our domino game
 * and allow us to parse input in order to play the game
 */
public class ConsoleDisplay{
    Scanner choiceScanner = new Scanner(System.in);
    String direction;
    Domino playedDomino;

    /**
     * Displays intro at beginning of game
     */
    public void introDisplay() {
        System.out.println("Welcome to a game of Domino!");
    }

    /**
     * Displays the current state of the game to the perspective of the currentPlayer
     * and will parse the input to allow the player to play the game
     * @param currentPlayer
     * @param board
     * @param boneyardSize
     * @return choice
     */
    public String turnDisplay(Player currentPlayer, Board board, int boneyardSize) {
        System.out.println();
        System.out.println("Boneyard has " + boneyardSize + " dominoes left");

        /*
         * If it is the human players turn, we will display the current state of the board
         * in order to allow the user to play the game. If is is the computer turn, we
         * will declare as such and let the computer play
         */
        if(!currentPlayer.isComputer()) {
            System.out.println("Current Board:");
            board.boardString();
            System.out.println();
            System.out.println(currentPlayer.getName() + "'s turn!");
            System.out.println(currentPlayer.getName() + "'s tray: " + currentPlayer.getTray().toString());
        } else {
            System.out.println("Computer has " + currentPlayer.getTray().size() + " dominoes left.");
            return "Computer's Turn";
        }

        /*
         *  Displays options that the player can choose from
         */
        System.out.println("What would you like to do?");
        System.out.println("[p] Play a Domino");
        System.out.println("[d] Draw from the Boneyard");
        System.out.println("[a] Pass turn");
        System.out.println("[q] Quit Game");

        /*
         * We will parse the input and check to see if it is any of the given option. If that is not
         * the case, we will prompt the user to make another choice and allow them to do so.
         * If it is a proper choice, we will send it to our GameManager fin order to process the play.
         */
        String choice = choiceScanner.nextLine().toLowerCase();

        while(!choice.equals("p") && !choice.equals("d") && !choice.equals("q") && !choice.equals("a")) {
            System.out.println("Invalid input. Please make another choice");
            choice = choiceScanner.nextLine();
        }

        return choice;
    }

    /**
     * Displays what the computer player has done during their turn
     * @param state
     */
    public void computerTurn(String state) {
        switch (state){
            case "played":
                System.out.println();
                System.out.println("Computer has played a domino");
                break;
            case "draw":
                System.out.println();
                System.out.println("Computer has drawn from the Boneyard");
                break;
            case "passed":
                System.out.println();
                System.out.println("Computer has passed its turn");
                break;
        }
    }

    /**
     * Parses input as a number in order to get a domino at that index
     * @param currentPlayer
     * @return index
     */
    public int dominoSelection(Player currentPlayer) {
        System.out.println("What domino would you like to play?");

        /*
         * Parses input as int and checks to see that it is a valid index
         */
        int index = Integer.parseInt(choiceScanner.nextLine()) - 1;

        while(index > currentPlayer.getTray().size() || index < 0) {
            System.out.println("Invalid index. Please make another selection.");
            index = Integer.parseInt(choiceScanner.nextLine()) - 1;
        }

        playedDomino = currentPlayer.getTray().get(index);

        return index;
    }

    /**
     * Parses direction that the player wants to play their domino
     * @return direction
     */
    public String getDirection() {
        System.out.println("What side of the board would you like to place the Domino?");
        String direction = choiceScanner.nextLine().toLowerCase();

        while(!direction.equals("left") && !direction.equals("right")) {
            System.out.println("Not a valid direction. Please choose either left or right.");
            direction = choiceScanner.nextLine().toLowerCase();
        }

        this.direction = direction;

        return direction;
    }

    /**
     * Display different error messages depending on the type of error it recieves.
     * @param errorType
     */
    public void errorDisplay(String errorType) {
        if(errorType.equals("No Valid Moves")) {
            System.out.println();
            System.out.println("Cannot play Domino. Try again or draw from the Boneyard.");
        }

        if(errorType.equals("Draw Block")) {
            System.out.println();
            System.out.println("Cannot draw from Boneyard. You still have valid moves.");
        }

        if(errorType.equals("Empty Boneyard")) {
            System.out.println();
            System.out.println("Cannot draw from Boneyard. Boneyard is empty.");
        }

        if(errorType.equals("Pass Block")) {
            System.out.println();
            System.out.println("Cannot pass turn. You still have a valid move or can draw from Boneyard");
        }
    }

    /**
     * Displays when the player has passed their turn
     */
    public void passTurn() {
        System.out.println();
        System.out.println("Player has passed turn");
    }

    /**
     * Displays the winner of the game
     * @param winner
     */
    public void displayWinner(Player winner) {
        System.out.println("Congratulations! " + winner.getName() + " has won!");
    }

    /**
     * Displays what domino was played as the turn ends
     */
    public void endTurn() {
        System.out.println("Playing " + playedDomino.toString() + " at " + direction);
    }

}
