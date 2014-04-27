package net.gummycraft.wafsChests;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private static int chunkMinX = -11;
	private static int chunkMaxX = 11;
	private static int chunkMinZ = -11;
	private static int chunkMaxZ = 11;
	private static int chestBuffer = 2;
	private static int chestOverallMin = 13;
	private static int chestOverallMax = 120;
	private static int randomChestMin = 4;
	private static int chestPerPlayerMin = 3;
	private static int chestPerPlayerMax = 8;
	private static int slotPerChestMin = 0;
	private static int slotPerChestMax = 6;
	private static double slotPerPlayerMin = 0;
	private static double slotPerPlayerMax = .4;
	

	public static void load(Chests plugin) {
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		FileConfiguration c = plugin.getConfig();
	
		chunkMinX = c.getInt("chunkminx", -11);
		chunkMaxX = c.getInt("chunkmaxx", 11);
		chunkMinZ = c.getInt("chunkminz", -11);
		chunkMaxZ = c.getInt("chunkmaxz", 11);
		chestBuffer = c.getInt("chestbuffer", 2);
		chestOverallMin = c.getInt("chestperchestmin", 13);
		chestOverallMax = c.getInt("chestperchestmax", 120);
		randomChestMin = c.getInt("randomchestmin", 4);
		chestPerPlayerMin = c.getInt("chestperplayermin", 3);
		chestPerPlayerMax = c.getInt("chestperplayermax", 8);
		slotPerChestMin = c.getInt("slotoverallmin", 0);
		slotPerChestMax = c.getInt("slotoverallmax", 6);
		slotPerPlayerMin = c.getDouble("slotperplayermin", 0);
		slotPerPlayerMax = c.getDouble("slotperplayermax", .4);
	}
	
	public static int getSlotPerChestMin() {
		return(slotPerChestMin);
	}
	public static int getSlotPerChestMax() {
		return(slotPerChestMax);
	}
	public static double getSlotPerPlayerMin() {
		return(slotPerPlayerMin);
	}
	public static double getSlotPerPlayerMax() {
		return(slotPerPlayerMax);
	}
	
	public static int getChestOverallMin() {
		return(chestOverallMin);
	}
	public static int getChestOverallMax() {
		return(chestOverallMax);
	}
	public static int getChestPerPlayerMin() {
		return(chestPerPlayerMin);
	}
	public static int getChestPerPlayerMax() {
		return(chestPerPlayerMax);
	}
	public static int getChunkMinX() {
		return( Math.min(chunkMinX, chunkMaxX) );
	}
	public static int getChunkMaxX() {
		return( Math.max(chunkMinX, chunkMaxX) );
	}
	public static int getChunkMinZ() {
		return( Math.min(chunkMinZ, chunkMaxZ) );
	}
	public static int getChunkMaxZ() {
		return( Math.max(chunkMinZ, chunkMaxZ) );
	}
	public static int getChestBuffer() {
		return( chestBuffer );
	}
	public static int getRandomChestMin() {
		return( randomChestMin );
	}
}
