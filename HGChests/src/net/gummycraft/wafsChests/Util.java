package net.gummycraft.wafsChests;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {
	static Random rand = new Random();
	
	static public int findOpenSlot(Inventory inv) {
    	int highest = inv.getSize();
    	
    	for (int watchDog = 0; watchDog < 11; watchDog++) {
    		int slot = rand.nextInt( highest );
			ItemStack is = inv.getItem(slot);
			if ( is == null || is.getType() == Material.AIR ) {
				return(slot);
			}
    	}
    	for (int slot = 0; slot < highest; slot++ ) {
			ItemStack is = inv.getItem(slot);
			if ( is == null || is.getType() == Material.AIR ) {
				return(slot);
			}	
    	}    	
    	return(0);
    }
	
	/*
	 * based on config and num players get the min or max number of chests to populate (or place) on the map
	 */
	static public int getMinChests(int players) {
		int chestTargetMin = Math.max( (Config.getChestPerPlayerMin()*players), Config.getChestOverallMin() );
		int chestTargetMax = Math.min( (Config.getChestPerPlayerMax()*players), Config.getChestOverallMax() );
		
		if ( chestTargetMax < Config.getChestOverallMin() ) chestTargetMax = Config.getChestOverallMin();
		if ( chestTargetMin > Config.getChestOverallMax() ) chestTargetMin = Config.getChestOverallMax();	
		return( chestTargetMin );
	}
	static public int getMaxChests(int players) {
		int chestTargetMin = Math.max( (Config.getChestPerPlayerMin()*players), Config.getChestOverallMin() );
		int chestTargetMax = Math.min( (Config.getChestPerPlayerMax()*players), Config.getChestOverallMax() );
		
		if ( chestTargetMax < Config.getChestOverallMin() ) chestTargetMax = Config.getChestOverallMin();
		if ( chestTargetMin > Config.getChestOverallMax() ) chestTargetMin = Config.getChestOverallMax();	
		return( chestTargetMax );		
	}
	/*
	 * based on config and num players ge the min/max slots to fill per chest
	 */
	static public double getMinSlots(int players) {
		double slotTargetMin = Math.max( (Config.getSlotPerPlayerMin()*(double)players), (double)Config.getSlotPerChestMin() );
		double slotTargetMax = Math.min( (Config.getSlotPerPlayerMax()*(double)players), (double)Config.getSlotPerChestMax() );		
		
		if ( slotTargetMax < (double) Config.getSlotPerChestMin() ) 
			slotTargetMax = (double) Config.getSlotPerChestMin();
		if ( slotTargetMin > (double) Config.getSlotPerChestMax() ) 
			slotTargetMin = (double) Config.getSlotPerChestMax();
		
		return( slotTargetMin );		
	}
	static public double getMaxSlots(int players) {
		double slotTargetMin = Math.max( (Config.getSlotPerPlayerMin()*(double)players), (double)Config.getSlotPerChestMin() );
		double slotTargetMax = Math.min( (Config.getSlotPerPlayerMax()*(double)players), (double)Config.getSlotPerChestMax() );		
		
		if ( slotTargetMax < (double) Config.getSlotPerChestMin() ) 
			slotTargetMax = (double) Config.getSlotPerChestMin();
		if ( slotTargetMin > (double) Config.getSlotPerChestMax() ) 
			slotTargetMin = (double) Config.getSlotPerChestMax();
		
		return( slotTargetMax );		
	}

	
	/*
	 * This starts on top and goes down looking for a valid spot to put a chest...
	 *    there are "skip" blocks, when it finds those it just continues looking for
	 *    once it finds a block not on the 'skip' list it verfies the blocks is
	 *    good enough to place a chest on
	 * 
	 */
	static int findSolidSurface(Block b, int lowestY) {
		// from starting point...go down until we find something not on skip list with air above it
		while ( isSkipList(b.getType()) == true && b.getY() > lowestY) {
			b = b.getRelative(BlockFace.DOWN);
		}
		
		// b is out of bounds, not a suitable base, above is not air ... column is not good
		if ( b.getY() <= lowestY || !isSuitableBase( b.getType() ) ||
				b.getRelative(BlockFace.UP).getType() != Material.AIR ) {
			return(-1);
		}		
		return( b.getY()+1 );
	}
	
	/*
	 *  just checks if a material is on the skip list (air and leaves right now)
	 */
	static private boolean isSkipList(Material mat) {		
		switch ( mat ) {
			case AIR: 
			case LEAVES:
			case LEAVES_2:
			case HUGE_MUSHROOM_1:
			case HUGE_MUSHROOM_2:
				return(true);
			default:
				break;
		}
		return(false);		
	}
	
	/*
	 * this list should be in the config file somewhere, sometime, but don't care right now
	 * 
	 * just a simple check to see if a location type is suitable to put a chest on
	 *   Tries to have some sense of where a chest can be, but it's not perfect unlike me
	 * 
	 */
	static private boolean isSuitableBase(Material mat) {
		switch ( mat ) {
			case LAVA:
			case STATIONARY_LAVA:
			case FIRE:
				
			case WATER:
			case STATIONARY_WATER:
				
			case CACTUS:
			case CROPS:
			case DEAD_BUSH:
			case LONG_GRASS:
				
			case TORCH:
			case STONE_BUTTON:
			case WOOD_BUTTON:
			case REDSTONE_TORCH_ON:
			case REDSTONE_TORCH_OFF:
			case TRIPWIRE_HOOK:
			case LEVER:
			case LADDER:
			case VINE:
			case WOOD_DOOR:
			case IRON_DOOR:
			case TRAP_DOOR:
			case COBBLE_WALL:
			case WEB:
			case WATER_LILY:
			case IRON_FENCE:
			case FENCE:
			case NETHER_FENCE:
			case FENCE_GATE:
			case SIGN_POST:
			case WALL_SIGN:
				
			case TRIPWIRE:
			case GOLD_PLATE:
			case IRON_PLATE:
			case WOOD_PLATE:
			case STONE_PLATE:
				return(false);
			default:
				break;
		}
		return(true);
	}
}
