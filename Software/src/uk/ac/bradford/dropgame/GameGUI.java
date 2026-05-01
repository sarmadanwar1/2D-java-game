package uk.ac.bradford.dropgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The GameGUI class is responsible for rendering graphics to the screen to
 * display the game room, player and dropper. The GameGUI class passes keyboard
 * events to a registered GameInputHandler to be handled.
 *
 * @author prtrundl
 */
public class GameGUI extends JFrame {

    /**
     * The three final int attributes below set the size of some graphical
     * elements, specifically the display height and width of tiles in the game
     * and the height of bars for some Entity objects in the game. Tile sizes
     * should match the size of the image files used in the game.
     */
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int BAR_HEIGHT = 3;

    /**
     * The canvas is the area that graphics are drawn to. It is an internal
     * class of the GameGUI class.
     */
    Canvas canvas;

    /**
     * Constructor for the GameGUI class. It calls the initGUI method to
     * generate the required objects for display.
     */
    public GameGUI() {
        initGUI();
    }

    /**
     * Registers an object to be passed keyboard events captured by the GUI.
     *
     * @param i the GameInputHandler object that will process keyboard events to
     * make the game respond to inputs
     */
    public void registerKeyHandler(InputHandler i) {
        addKeyListener(i);
    }

    /**
     * Method to create and initialise components for displaying elements of the
     * game on the screen.
     */
    private void initGUI() {
        add(canvas = new Canvas());     //adds canvas to this frame
        setTitle("Warehouse Escape!");
        setSize(1136, 615);
        setLocationRelativeTo(null);        //sets position of frame on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method to update the graphical elements on the screen, usually after
     * player and dropper have moved when a keyboard event was handled. The
     * method requires three arguments and displays corresponding information on
     * the screen.
     *
     * @param tiles A 2-dimensional array of TileTypes. This is the tiles of the
     * current game level that should be drawn to the screen.
     * @param player A Player object. This object is used to draw the player in
     * the right tile and display its energy. null can be passed for this
     * argument, in which case no player will be drawn.
     * @param dropper A Dropper object that is processed to draw the dropper.
     * null can be passed for this argument in which case no dropper will be
     * drawn.
     */
    public void updateDisplay(Tile[][] tiles, Player player, Dropper dropper) {
        canvas.update(tiles, player, dropper);
    }
}

/**
 * Internal class used to draw elements within a JPanel. The Canvas class loads
 * images from the asset folder inside the main project folder.
 *
 * @author prtrundl
 */
class Canvas extends JPanel {

    private BufferedImage background;
    private BufferedImage floor;
    private BufferedImage wall;
    private BufferedImage door;
    private BufferedImage ceiling;
    private BufferedImage box;
    private BufferedImage itembox;
    private BufferedImage item;
    private BufferedImage player;
    private BufferedImage dropper;

    Tile[][] currentTiles;      //the current 2D array of tiles to display
    Player currentPlayer;       //the current player object to be drawn
    Dropper currentDropper;     //the current dropper to draw

    /**
     * Constructor that loads tile images for use in this class
     */
    public Canvas() {
        loadTileImages();
    }

