package uk.ac.bradford.dropgame;

/**The Dropper class is a subclass of Entity and implements the
 * dropper concept in the game. Droppers move along the ceiling of
 * the level and occasionally create boxes in the tile below them (see
 * tasks in coursework specification for more details).
 *
 * @author prtrundl
 */
public class Dropper extends Entity {
    
    /**
     * This constructor is used to create a Dropper object to use in the game.
     * @param x the starting X position of this Dropper in the level
     * @param y the starting Y position of this Dropper in the level
     */
    public Dropper(int x, int y) {
        setPosition(x, y);
    }
}
