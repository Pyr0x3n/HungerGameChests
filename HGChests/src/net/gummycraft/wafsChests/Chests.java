package net.gummycraft.wafsChests;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class Chests extends JavaPlugin {
	Random rand = new Random();
	static Logger log;
	
	
	@Override
	public void onEnable() {
		log = this.getLogger();
		
		Config.load(this);
		
		if ( Bukkit.getServer().getPluginManager().getPlugin("LibsHungergames") == null ) {
			log.info("LibsHungergames not found, using commands only");
		} else {
			log.info("LibsHungergames found, using game listeners");
			new LibHungerGameListener(this);
			storeNaturalChests();
			createMoreLocations();			
		}
	}
	
	@Override
	public void onDisable() {
		restore();
	}
	

	
		
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if ( args.length > 0 && args[0].equalsIgnoreCase("load")) {
    		if ( ChestKeeper.size() > 0 ) {
    			sender.sendMessage(ChatColor.RED + "Chests are already loaded");
    		} else {
        		storeNaturalChests();
        		createMoreLocations();
    		}
    		
    	} else if ( args.length > 0 && args[0].equalsIgnoreCase("start")) {
    		int num = 8;
    		if ( args.length > 1)
    			num = Integer.parseInt(args[1]);
    		if ( num < 2 )
    			num = 2;
    		PopulateWorld.go(num);
    		
    	} else if ( args.length > 0 && args[0].equalsIgnoreCase("restore")) {
    		restore();
    		
    	} else {
    		sender.sendMessage("/chest < load | start [players] | restore>");
    		
    	}
		return false;
    }

	/*
	 * Scans chunk -11,-11 to 11,11 and stores all the Trapped chests along with their contents
	 * removing them along the way. If enough are found these are the locations used for chests
	 * scattered on the map. The contents (if there are any) will be put in chests as they are
	 * placed and will not be filled with additional contents from the configuration. 
	 */
	public void storeNaturalChests() {
		World w = Bukkit.getWorlds().get(0);
		
		long start = System.currentTimeMillis();
		for (int x=Config.getChunkMinX(); x < Config.getChunkMaxX(); x++) {
			for (int z=Config.getChunkMinZ(); z < Config.getChunkMaxZ(); z++) {
				Chunk c = w.getChunkAt(x, z);
				c.load(true);
				BlockState[] tiles = c.getTileEntities();
				for ( BlockState tile : tiles ) {
					if ( tile.getType() == Material.TRAPPED_CHEST ) {
						ChestKeeper ck = new ChestKeeper( tile.getLocation(), true );
						
						Inventory inv = ((Chest) tile).getInventory();
						int invSize = inv.getSize();
						for (int slot = 0; slot < invSize; slot++) {
							ItemStack is = inv.getItem(slot);
							if ( is != null && is.getType() != Material.AIR ) {
								ck.saveStack(is);
							}
						}
						inv.clear();
						Block b = tile.getBlock();
						b.setType(Material.AIR);
					}
				}
			}
		}
		long end = System.currentTimeMillis();
		log.info("Stored " + ChestKeeper.size() + " chests in " + (end-start) + " milliseconds");
	}

	/*
	 * This finds "valid" locations to store more chests up to the chestOverallMax count...which we will fill later
	 *    (maybe) at some point either just delay, or break it into chunks over several ticks .. right now, idgaf
	 */
	public void createMoreLocations() {
		long start = System.currentTimeMillis();
		int needed = Config.getChestOverallMax();
		int xBase= Config.getChunkMaxX() - Config.getChunkMinX();
		int xOffset = Config.getChunkMinX();
		int zBase= Config.getChunkMaxZ() - Config.getChunkMinZ();
		int zOffset = Config.getChunkMinZ();
		World w = Bukkit.getWorlds().get(0);
				
		// loop until we find enough chest locations
		int watchDog = 0;
		int bufferZone = Config.getChestBuffer();
		while ( needed > ChestKeeper.size() && watchDog < (needed*2) ) {
			// get a random chunk - within boundries and outside of buffer if there is one
			int cx;
			int cz;
			int miniWatchdog = 0;
			do {
				cx = rand.nextInt(xBase) + xOffset;
				cz = rand.nextInt(zBase) + zOffset;			     
			} while (  (Math.abs(cx) < bufferZone || Math.abs(cz) < bufferZone) && ++miniWatchdog < 10  );
			Chunk c = w.getChunkAt(cx, cz);
			
			// inside this chunk, look up to 20 times to find a block x,y,z that works
			for (int smallWatchDog = 0; smallWatchDog < 8; smallWatchDog++ ) {
				int bx = rand.nextInt(16);
				int bz = rand.nextInt(16);
				
				Block b = c.getBlock(bx,  120,  bz);
				int by = Util.findSolidSurface(b, 45);
				
				if ( by > 0 ) {
					new ChestKeeper ( c.getBlock(bx,  by,  bz).getLocation(), false );
					break;
				}
			}
			watchDog++;
		}
		long end = System.currentTimeMillis();
		log.info("Created " + (ChestKeeper.size()-ChestKeeper.getNaturalSize()) + " additional locations in " + (end-start) + " milliseconds");
	}
    
    
    /*
     * This puts the world (at least where the chests are and were) back to the way we found it
     * doesn't matter when running lib's hungergames as the world is recopied each time, but if used
     * for maps that don't reset this will restore back so it can run again.
     */
    public void restore() {
    	for ( ChestKeeper ck : ChestKeeper.getList() ) {
    		Block b = ck.getLocation().getBlock();
    		if ( b.getType() == Material.TRAPPED_CHEST || b.getType() == Material.CHEST ) {
        		Inventory inv = ((Chest) b.getState()).getInventory();
        		inv.clear();
    		}
    		if ( ck.isNatural() == false ) {
    			b.setType(Material.AIR);
    		} else {
        		b.setType(Material.TRAPPED_CHEST);
        		Inventory inv = ((Chest) b.getState()).getInventory();
        		
        		ItemStack[] stacks = ck.getStacks();
        		for ( ItemStack is : stacks ) {
        			int slot = Util.findOpenSlot(inv);
        			inv.setItem(slot, is );
        		}    			
    		}
    	}
    	ChestKeeper.clear();
    }
}
