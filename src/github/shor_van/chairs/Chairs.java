package github.shor_van.chairs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Chairs extends JavaPlugin
{
    private List<SittingPlayer> sittingPlayers; //a list of players that are sitting
    private EventListener eventListener; //The plugin's event listener
    
    /**Initiates the plugin*/
	@Override
	public void onEnable()
	{
		eventListener = new EventListener(this);
		sittingPlayers = new ArrayList<SittingPlayer>();
		
		getServer().getPluginManager().registerEvents(eventListener, this);
	}
	
	/**Disables the plugin.*/
	@Override
	public void onDisable()
	{
	    eventListener = null;
	    
	    //Kick all players of chairs
	    for(SittingPlayer sittingPlayer : sittingPlayers)
	        sittingPlayer.remove();;
	    
	    //Clear the list of sitting players and nullify
	    sittingPlayers.clear();
	    sittingPlayers = null;
	}
	
	public SittingPlayer getSittingPlayer(Player player)
	{
       for(SittingPlayer sittingPlayer : sittingPlayers)
            if(sittingPlayer.isThisPlayer(player))
                return sittingPlayer;
       return null;
	}
	
	public void removeSittingPlayer(Player player)
	{
	    SittingPlayer sittingPlayer = getSittingPlayer(player);
	    if(sittingPlayer != null)
	    {
	        sittingPlayer.remove();
	        sittingPlayers.remove(sittingPlayer);
	    }
	}
	
    /**Checks whether the specified block is being used as a chair
     * @param block the block to check
     * @return true if the specified block is being used as a chair*/
	public boolean isBlockOccupied(Block block)
    {
        for(SittingPlayer sittingPlayer : sittingPlayers)
            if(sittingPlayer.isThisBlock(block))
                return true;
        return false;
    }

    /**Removes the specified sitting player from the list
     * @param sittingPlayer the sitting player to remove*/
	public void removeSittingPlayer(SittingPlayer sittingPlayer)
	{
        sittingPlayer.remove();
        sittingPlayers.remove(sittingPlayer);
	}
	
    /**Gets the list of sitting players
     * @return a list of players that are sitting*/
    public List<SittingPlayer> getSittingPlayers() { return sittingPlayers; }
	
    /**Checks whether the specified block can be used as a chair
     * @param block the block to check if it is a valid chair
     * @return true if the specified block is a valid chair false if it is not*/
	public static boolean isBlockValidChair(Block block)
	{
	    BlockData data = block.getState().getBlockData();
	    if(data instanceof Stairs)
	    {
	        if(((Stairs)data).getHalf() == Half.TOP) //Is stair block upside down
	            return false;
	        return true;
	    }
	    return false;
	}
}
