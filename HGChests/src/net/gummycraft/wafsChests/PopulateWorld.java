package net.gummycraft.wafsChests;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import me.libraryaddict.Hungergames.Interfaces.ChestManager;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PopulateWorld {
	static Random rand = new Random();
	static Logger log = Chests.log;
	
	
	public static void go(int players) {
		if ( ChestKeeper.size() < Config.getChestOverallMax() ) {
			Bukkit.broadcastMessage(ChatColor.RED + "Chest error, attempted to populate without sufficient chests");
			return;
		}
		// chests to place: min, max and mean
		int min = Util.getMinChests(players);
		int max = Util.getMaxChests(players);
		int target = (min+max)/2;
		// how many natural and unnatural there are, and the percent of each to place
		int natural = ChestKeeper.getNaturalSize();
		float naturalPercent;
		int unnatural = ChestKeeper.size() - natural;
		float unnaturalPercent;
		// number placed thus far
		int placedChests = 0;
		// total slots filled
		int slotsFilled = 0;
		
		// min, max and percent # of slots to fill per chest
		int minSlots = (int) (  Util.getMinSlots(players) + .9999 );
		int maxSlots = (int) (  Util.getMaxSlots(players) + .9999 );
		double pctSlots = (Util.getMinSlots(players) + Util.getMaxSlots(players)) / 54;
		
		if ( natural < target ) {
			naturalPercent = 1;
			unnaturalPercent = (float) (target-natural) / (float) unnatural ;		
		} else {
			naturalPercent = (float) target / (float) natural;
			unnaturalPercent = 0;
		}

		// this may need multiple cycles to get valid numbers...but probably not
		while ( placedChests < min ) {
			// cycle through all the chests we stored
			for ( ChestKeeper ck : ChestKeeper.getList() ) {
				// are we using this chest?
				if ( (   (ck.isNatural() == true && rand.nextFloat() < naturalPercent) ||
						(ck.isNatural() == false && rand.nextFloat() < unnaturalPercent ) )   &&
						ck.isPlaced() == false ) {
					// if so, then fill (which places) then count it
					slotsFilled += fillChest( ck, minSlots, maxSlots, pctSlots);
					placedChests++;
				}
				if ( placedChests >= max )
					break;
			}
		}
		log.info( "Chests: " + min + " [goal=" +  target + "] " + max + " = " + placedChests);
		log.info( "Slots: " + minSlots + " [goal=" +  String.format("%.4f", pctSlots*27) + "] " + maxSlots + " = " + slotsFilled);
	}
	
	
	/*
	 * This fills a chest that has already been placed .. returns the number slots filled
	 */
	static private int fillChest(ChestKeeper ck, int min, int max, double percent) {
		int filledSlots = 0;

		// mark as chest placed
		ck.setPlaced(true);
		// make the block a chest now
		Block b = ck.getLocation().getBlock();
		b.setType(Material.CHEST);
		// get the inventory object
		Inventory inv = ((Chest) b.getState()).getBlockInventory();
		int invSize = inv.getSize();
		// if there was an inventory before, get the list now
		List<ItemStack> originalStacks = ck.getStackClone();

		// cycle until we have enough at least min number of slots full (should only be 1 virtually all the time)
		for ( int watchDog = 0; watchDog < 5; watchDog++ ) {
			// cycle through the inventory slots
			for (int slot = 0; slot < invSize; slot++) {
				ItemStack is = inv.getItem(slot);
				if ( is == null || is.getType() == Material.AIR ) {
					if ( rand.nextFloat() < percent ) {
						filledSlots++;
						is = getSlotStack(originalStacks);
						inv.setItem(slot, is);
					}
				}
				if ( filledSlots >= max ) // not in for loop? after thought? testing? oh well.
					break;
			}
			if ( filledSlots >= min ) // why didn't i put this in the for loop? was it an afterthought?
				break;
		}
		return(filledSlots);
	}

	
	/*
	 * finds something to put in this one slot
	 */
	private static ItemStack getSlotStack(List<ItemStack> originalStacks) {
		int size = originalStacks.size();
		ItemStack is;
		
		if ( size > 0 ) {
			int i = rand.nextInt( size );
			is = originalStacks.get(i);
			originalStacks.remove(i);
			return(is);
		} else {
			ChestManager cm = HungergamesApi.getChestManager();
			Inventory inv = Bukkit.createInventory(null,  27);
			cm.fillChest(inv);
			
			int invSize = inv.getSize();
			for (int slot = 0; slot < invSize; slot++) {
				is = inv.getItem(slot);
				if ( is != null && is.getType() != Material.AIR ) {
					return(is);
				}
			}
		}
		
		is = new ItemStack(Material.LEATHER_BOOTS, 1);
		return(is);
	}
	

	
	

}
