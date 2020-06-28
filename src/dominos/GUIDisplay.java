package dominos;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class GUIDisplay creates the GUI representation of our gram
 */
public class GUIDisplay extends Application{
    private BorderPane guiPane = new BorderPane();
    private BorderPane trayPane = new BorderPane();
    private HBox boardPane = new HBox();
    private HBox trayBox = new HBox();
    private Label boneyardLabel = new Label();
    private Label computerLabel = new Label();
    private BorderPane labelPane = new BorderPane();
    private Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
    private Domino chosenDomino;
    private DominoGUI playedDomino;
    private GameManager gm;
    private String direction = "left";
    private boolean clickedBoard = false;
    private int sets;

    /**
     * Used to get the number of sets that we will use to play our game
     */
    @Override
    public void init() {
        this.sets = Integer.parseInt(getParameters().getRaw().get(0));
    }

    /**
     * Private class DominoGUI is used to create the visual representation of
     * our domino and sets the behavior to when it detects a click
     */
    private class DominoGUI extends HBox {
        private Domino refDomino;
        private int maxSize = 30;

        /**
         * Constructor Takes a domino and a dominoType string in order to build
         * the domino and see what type of behavior it should have. Dominos are
         * created by drawing rectangles and putting them in an HBox.
         * @param currentDomino
         * @param dominoType
         */
        public DominoGUI(Domino currentDomino, String dominoType) {
            refDomino = currentDomino;

            if(dominoType.equals("playable")) {
                setOnMouseClicked(event -> {
                    chosenDomino = refDomino;
                    playedDomino = this;
                });
            }

            Rectangle leftSide = new Rectangle(maxSize, maxSize);
            leftSide.setFill(Color.BEIGE);
            leftSide.setStroke(Color.BLACK);
            Text leftNumber = new Text(Integer.toString(currentDomino.getLeftDots()));
            StackPane leftDomino = new StackPane(leftSide, leftNumber);

            Rectangle rightSide = new Rectangle(maxSize,maxSize);
            rightSide.setFill(Color.BEIGE);
            rightSide.setStroke(Color.BLACK);
            Text rightNumber = new Text(Integer.toString(currentDomino.getRightDots()));
            StackPane rightDomino = new StackPane(rightSide, rightNumber);

            getChildren().addAll(leftDomino, rightDomino);
        }
    }

    /**
     * Alert popup that notifies the user when the game is over and who won.
     */
    private void showGameOverAlert() {
        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setHeaderText(gm.findWinner());

        gameOverAlert.setOnHidden(event -> Platform.exit());
        gameOverAlert.show();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Domino Game");

        /*
         * Initializes the game
         */
        gm = new GameManager(sets);

        /*
         * Sets up the GUI
         */

        buildTray();
        initBoard();
        boneyardLabel.setText("Boneyard has " + gm.getBoneyard().boneyardSize() + " dominoes left");
        computerLabel.setText("Computer has 7 dominoes left");
        VBox labelBox = new VBox(boneyardLabel, computerLabel);

        Button passTurnButton = new Button("Pass Turn");
        passTurnButton.setOnMouseClicked(event -> {

            System.out.println("Button Clicked");

            if(gm.getBoneyard().boneyardSize() == 0 && !gm.getCurrentPlayer().hasLegalMove()) {
                gm.switchPlayer();
                gm.incrementTurnsPassed();
            }
        });

        Insets insets = new Insets(0, 400, 0, 370);

        trayBox.setSpacing(5);
        trayPane.setCenter(new Group(trayBox));

        labelPane.setLeft(labelBox);
        BorderPane computerMovePane = new BorderPane();
        Label computerMoveLabel = new Label("Waiting for player to play");

        computerMovePane.setCenter(new Group(computerMoveLabel));
        labelPane.setRight(new Group(new VBox(passTurnButton)));
        labelPane.setCenter(computerMovePane);
        BorderPane.setMargin(computerMovePane, insets);
        boardPane.setSpacing(5);
        guiPane.setBottom(trayPane);
        guiPane.setTop(new Group(labelPane));
        guiPane.setCenter(new Group(boardPane));

        Scene scene = new Scene(guiPane, 1200, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        /*
         * Main animation loop where some logic is implemented in order to see if the game should
         * keep going
         */
        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                /*
                 * If the game isn't over, we will check to see whose turn is it
                 * and then play the game accordingly
                 */
                if(!gm.isGameOver()) {
                    Player currentPlayer = gm.getCurrentPlayer();
                    if(currentPlayer.getName().equals("Human")) {
                        if(currentPlayer.hasLegalMove()) {
                            if(chosenDomino != null) {
                                /*
                                 * If we have chosen a domino and the we have clicked on the board
                                 * we play it to our board and update our board. Then we switch
                                 * players. If the user cannot put a domino, we then see
                                 * if we can draw another domino.
                                 */
                                if(clickedBoard) {
                                    gm.resetTurnsPassed();
                                    boolean playFlag = currentPlayer.play(chosenDomino, direction);

                                    if(playFlag) {
                                        trayBox.getChildren().remove(playedDomino);
                                        buildBoard();
                                        gm.switchPlayer();
                                    }

                                    clickedBoard = false;
                                }
                            }
                        } else if(gm.getBoneyard().boneyardSize() != 0) {
                            boolean drawFlag = currentPlayer.draw();

                            if(drawFlag) {
                                buildTray();
                                boneyardLabel.setText("Boneyard has " + gm.getBoneyard().boneyardSize() + " dominoes left");
                            }
                        }
                    }
                    /*
                     * If it is the computers turn, they will attempt to play a domino, and if they can't, then
                     * they will draw or pass the turn. We update labels as well.
                     */
                    else if(currentPlayer.getName().equals("Computer")) {
                        if(currentPlayer.hasLegalMove()) {
                            gm.resetTurnsPassed();
                            currentPlayer.playAuto();
                            buildBoard();
                            gm.switchPlayer();
                            computerMoveLabel.setText("Computer has played a domino");
                        } else {
                            if(gm.getBoneyard().boneyardSize() != 0) {
                                currentPlayer.draw();
                                computerMoveLabel.setText("Computer has drawn from Boneyard");
                            } else {
                                gm.incrementTurnsPassed();
                                gm.switchPlayer();
                                computerMoveLabel.setText("Computer has passed turn");
                            }
                        }

                        computerLabel.setText("Computer has " + currentPlayer.getTray().size() + " dominoes");
                    }
                }
                /*
                 * If the game is over, we stop the clock and pop up the alert
                 */
                else {
                    stop();
                    showGameOverAlert();
                }
            }
        };

