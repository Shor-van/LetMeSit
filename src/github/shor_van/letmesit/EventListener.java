package github.shor_van.letmesit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener
{
    private final JavaPlugin plugin; //Reference to the base plugin, should not be null
    
    public EventListener(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    /**Triggered when a player interacts with anything, this method will look for interaction with a block
     * @param event the PlayerInteractEvent that was triggered*/
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //check if it a right click on block with main hand
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND)
        {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            
            //if player is sitting ignore
            
            //Is block a valid chair block and item in hands are not blocks
            if(LetMeSit.isBlockValidChair(block) && event.isBlockInHand() == false && (player.getInventory().getItemInOffHand().getType() == Material.AIR || player.getInventory().getItemInOffHand().getType().isBlock() == false))
            {
                //hold player's location
                
                //Spawn entity and set player as passenger
                Entity entity = player.getWorld().spawnEntity(new Location(player.getWorld(), block.getX() + 0.5, block.getY() - 0.1, block.getZ() + 0.5, 0.0f, 0.0f), EntityType.PIG);
                entity.setInvulnerable(true);
                entity.setSilent(true);
                ((LivingEntity) entity).setAI(false);
                entity.addPassenger(player);
                
                player.sendMessage("You are now sitting.");
            }
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDismount(VehicleExitEvent event)
    {
        LivingEntity entity = event.getExited();
        if(entity instanceof Player)
        {
            Player player = (Player) entity;
            player.sendMessage("You are nolonger sitting.");
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerQuitEvent event)
    {
        
    }
}
