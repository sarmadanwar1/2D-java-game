package uk.ac.bradford.dropgame;

import java.util.HashSet;
import java.util.Random;
import uk.ac.bradford.dropgame.Tile.TileType;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating rooms, the player and dropper, as well as updating information when a
 * key is pressed (processed by the InputHandler) while the game is running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 35;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 18;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to place the player and
     * objects, and to randomise movement etc. Passing an integer (e.g. 123) to
     * the constructor called here will give fixed results - the same numbers
     * will be generated every time WHICH CAN BE VERY USEFUL FOR TESTING AND
     * BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by raising the door height and items required to exit
     */
    private int levelNumber = 1;

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 1;
    
    /**
     * The number of items collected in this level. Used to check if enough
     * items have been collected to allow the player to exit via a DOOR
     * tile and complete the current level.
     */
    private int itemsCollected = 0;
    
    /**
     * The GUI associated with this GameEngine object. This link allows the
     * engine to pass level and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles that represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created. This is the array that is used to draw images to the
     * screen by the GUI class, and by you to check what a specific Tile contains
     * by checking the content of specific elements of the array and using the
     * getType() method to determine the type of the Tile.
     */
    private Tile[][] room;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including energy and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player player;

    /**
     * A Dropper object used to create the dropper for this level. The object 
     * has position information stored via its attributes, and methods to check and
     * update the position of the dropper.
     */
    private Dropper dropper;
    
    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. This method should instantiate the room array,
     * and then fill it with Tile objects that are created inside this method.
     * It is recommended that for your first version of this method you fill the
     * 2D array using for loops, and create new Tile objects inside the inner
     * loop, set their type using the constructor and the TileType enumeration,
     * then assigning the Tile object to an element in the array. For the first version
     * you should use just Tile objects with the type BACKGROUND.
     *
     * Later tasks will require additions to this method to add new content, see
     * the specification document for more details.
     */
    private void generateRoom() {
        room = new Tile[35][18];        
        for (int x = 0; x < 35; x++){
        for (int y = 0; y < 18; y++){
            room[x][y] = new Tile (TileType.BACKGROUND);
        }
}
        for (int x =0; x < room.length; x++){
            room[x][0] = new Tile(TileType.CEILING);
            room[x] [room[x].length - 1] = new Tile(TileType.FLOOR);
        }
        for (int y = 1; y < room[0].length - 1; y++){
            room[0] [y] = new Tile(TileType.WALL);
            room[34] [y] = new Tile(TileType.WALL);
        }
        int doorY = rng.nextInt(18 - 2) + 1;
        int doorX = rng.nextInt(2) == 0 ? 0 : 35 - 1;
 
        room[doorX][doorY] = new Tile(TileType.DOOR);
                
    }

    /**
     * Adds a dropper to the current level. The dropper should be placed at the
     * top of the room by setting appropriate X and Y co-ordinates when creating
     * this object, and the object created should be assigned to the "dropper"
     * attribute of this class.
     */
    private void addDropper() {
        int dropperX = rng.nextInt(35);
        int dropperY = 1;
        dropper = new Dropper(dropperX,dropperY);
                
    }

    /**
     * Creates a Player object in the game. This method should instantiate the
     * Player class by creating an object using the appropriate constructor.
     * The created object should then be assigned to the "player" attribute of
     * this class.
     * 
     * The player should be placed at the bottom of the level by using appropriate
     * X and Y co-ordinate values when calling the constructor. The energy value
     * can be set to any integer for your first version of this method, but may
     * need updating in a later task.
     */
    private void createPlayer() {
        int maxEnergy = 1;
        int initialisex = 17;
        int initialisey = 16;
        player = new Player(initialisex, initialisey, maxEnergy);
    }

    /**
     * Activates the dropper object in the level by creating a new Tile object,
     * with the type TileType.BOX. This new object should be set to the position
     * in the room array that is one tile below the dropper's current position
     * (use the relevant methods from the Entity/Dropper class to get the current
     * position of the dropper and calculate the co-ordinates of the space below it).
     */
    private void activateDropper() {
        int dropperX = dropper.getX();
        int dropperY = dropper.getY();
        
        if (dropperY < 18 - 1) {
            double chance = Math.random();
        Tile.TileType newTileType;
        
        if (chance < 0.9) {
            newTileType = Tile.TileType.BOX;
        } else {
            newTileType = Tile.TileType.ITEMBOX;
        }
        
            if (room[dropperX][dropperY + 1].getType() == Tile.TileType.BACKGROUND) {
                room[dropperX][dropperY + 1] = new Tile(newTileType);

            }
        }
    }

    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the GameInputHandler class when
     * the user has pressed the left arrow key on the keyboard.
     * Your code should set the X and Y position of the player to place them
     * in the correct tile based on the direction of movement, i.e. changing
     * their X position by one. You will need to use the relevant method(s) from
     * the Player/Entity class to find the current position of the player, and
     * set new values based on the current X and Y values.
     *
     * In later tasks you will need to update this method to handle more complex
     * movement cases (e.g. only moving into BACKGROUND tiles, collecting items
     * and climbing boxes).
     *
     */
    public void movePlayerLeft() {
        int playerX = player.getX();
        int playerY = player.getY();
        
         if (playerX > 0) {
        Tile.TileType targetTile = room[playerX - 1][playerY].getType();
        if (targetTile == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX - 1, playerY);
        }
        else if (targetTile == Tile.TileType.BOX && playerY > 0 && room[playerX - 1][playerY - 1].getType() == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX - 1, playerY - 1);
       }
         else if (targetTile == Tile.TileType.ITEMBOX && playerY > 0 && room[playerX - 1][playerY - 1].getType() == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX - 1, playerY - 1);
         }
        else if (targetTile == Tile.TileType.DOOR) {
           player.setPosition(playerX - 1, playerY);
        }
                 else if (targetTile == Tile.TileType.ITEM) {
    player.setPosition(playerX - 1, playerY);
    itemsCollected++;
    }
         }
        }

    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the GameInputHandler class when
     * the user has pressed the right arrow key on the keyboard.
     * Your code should set the X and Y position of the player to place them
     * in the correct tile based on the direction of movement, i.e. changing
     * their X position by one. You will need to use the relevant method(s) from
     * the Player/Entity class to find the current position of the player, and
     * set new values based on the current X and Y values.
     *
     * In later tasks you will need to update this method to handle more complex
     * movement cases (e.g. only moving into BACKGROUND tiles, collecting items
     * and climbing boxes).
     *
     */
    public void movePlayerRight() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        if (playerX < 35 - 1) {
        Tile.TileType targetTile = room[playerX + 1][playerY].getType();
         if (targetTile == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX + 1, playerY);
        }
        else if (targetTile == Tile.TileType.BOX && playerY > 0 && room[playerX + 1][playerY - 1].getType() == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX + 1, playerY - 1);
        }
        else if (targetTile == Tile.TileType.ITEMBOX && playerY > 0 && room[playerX + 1][playerY - 1].getType() == Tile.TileType.BACKGROUND) {
            player.setPosition(playerX + 1, playerY - 1);
        }
        else if (targetTile == Tile.TileType.DOOR) {
    player.setPosition(playerX + 1, playerY);
    }
         else if (targetTile == Tile.TileType.ITEM) {
    player.setPosition(playerX + 1, playerY);
    itemsCollected++;
    }
    }
    }
    
    /**
     * Breaks all boxes surrounding the player. This method is automatically 
     * called when the player presses the Q key on the keyboard. This method
     * should check all the tiles surrounding the player (ideally 8 tiles that
     * are adjacent to the player object) and "break" them by replacing them
     * with new Tile objects with the type BACKGROUND.
     * 
     * A later task will require you to handle the breaking of ITEMBOX tiles,
     * and to handle player energy to limit the use of this ability.
     */
    public void breakBoxes() {
        
      int[][] directions = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},          {0, 1},  
        {1, -1}, {1, 0}, {1, 1}    
    };
        
        for (int[] dir : directions) {
        int playerX = player.getX() + dir[0];
        int playerY = player.getY() + dir[1];
        
            if (playerX >= 0 && playerX < 18 &&
            playerY >= 0 && playerY < room[0].length) {
            
        }
                 if (room[playerX][playerY].getType()==TileType.BOX) {
                 room[playerX][playerY] = new Tile(Tile.TileType.BACKGROUND);
        }
                 else if (room[playerX][playerY].getType() == Tile.TileType.ITEMBOX) {
                    room[playerX][playerY] = new Tile(Tile.TileType.ITEM);
        }
                 else if (room[playerX][playerY].getType() == Tile.TileType.ITEM) {
                    room[playerX][playerY] = new Tile(Tile.TileType.BACKGROUND);
                    itemsCollected++;
        }
    }
    }
    /**
     * Moves the dropper object within the room. The method updates the X and Y
     * attributes of the "dropper" object (attribute of this class) to move it
     * left or right.
     * 
     * Your first version of this method should move the dropper either left or
     * right depending on a random choice - it may be easiest to generate a
     * random number with the value 0 or 1 and move left or right depending on
     * this randomly generated value.
     * 
     * A later version of this method will require you to move the dropper
     * by checking the player object's position and "following" the player.
     */
    private void moveDropper() {
        int dropperX = dropper.getX();
        int dropperY = dropper.getY();
        int playerX = player.getX();
        
        if (dropperX > playerX) {
            if (dropperX > 0 && room[dropperX - 1][dropperY].getType() == Tile.TileType.BACKGROUND){
                dropper.setPosition(dropperX -1, dropperY);
            }
        }
        else if(dropperX < playerX) {
            if (dropperX < 35 -1 && room[dropperX + 1][dropperY].getType() == Tile.TileType.BACKGROUND){
                dropper.setPosition(dropperX + 1, dropperY);     
            }
        }             
    }

    /**
     * A method that applies gravity to the player and boxes in the game, using
     * two specific methods.
     */
    private void applyGravity() {
        applyPlayerGravity();
        applyBoxGravity();
    }

    /**
     * Applies gravity to the player object. This method should check if the Tile
     * below the player is of the type BACKGROUND; if it is then the player's
     * position should be changed to "drop" the player one Tile into the empty
     * Tile below their current position. You will need to retrieve their
     * current position using the appropriate methods in the Player/Entity class.
     */
    private void applyPlayerGravity() {
        int playerX = player.getX();
        int playerY = player.getY();
        Tile belowTile = room[playerX][playerY + 1];
            if (belowTile.getType() == TileType.BACKGROUND){
                player.setPosition(playerX, playerY + 1);
    }
    }   

    /**
     * Applies gravity to BOX tiles. This method should (starting from the
     * bottom of the level!) traverse over every Tile object in the room array,
     * and for every tile with the type BOX it should check if the tile below
     * the BOX is of the type BACKGROUND. If the tile below the BOX is a BACKGROUND
     * tile then the BOX should be "dropped" one tile by creating new objects and
     * placing them into the room array at the correct positions, or swapping
     * the BOX and BACKGROUND tile objects in the array. After dropping, the BOX
     * should now be one tile lower and the position it used to be in should be a
     * BACKGROUND tile.
     */
    private void applyBoxGravity() {
        for (int y = 18 - 2; y >= 0; y--) {
             for (int x = 0; x < 35; x++) {
                 if (room[x][y].getType() == Tile.TileType.BOX) {
                     
                     if (player.getX()== x && player.getY()== y+1){
                         System.exit(0);
                     }
                     if (room[x][y + 1].getType() == Tile.TileType.BACKGROUND) {
                         room[x][y + 1] = new Tile(Tile.TileType.BOX);
                         room[x][y] = new Tile(Tile.TileType.BACKGROUND);
                     }
                     
                 }
             }
        }
       for (int y = 18 - 2; y >= 0; y--) {
             for (int x = 0; x < 35; x++) {
                 if (room[x][y].getType() == Tile.TileType.ITEMBOX) {
                     
                 
                     if (room[x][y + 1].getType() == Tile.TileType.BACKGROUND) {
                         room[x][y + 1] = new Tile(Tile.TileType.ITEMBOX);
                         room[x][y] = new Tile(Tile.TileType.BACKGROUND);
        }
                 }
             }
       }
    }
    /**
     * A method to check if the current level has been "completed". A level is
     * complete when the player has reached the DOOR tile. This method should
     * return either true or false depending on whether the level is complete.
     * 
     * A later version of this method will also need to check if a sufficient
     * number of items have been collected before the door can be used.
     * 
     * @return true if the level is completed, or false otherwise.
     */
    private boolean levelComplete() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        int requiredItems = levelNumber * 2;
        if (itemsCollected < requiredItems) {
            return false;
        }
        
        for (int y = 1; y < 18 - 1; y++) {
            if ((room[0][y].getType() == TileType.DOOR && playerX == 0 && playerY == y)
                    || (room[35 - 1][y].getType() == TileType.DOOR && playerX == 35 - 1 && playerY == y)) {
                return true;
            }
        }
        return false;  
    }
    /**
     * This method is called when the player completes the current level by reaching
     * a DOOR tile, optionally also requiring some ITEM tiles to be collected first (in a later task).
     *
     * This method should increase the current level number, reset the counter
     * for the number of items collected in the level, generate a new room by
     * using the appropriate method, and place a dropper and the player in the
     * new level using the addDropper() method and the placePlayer() method.
     *
     */
    private void nextLevel() {
        levelNumber++;

        itemsCollected = 0;
 
        generateRoom();
 
        addDropper();
 
        placePlayer();
 
        turnNumber = 1;
 
        gui.updateDisplay(room, player, dropper);
    }

    /**
     * Places the existing player object into a new level by setting appropriate
     * X and Y values for the player objects position.
     */
    private void placePlayer() {
        int initialiseX = 17;
        int initialisey = 16;
        
        player.setPosition(initialiseX,initialisey);
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method increments the turnNumber, applies gravity, moves
     * the dropper every two turns, activates the dropper every seven turns and
     * checks if the level has been completed, creating a new level if it has.
     * Finally it requests the GUI to redraw the game level by passing it the
     * room, player and dropper objects for the current level.
     *
     */
    public void doTurn() {
        turnNumber++;
        applyGravity();
        if (turnNumber % 2 == 0) {      //moves every two turns
            moveDropper();
        }
        if (turnNumber % 7 == 0) {      //activates every seven turns
            activateDropper();
        }
        if (levelComplete()) {
            nextLevel();
        }
        gui.updateDisplay(room, player, dropper);
    }

    /**
     * Starts a game. This method generates a room, adds the dropper and player, 
     * then requests the GUI to update the level on screen using the information
     * passed to it.
     */
    public void startGame() {
        generateRoom();
        addDropper();
        createPlayer();
        gui.updateDisplay(room, player, dropper);
    }

}
