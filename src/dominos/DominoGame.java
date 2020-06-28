package dominos;

import javafx.application.Application;

public class DominoGame {
    public static void main(String[] args) {

        if(args.length == 0) {
            Application.launch(GUIDisplay.class, "6");
        } else if(args.length == 1) {
            try {
                int sets = Integer.parseInt(args[0]);

                if(sets < 4) {
                    System.out.println("Cannot play Domino with that set");
                    System.exit(1);
                }

                Application.launch(GUIDisplay.class, Integer.toString(sets));
            } catch (NumberFormatException e) {
                System.out.println("Please input a number for the sets");
            }
        } else if(args.length == 2) {
            if(args[1].toLowerCase().equals("console")) {
                new GameManager(Integer.parseInt(args[0]), args[1]);
            } else if(args[1].toLowerCase().equals("gui")) {
                try {
                    int sets = Integer.parseInt(args[0]);

                    if(sets < 4) {
                        System.out.println("Cannot play Domino with that set");
                        System.exit(1);
                    }

                    Application.launch(GUIDisplay.class, Integer.toString(sets));
                } catch (NumberFormatException e) {
                    System.out.println("Please input a number for the sets");
                }
            } else {
                System.out.println("Please enter an appropriate version");
            }
        } else {
            System.out.println("Too many arguments");
        }
    }
}
