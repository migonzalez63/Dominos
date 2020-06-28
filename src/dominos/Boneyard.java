package dominos;

import java.util.*;

/**
 * Class Boneyard creates logical representation of our boneyard where we store our dominoes
 * to draw.
 */
public class Boneyard {
    private Deque<Domino> boneyard = new LinkedList<>();
    private int maxDominoes;

    /**
     * Constructor will create any set of Dominos by using the
     * formula ((n+1)(n+2))/2. Once the boneyard is created,
     * we will shuffle it.
     * @param set
     */
    public Boneyard(int set) {
        maxDominoes = ((set + 1) * (set + 2)) / 2;

        for(int i = 0; i <= set; i++) {
            for(int j = 0; j <= i; j++) {
                Domino newDomino = new Domino(j, i);
                boneyard.add(newDomino);
            }
        }

        Collections.shuffle((List<?>) boneyard);
    }

    /**
     * Takes a domino from the beginning of the queue
     * @return
     */
    public Domino takeDomino() {
        return boneyard.removeLast();
    }

    /**
     * Returns the size of the boneyard
     * @return boneyard.size()
     */
    public int boneyardSize() {
        return boneyard.size();
    }

    /**
     * Returns a string representation of the board
     * @return
     */
    public String toString() {
        int i = 1;
        StringBuilder boneYardString = new StringBuilder();

        boneYardString.append("[");

        for(Domino dominoes: boneyard) {
            boneYardString.append(dominoes.toString());
            if(i < maxDominoes - 1) {
                boneYardString.append(", ");
            }

            i++;
        }

        boneYardString.append("]");

        return boneYardString.toString();
    }

}
