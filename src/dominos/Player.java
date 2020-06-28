package dominos;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * C
 */
public class Player {
    private boolean computerFlag;
    private String name;
    private ArrayList<Domino> tray;
    private Boneyard boneyard;
    private Board board;
    private Domino legalMove;

    /**
     * Constructor sets the name of the players (Human or Computer), sets their tray,
     * and gets references to the board and boneyard
     */
    public Player(boolean computerFlag, String name, ArrayList newTray, Boneyard boneyard, Board board) {
        this.name = name;
        this.tray = newTray;
        this.boneyard = boneyard;
        this.board = board;
        this.computerFlag = computerFlag;
    }

    /**
     * Calculates the score of the player by checking to
     * see hom much each domino in their tray is worth
     * @return totalScore
     */
    private int calculateScore() {
        int totalScore = 0;

        for(Domino currentDomino: tray) {
            totalScore += (currentDomino.getLeftDots() + currentDomino.getRightDots());
        }

        return totalScore;
    }

    /**
     * Allows the player to draw from the boneyard
     * @return true if able to draw, false otherwise
     */
    public boolean draw() {
        if(boneyard.boneyardSize() == 0) {
            return false;
        } else {
            Domino nextDomino = boneyard.takeDomino();
            tray.add(nextDomino);
            return true;
        }
    }

    /**
     * Checks to see if the player can make a legal move
     * @return true if able to play legal move, false otherwise
     */
    public boolean hasLegalMove() {
        /*
         * If the board is empty, then we always have a legal move
         */
        if(board.boardSize() == 0) {
            legalMove = tray.get(0);
            return true;
        }

        /*
         * if our tray is empty, then we can't play any moves
         */
        if(tray.size() == 0) {
            return false;
        }

        int leftSide = board.getLeftEnd();
        int rightSide = board.getRightEnd();

        /*
         * If either ends of the board end with a zero, then any of our dominos is able
         * to be played there
         */
        if(leftSide == 0 || rightSide == 0) {
            legalMove = tray.get(0);
            return true;
        }

        /*
         * We iterate through every domino and make sure that one of them is playable
         */
        for(Domino currentDomino: tray) {
            if(currentDomino.getLeftDots() == 0 || currentDomino.getRightDots() == 0) {
                legalMove = currentDomino;
                return true;
            }

            if(currentDomino.getLeftDots() == leftSide || currentDomino.getLeftDots() == rightSide) {
                legalMove = currentDomino;
                return true;
            }

            if(currentDomino.getRightDots() == leftSide || currentDomino.getRightDots() == rightSide) {
                legalMove = currentDomino;
                return true;
            }
        }

        return false;
    }


    /**
     * Play a domino in the board in the given direction
     * @param chosenDomino
     * @param direction
     * @return true if played. false otherwise
     */
    public boolean play(Domino chosenDomino, String direction) {
        int left = chosenDomino.getLeftDots();
        int right = chosenDomino.getRightDots();

        /*
         * If the board is empty, we will just play our domino
         */
        if(board.boardSize() == 0) {
            tray.remove(chosenDomino);
            board.addLeft(chosenDomino);
            return true;
        }

        switch (direction) {
            /*
             * If we want to play our domino to the left of the board, we
             * need to check our domino and see how we must oriented in order
             * to match the board
             */
            case "left":
                int leftEnd = board.getLeftEnd();

                if(leftEnd == 0 || right == 0) {
                    tray.remove(chosenDomino);
                    board.addLeft(chosenDomino);
                    return true;
                } else if(left == 0) {
                    tray.remove(chosenDomino);
                    chosenDomino.flipDomino();
                    board.addLeft(chosenDomino);
                    return true;
                } else if(right == leftEnd) {
                    tray.remove(chosenDomino);
                    board.addLeft(chosenDomino);
                    return true;
                } else if(left == leftEnd) {
                    tray.remove(chosenDomino);
                    chosenDomino.flipDomino();
                    board.addLeft(chosenDomino);
                    return true;
                }

                return false;
            case "right":
                int rightEnd = board.getRightEnd();

                if(rightEnd == 0) {
                    tray.remove(chosenDomino);
                    board.addRight(chosenDomino);
                    return true;
                } else if(left == rightEnd || left == 0) {
                    tray.remove(chosenDomino);
                    board.addRight(chosenDomino);
                    return true;
                } else if(right == rightEnd || right == 0) {
                    tray.remove(chosenDomino);
                    chosenDomino.flipDomino();
                    board.addRight(chosenDomino);
                    return true;
                }

                return false;
            default:
                return false;
        }
    }

    /**
     * Allows for automatic plays for the player. Serves to provide some functionality for the computer player
     * @return true if play was successful, false otherwise
     */
    public boolean playAuto() {
        int rightEnd = board.getRightEnd();

        for(Domino currentDomino: tray) {
            if(currentDomino.equals(legalMove)) {
                if(currentDomino.getLeftDots() == rightEnd || currentDomino.getRightDots() == rightEnd) {
                    return play(currentDomino, "right");
                } else {
                    return play(currentDomino, "left");
                }
            }
        }

        return false;
    }

    /**
     * Gets the players tray
     * @return tray
     */
    public ArrayList<Domino> getTray() {
        return tray;
    }

    /**
     * Checks to see if the player is a computer
     * @return
     */
    public boolean isComputer() {
        return computerFlag;
    }

    /**
     * Gets the score for the player
     * @return
     */
    public int getScore() {
        return calculateScore();
    }

    /**
     * Gets their name
     * @return
     */
    public String getName() {
        return name;
    }
}
