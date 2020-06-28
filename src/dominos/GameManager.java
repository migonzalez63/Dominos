package dominos;

import java.util.ArrayList;

/**
 * Class GameMananager serves to intialize the game and enforce rules of the game.
 * Also plays as the main loop for our Console Display version of the game
 */
public class GameManager {

    private Board board;
    private Boneyard boneyard;
    private Player humanPlayer;
    private Player computerPlayer;
    private Player currentPlayer;
    private ConsoleDisplay consoleDisplay;
    private int turnsPassed = 0;


    /**
     * Initializes the game and will be used to play a GUI version of it
     * @param sets
     */
    public GameManager(int sets) {
        initGame(sets);
    }

    /**
     * Initializes the game and can choose between playing the GUI version or a console based
     * version
     * @param sets
     * @param version
     */
    public GameManager(int sets, String version) {
        initGame(sets);

        if(version.toLowerCase().equals("console")) {
            this.consoleDisplay = new ConsoleDisplay();
            startConsoleGame();
        }
    }

    /**
     * Initializes the board, the boneyard, gives both players their initial hands and sets the first
     * move to the human player/
     * @param sets
     */
    private void initGame(int sets) {
        this.board = new Board();
        this.boneyard = new Boneyard(sets);
        this.humanPlayer = new Player(false,"Human", openingHand(boneyard), boneyard, board);
        this.computerPlayer = new Player(true,"Computer", openingHand(boneyard), boneyard, board);
        this.currentPlayer = humanPlayer;
    }

    /**
     * Gets seven dominoes in a boneyard and sets them in an ArrayList to
     * be given to a player
     * @param boneyard
     * @return openingHand
     */
    private ArrayList<Domino> openingHand(Boneyard boneyard) {
        int startingHandSize = 7;
        ArrayList<Domino> openingHand = new ArrayList<>();

        for(int i = 0; i < startingHandSize; i++) {
            openingHand.add(boneyard.takeDomino());
        }

        return openingHand;
    }

    /**
     * Begins the console version of the game
     */
    private void startConsoleGame() {
        /*
         * Displays intro to game
         */
        consoleDisplay.introDisplay();

        /*
         * While the game is not over, we will enforce rules and make sure
         * that what the players do is legal
         */
        while(!isGameOver()) {
            /*
             * If its the humans turn, we will prompt them to make a choice and parse that choice
             * to decide what to do
             */
            String choice = consoleDisplay.turnDisplay(currentPlayer, board, boneyard.boneyardSize());

            switch(choice) {
                /*
                 * If the choice is p, we will attempt to play a domino. If it is not possible,
                 * we will prompt the user as such and allow them to make another choice. If a domino
                 * was played, we pass our turn and allow the computer to play.
                 */
                case "p":
                    if (currentPlayer.hasLegalMove()) {
                        resetTurnsPassed();
                        int index = consoleDisplay.dominoSelection(currentPlayer);
                        Domino chosenDomino = currentPlayer.getTray().get(index);
                        String direction = consoleDisplay.getDirection();
                        boolean playFlag = currentPlayer.play(chosenDomino, direction);

                        if(playFlag) {
                            consoleDisplay.endTurn();
                            switchPlayer();
                        } else {
                            consoleDisplay.errorDisplay("No Valid Moves");
                        }
                    } else {
                        consoleDisplay.errorDisplay("No Valid Moves");
                    }
                    break;
                /*
                 * If the choice is d, we will attempt to draw a domino. If it is not possible,
                 * we will prompt the user as such and allow them to make another choice.
                 */
                case "d":
                    if (currentPlayer.hasLegalMove()) {
                        consoleDisplay.errorDisplay("Draw Block");
                    } else {
                        boolean drawFlag = currentPlayer.draw();

                        if (drawFlag == false) {
                            consoleDisplay.errorDisplay("Empty Boneyard");
                        }
                    }
                    break;
                /*
                 * If the choice is a, we will attempt to pass the turn.
                 */
                case "a":
                    if (currentPlayer.hasLegalMove() || boneyard.boneyardSize() != 0) {
                        consoleDisplay.errorDisplay("Pass Block");
                    } else {
                        consoleDisplay.passTurn();
                        switchPlayer();
                        incrementTurnsPassed();
                    }
                    break;
                /*
                 * If the choice is q, we will terminate the program
                 */
                case "q":
                    System.exit(1);
                    break;
                /*
                 * If it is the computers turn, we will attempt to play a domino if possible. If not
                 * possible, we will attempt to draw a domino. If that is also not possible, we
                 * will simply skip the turn and relinquish control to the computer.
                 */
                case "Computer's Turn":
                    if(currentPlayer.hasLegalMove()) {
                        resetTurnsPassed();
                        currentPlayer.playAuto();
                        consoleDisplay.computerTurn("played");
                        switchPlayer();
                    } else {
                        if(boneyard.boneyardSize() != 0) {
                            currentPlayer.draw();
                            consoleDisplay.computerTurn("draw");
                        } else {
                            consoleDisplay.passTurn();
                            consoleDisplay.computerTurn("passed");
                            switchPlayer();
                            incrementTurnsPassed();
                        }
                    }
                    break;
            }
        }

        /*
         * Displays the winner of the game
         */
        if(findWinner().equals("Human Wins!")) {
            consoleDisplay.displayWinner(humanPlayer);
        } else if(findWinner().equals("Computer Wins!")) {
            consoleDisplay.displayWinner(computerPlayer);
        }
    }

    /**
     * Returns a reference to the board
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns a reference to the boneyard
     * @return
     */
    public Boneyard getBoneyard() {
        return boneyard;
    }

    /**
     * Increments the number of turns passed
     */
    public void incrementTurnsPassed() {
        this.turnsPassed++;
    }

    /**
     * Resets the number of turns passed
     */
    public void resetTurnsPassed() {
        this.turnsPassed = 0;
    }

    /**
     * returns the current player
     * @return currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Switches what player is currently taking a turn
     */
    public void switchPlayer() {
        if(currentPlayer.equals(humanPlayer)) {
            currentPlayer = computerPlayer;
        } else {
            currentPlayer = humanPlayer;
        }
    }

    /**
     * Checks to see if the game is over by verifying whether the boneyard is
     * empty and either the current player has no dominoes in their tray or
     * if both players have passed their turn
     * @return
     */
    public boolean isGameOver() {
        if(boneyard.boneyardSize() == 0) {
            if(turnsPassed == 2) {
                return true;
            }

            if(currentPlayer.getTray().size() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the winner of the game by checking the total sum
     * of dots in their tray
     * @return "Computer Wins!" if computer is the winner, else "Human Wins!"
     */
    public String findWinner() {
        int humanScore = humanPlayer.getScore();
        int computerScore = computerPlayer.getScore();

        if(humanScore > computerScore) {
            return "Computer Wins!";
        } else {
            return "Human Wins!";
        }
    }
}