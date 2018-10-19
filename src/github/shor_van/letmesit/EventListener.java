package github.shor_van.letmesit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventListener implements Listener
{
    private static boolean forceSpawn = false; //Set to true when spawning entity to bypass world guard
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
            if(player.isInsideVehicle() == true)
                return;
            
            //Is block a valid chair block and item in hands are not blocks
            if(LetMeSit.isBlockValidChair(block) && event.isBlockInHand() == false && (player.getInventory().getItemInOffHand().getType() == Material.AIR || player.getInventory().getItemInOffHand().getType().isBlock() == false))
            {
                //Check if another player is sitting in the block
                if(((LetMeSit) plugin).isBlockOccupied(block) == true)
                {
                    player.sendMessage(ChatColor.RED + "Someone else is sitting there!");
                    return;
                }
                
                //set up entity's location
                double x = block.getX() + 0.5;
                double y = block.getY() - 0.3;
                double z = block.getZ() + 0.5;
                float yaw = 0.0f;
                BlockData data = block.getState().getBlockData();
                if(data instanceof Stairs)
                {
                    Stairs stairData = (Stairs) data;
                    if(stairData.getFacing() == BlockFace.NORTH)
                        yaw = 0.0f;
                    else if(stairData.getFacing() == BlockFace.SOUTH)
                        yaw = 180.0f;
                    else if(stairData.getFacing() == BlockFace.EAST)
                        yaw = 90.0f;
                    else if(stairData.getFacing() == BlockFace.WEST)
                        yaw = 270.0f;
                }
                
                Location location = new Location(player.getWorld(), x, y, z, yaw, 0.0f);
                
                //Spawn entity and set player as passenger
                Entity entity = player.getWorld().spawnEntity(location, EntityType.PIG);
                
                //Setup entity
                entity.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6ChairPig"));
                entity.setCustomNameVisible(false);
                entity.setInvulnerable(true);
                entity.setSilent(true);
                ((LivingEntity) entity).setAI(false);
                //((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false), true);
                
                //Add to the list
                ((LetMeSit) plugin).getSittingPlayers().add(new SittingPlayer(player, block));
                
                //set player as passenger
                entity.addPassenger(player);
                player.getLocation().setYaw(yaw);
                
                player.sendMessage("You are now sitting.");
            }
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDismount(VehicleExitEvent event)
    {
        LivingEntity entity = event.getExited();
        if(entity instanceof Player)//is the entity that left a player
        {
            Vehicle vehicleEntity = event.getVehicle();
            if(vehicleEntity.getCustomName().equals(ChatColor.translateAlternateColorCodes('&', "&6ChairPig")))//Is it a "chair pig" vehicle entity
            {
                Player player = (Player) entity;
                SittingPlayer sittingPlayer = ((LetMeSit) plugin).getSittingPlayer(player); 
                
                vehicleEntity.remove();
                player.teleport(sittingPlayer.getOriginalLocation());
                ((LetMeSit) plugin).removeSittingPlayer(sittingPlayer);
                
                player.sendMessage("You are nolonger sitting.");
            }
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if(player.isInsideVehicle() == true)//Check if player is in a vehicle
        {
            Entity vehicleEntity = player.getVehicle();
            if(vehicleEntity.getCustomName().equals(ChatColor.translateAlternateColorCodes('&', "&6ChairPig")))//Is it a "chair pig" vehicle entity
            {
                vehicleEntity.remove();
                SittingPlayer sittingPlayer = ((LetMeSit) plugin).getSittingPlayer(player); 
                ((LetMeSit) plugin).removeSittingPlayer(sittingPlayer);
            }
        }
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if(player.isInsideVehicle() == true)//Check if player is in a vehicle
        {   
            Entity vehicleEntity = player.getVehicle();
            if(vehicleEntity.getCustomName().equals(ChatColor.translateAlternateColorCodes('&', "&6ChairPig")))//Is it a "chair pig" vehicle entity
            {
                vehicleEntity.remove();
                SittingPlayer sittingPlayer = ((LetMeSit) plugin).getSittingPlayer(player); 
                ((LetMeSit) plugin).removeSittingPlayer(sittingPlayer);
            }
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        //If event cancelled ignore
        if(event.isCancelled() == true)
            return;
        
    }
    
    //world guard override
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e)
    {
        if (forceSpawn)
            e.setCancelled(false);
    }
}
