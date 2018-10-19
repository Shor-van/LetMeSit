package github.shor_van.letmesit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LetMeSit extends JavaPlugin
{
    private List<SittingPlayer> sittingPlayers;
    private EventListener eventListener;
    
	@Override
	public void onEnable()
	{
		eventListener = new EventListener(this);
		sittingPlayers = new ArrayList<SittingPlayer>();
		
		getServer().getPluginManager().registerEvents(eventListener, this);
	}
	
	@Override
	public void onDisable()
	{
	    //Kick all players of chairs
	    for(SittingPlayer sittingPlayer : sittingPlayers)
	        sittingPlayer.remove();
	    sittingPlayers.clear();
	    sittingPlayers = null;
	    
		eventListener = null;
	}
	
	public boolean isPlayerSitting(Player player)
	{
	    for(SittingPlayer sittingPlayer : sittingPlayers)
	        if(sittingPlayer.isThisPlayer(player))
	            return true;
	    return false;
	}
	
	public boolean isBlockOccupied(Block block)
	{
	    for(SittingPlayer sittingPlayer : sittingPlayers)
            if(sittingPlayer.isThisBlock(block))
                return true;
	    return false;
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
	
    public List<SittingPlayer> getSittingPlayers() { return sittingPlayers; }
	
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
