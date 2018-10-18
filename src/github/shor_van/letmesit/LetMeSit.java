package github.shor_van.letmesit;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
	
	public static boolean isBlockChair(Block block)
	{
	    Material material = block.getType();
	    if(material == Material.OAK_STAIRS)
	        return true;
	    return false;
	}
}
