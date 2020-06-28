# Domino

## Miguel Gonzalez

Introduction
------------

Dominios is a game where two players start off with 7 dominos and place dominoes on the edge of the board in order to match
the outside number of the domino. The player must play a domino if capable and must draw from the boneyard if they cant.
The game will end when the boneyard is empty and either one player's tray is empty, or both players have passed their turn

Version 1: Console Based Game
------------------------------

### How to Play 
- In order to start the game, you must invoke java -jar DominosGame_V1.jar in the terminal
- Default sets for dominos is 6, but you can play with more (Note: set of 6 is the smallest you can go)
- When the game starts, you and the computer will be drawn seven dominoes from the boneyard and the game will start
- The player will choose from four options in order to play a domino
- When a domino is played, the player must choose the index of the domino they would like to choose and the direction
- The domino will be placed, and then the computer will take its turn
- Repeat this until the boneyard has been emptied and either one of the players has emptied their tray or both players pass their turn

### Bugs
- When a domino is placed on the board, the board will flip itself, but still leave the corresponding domino connections intact. 

Version 2: GUI
--------------

### How to play 
- Double clicking the file will star the GUI with the default set of 6
- You will choose a domino and then choose the left or right side of the board. (Note: You must click after the domino in order for the game to recognize what side of the board you picked)
- Default sets for dominos is 6, but you can play with more (Note: set of 6 is the smallest you can go)
- When the game starts, you and the computer will be drawn seven dominoes from the boneyard and the game will start
- The player will click on a domino and then click on what side of the board they want to play on.
- The game will take care of drawing dominoes from the boneyard if you are out of moves and will also take care of how the domino is positioned on the board.
- Once you play a domino, the game will switch control to the computer and let it play.
- The game will keep going unitl the game over conditions have been met.

### Bugs
- Currently, each domino is drawn into the screen rather than using image files. This is because for some reason, some of the images were not loading when the dominoes
  are created and you could not see what domino was being played.
- The game has no implemented way of staggering the dominoes, causing the dominoes to go off screen and requiring some reizing of the window.