        loop.start();
    }

    /**
     * Builds the GUI representation of the player tray
     */
    private void buildTray() {
        Player currentPlayer = gm.getCurrentPlayer();

        trayBox.getChildren().clear();

        for(Domino currentDomino: currentPlayer.getTray()) {
            DominoGUI newDomino = new DominoGUI(currentDomino, "playable");
            trayBox.getChildren().add(newDomino);
        }
    }

    /**
     * Initializes the board in order to allow us to play the first move
     */
    private void initBoard() {
        Rectangle leftBoard = buildRectangle(Color.TRANSPARENT, "left");
        Rectangle rightBoard = buildRectangle(Color.TRANSPARENT, "right");

        boardPane.getChildren().addAll(leftBoard, rightBoard);
    }

    /**
     * Helper method that builds a rectangle. These are used to detect what sie of the board we want to play
     * @param color
     * @param side
     * @return
     */
    private Rectangle buildRectangle(Color color, String side) {
        Rectangle sideRect = new Rectangle(50, 50);
        sideRect.setFill(color);
        sideRect.setOnMouseClicked(event -> {
            clickedBoard = true;
            direction = side;
        });
        return sideRect;
    }

    /**
     * Builds the visual representation of the board after dominoes have been played
     */
    private void buildBoard() {
        Board currentBoard = gm.getBoard();

        boardPane.getChildren().clear();

        for(Domino currentDomino: currentBoard.getBoard()) {
            DominoGUI newDomino = new DominoGUI(currentDomino, "non-playable");

            if (currentBoard.boardSize() == 1) {
                Rectangle leftSide = buildRectangle(Color.TRANSPARENT, "left");
                Rectangle rightSide = buildRectangle(Color.TRANSPARENT, "right");
                boardPane.getChildren().addAll(leftSide, newDomino, rightSide);
            } else if(currentDomino.equals(currentBoard.getLeftDomino())) {
                Rectangle leftSide = buildRectangle(Color.TRANSPARENT, "left");
                boardPane.getChildren().addAll(leftSide, newDomino);
            } else if(currentDomino.equals(currentBoard.getRightDomino())) {
                Rectangle rightSide = buildRectangle(Color.TRANSPARENT, "right");
                boardPane.getChildren().addAll(newDomino, rightSide);
            } else {
                boardPane.getChildren().add(newDomino);
            }
        }
    }
}
