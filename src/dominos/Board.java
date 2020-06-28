package dominos;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Board creates teh logical interpretation for our domino board using a Queue.
 */
public class Board {

    private Deque<Domino> board = new LinkedList<>();

    /**
     * Returns size of the board
     * @return board.size()
     */
    public int boardSize() {
        return board.size();
    }

    /**
     * Returns the numerical representation of the left
     * end of the queue
     * @return board.getFirst().getLeftDots()
     */
    public int getLeftEnd() {
        return board.getFirst().getLeftDots();
    }

    /**
     * Returns the numerical representation of the right
     * end of the queue
     * @return board.getLast().getRighttDots()
     */
    public int getRightEnd() {
        return board.getLast().getRightDots();
    }

    /**
     * Returns the Domino object located at the left end of the queue
     * @return board.getFirst
     */
    public Domino getLeftDomino() {return board.getFirst();}

    /**
     * Returns the Domino object located at the left end of the queue
     * @return board.getFirst
     */
    public Domino getRightDomino() {return board.getLast();}


    /**
     * Adds a domino to the beginning of the queue
     * @param domino
     */
    public void addLeft(Domino domino) {
        board.addFirst(domino);
    }

    /**
     * Adds a domino to the end of the queue
     * @param domino
     */
    public void addRight(Domino domino) {
        board.addLast(domino);
    }

    /**
     * Creates a string representation of the board
     */
    public void boardString() {
        StringBuilder topLevel = new StringBuilder();
        StringBuilder bottomLevel = new StringBuilder();
        LinkedList<Domino> stringBoard = new LinkedList<>(board);

        if(boardSize() % 2 == 0) {
            bottomLevel.insert(0, "   ");
        } else {
            topLevel.insert(0, "   ");
        }

        for(int i = 0; i < boardSize(); i++) {
            if(i % 2 == 0) {
                topLevel.append(stringBoard.get(i));
            } else {
                bottomLevel.append(stringBoard.get(i));
            }
        }

        System.out.println(topLevel.toString());
        System.out.println(bottomLevel.toString());
    }

    /**
     * Returns the board as a list
     * @return
     */
    public List<Domino> getBoard() {
        return (List<Domino>) this.board;
    }

}
