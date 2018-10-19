package github.shor_van.letmesit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        
        //set player as passenger
        entity.addPassenger(player);
        player.getLocation().setYaw(yaw);
    }
    
    /**Removes the sitting player*/
    public void remove()
    {
        //clear entity and set player location
        player.getVehicle().remove();
        player.teleport(prevLocation);
        
        player = null;
        prevLocation = null;
        block = null;
    }
    
    /**Checks whether the specified block is this block
     * @param block the block to check
     * @return true if the specified block is this block else false if it is not*/
    public boolean isThisBlock(Block block) { return this.block.equals(block); }
    
    /**Checks whether the specified player is this player
     * @param player the player to check
     * @return true if the specified player is this player else false if it is not*/
    public boolean isThisPlayer(Player player) { return player.equals(player); }
}