    /**
     * Loads tiles images from a fixed folder location within the project
     * directory; a folder called assets
     */
    private void loadTileImages() {
        try {
            background = ImageIO.read(new File("assets/background.png"));
            assert background.getHeight() == GameGUI.TILE_HEIGHT
                    && background.getWidth() == GameGUI.TILE_WIDTH;
            floor = ImageIO.read(new File("assets/floor.png"));
            assert floor.getHeight() == GameGUI.TILE_HEIGHT
                    && floor.getWidth() == GameGUI.TILE_WIDTH;
            wall = ImageIO.read(new File("assets/wall.png"));
            assert wall.getHeight() == GameGUI.TILE_HEIGHT
                    && wall.getWidth() == GameGUI.TILE_WIDTH;
            door = ImageIO.read(new File("assets/door.png"));
            assert door.getHeight() == GameGUI.TILE_HEIGHT
                    && door.getWidth() == GameGUI.TILE_WIDTH;
            ceiling = ImageIO.read(new File("assets/ceiling.png"));
            assert ceiling.getHeight() == GameGUI.TILE_HEIGHT
                    && ceiling.getWidth() == GameGUI.TILE_WIDTH;
            box = ImageIO.read(new File("assets/box.png"));
            assert box.getHeight() == GameGUI.TILE_HEIGHT
                    && box.getWidth() == GameGUI.TILE_WIDTH;
            player = ImageIO.read(new File("assets/player.png"));
            assert player.getHeight() == GameGUI.TILE_HEIGHT
                    && player.getWidth() == GameGUI.TILE_WIDTH;
            dropper = ImageIO.read(new File("assets/dropper.png"));
            assert dropper.getHeight() == GameGUI.TILE_HEIGHT
                    && dropper.getWidth() == GameGUI.TILE_WIDTH;
            itembox = ImageIO.read(new File("assets/itembox.png"));
            assert itembox.getHeight() == GameGUI.TILE_HEIGHT
                    && itembox.getWidth() == GameGUI.TILE_WIDTH;
            item = ImageIO.read(new File("assets/item.png"));
            assert item.getHeight() == GameGUI.TILE_HEIGHT
                    && item.getWidth() == GameGUI.TILE_WIDTH;

        } catch (IOException e) {
            System.out.println("Exception loading images: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /**
     * Updates the current graphics on the screen to display the tiles, player
     * and dropper
     *
     * @param t The 2D array of TileTypes representing the current level of the
     * game
     * @param player The current player object, used to draw the player and its
     * energy
     * @param dropper The dropper to display on the level
     */
    public void update(Tile[][] t, Player player, Dropper dropper) {
        currentTiles = t;
        currentPlayer = player;
        currentDropper = dropper;
        repaint();
    }

    /**
     * Override of method in super class, it draws the custom elements for this
     * game such as the tiles, player and dropper.
     *
     * @param g Graphics drawing object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLevel(g);
    }

    /**
     * Draws graphical elements to the screen to display the current game level
     * tiles, the player and the dropper. If the currentTiles, currentPlayer or
     * currentDropper objects are null they will not be drawn.
     *
     * @param g
     */
    private void drawLevel(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentTiles != null) {
            for (int i = 0; i < currentTiles.length; i++) {
                for (int j = 0; j < currentTiles[i].length; j++) {
                    if (currentTiles[i][j] != null) {
                        switch (currentTiles[i][j].getType()) {
                            case BACKGROUND:
                                g2.drawImage(background, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case FLOOR:
                                g2.drawImage(floor, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case WALL:
                                g2.drawImage(wall, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case DOOR:
                                g2.drawImage(door, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case CEILING:
                                g2.drawImage(ceiling, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case BOX:
                                g2.drawImage(box, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case ITEMBOX:
                                g2.drawImage(itembox, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                            case ITEM:
                                g2.drawImage(item, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                                break;
                        }
                    }
                }
            }
        }
        if (currentDropper != null) {
            g2.drawImage(dropper, currentDropper.getX() * GameGUI.TILE_WIDTH, currentDropper.getY() * GameGUI.TILE_HEIGHT, null);
        }
        if (currentPlayer != null) {
            g2.drawImage(player, currentPlayer.getX() * GameGUI.TILE_WIDTH, currentPlayer.getY() * GameGUI.TILE_HEIGHT, null);
           drawEnergyBar(g2, currentPlayer);
        }
        g2.dispose();
    }

    /**
     * Draws an energy bar for the given Player at the bottom of the tile that
     * the Player is located in.
     *
     * @param g2 The graphics object to use for drawing
     * @param p The Player that the energy bar will be drawn for
     */
    private void drawEnergyBar(Graphics2D g2, Player p) {
        double remainingEnergy = (double) p.getEnergy() / (double) p.getMaxEnergy();
        g2.setColor(Color.BLUE);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, p.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.BAR_HEIGHT));
        g2.setColor(Color.CYAN);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, p.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingEnergy, GameGUI.BAR_HEIGHT));
    }
}
