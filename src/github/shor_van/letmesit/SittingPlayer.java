package github.shor_van.letmesit;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SittingPlayer
{
    private Player player;
    private Location prevLocation;
    private Block block;
    
    public SittingPlayer(Player player, Block block)
    {
        //Get data
        this.player = player;
        this.prevLocation = player.getLocation();
        this.block = block;
    }
    
    /**Removes the sitting player*/
    public void remove()
    {        
        player = null;
        prevLocation = null;
        block = null;
    }
    
    /**Get the location the player was before they started sitting
     * @return the location the player was before they started sitting*/
    public Location getOriginalLocation() { return prevLocation; }
    
    /**Checks whether the specified block is this block
     * @param block the block to check
     * @return true if the specified block is this block else false if it is not*/
    public boolean isThisBlock(Block block) { return this.block.equals(block); }
    
    /**Checks whether the specified player is this player
     * @param player the player to check
     * @return true if the specified player is this player else false if it is not*/
    public boolean isThisPlayer(Player player) { return this.player.equals(player); }
}
