package github.shor_van.letmesit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener
{
    private final JavaPlugin plugin;
    
    public EventListener(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //check if it a right click on block with main hand
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND)
        {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            
            //Is block a valid chair block and item in hands are not blocks
            if(LetMeSit.isBlockChair(block) && event.isBlockInHand() == false && (player.getInventory().getItemInOffHand().getType() == Material.AIR || player.getInventory().getItemInOffHand().getType().isBlock() == false))
            {
                player.sendMessage("Interact");
            }
        }
    }
}
