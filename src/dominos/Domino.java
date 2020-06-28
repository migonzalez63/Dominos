package dominos;

/**
 * Class Domino serves to create a domino and properly set its properties
 */
public class Domino {

    private int leftDots;
    private int rightDots;
    private boolean flipped = false;
    private String image;

    /**
     * Constructor sets the left and right side of the domino
     * @param left
     * @param right
     */
    public Domino(int left, int right) {
        this.leftDots = left;
        this.rightDots = right;
    }

    /**
     * Flips the domino
     */
    public void flipDomino() {
        int temp = leftDots;
        leftDots = rightDots;
        rightDots = temp;

        flipped = true;
    }

    /**
     * Gets the left side of the domino
     * @return leftDots
     */
    public int getLeftDots() {
        return leftDots;
    }

    /**
     * Gets the right side of the domino
     * @return
     */
    public int getRightDots() {
        return rightDots;
    }

    /**
     * Returns a string representation of our domino
     * @return
     */
    public String toString() {
        return "[" + leftDots + "  " + rightDots + "]";
    }
}
