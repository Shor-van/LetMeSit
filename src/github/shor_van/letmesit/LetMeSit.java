package github.shor_van.letmesit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.plugin.java.JavaPlugin;

public class LetMeSit extends JavaPlugin
{
    private EventListener eventListener;
    
	@Override
	public void onEnable()
	{
		eventListener = new EventListener(this);
		
		getServer().getPluginManager().registerEvents(eventListener, this);
	}
	
	@Override
	public void onDisable()
	{
		eventListener = null;
	}
	
	public static boolean isBlockValidChair(Block block)
	{
	    BlockData data = block.getState().getBlockData();
	    if(data instanceof Stairs)
	    {
	        if(((Stairs)data).getFacing() == BlockFace.DOWN) //Is stair block upside down
	            return false;
	            
            Material material = block.getType();
            if(material == Material.OAK_STAIRS)
                return true;
	    }
	    return false;
	}
}
