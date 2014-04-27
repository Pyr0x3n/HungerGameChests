package net.gummycraft.wafsChests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ChestKeeper {
	private static List<ChestKeeper> chestList= new ArrayList<ChestKeeper>();
	private static int naturalCount = 0;
	private Location loc;
	private boolean natural;
	private boolean placed = false;
	private List<ItemStack> stackList = new ArrayList<ItemStack>();
	
	ChestKeeper(Location loc, boolean natural) {
		this.loc = loc;
		this.natural = natural;
		chestList.add(this);
		if ( natural ) {
			naturalCount++;
		}
	}
	
	public static int size() {
		return( chestList.size());
	}
	public static ChestKeeper[] getList() {
		return chestList.toArray(new ChestKeeper[chestList.size()]);
	}
	public static void clear() {
		for ( ChestKeeper ck : chestList ) {
			ck.clearContents();
		}
		chestList.clear();
		naturalCount = 0;
	}
	public static int getNaturalSize() {
		return(naturalCount);
	}
	
	public void clearContents() {
		stackList.clear();
	}
	public void saveStack(ItemStack is) {
		stackList.add( is.clone() );
	}
	public ItemStack[] getStacks() {
		return stackList.toArray(new ItemStack[stackList.size()]);
	}
	public List<ItemStack> getStackClone() {
		List<ItemStack> stackClone = new ArrayList<ItemStack>();
		for ( ItemStack is : stackList ) {
			stackClone.add( is.clone() );
		}
		return(stackClone);
	}
	public Location getLocation() {
		return(this.loc);
	}
	public boolean isNatural() {
		return(this.natural);
	}
	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
	public boolean isPlaced() {
		return(this.placed);
	}
}
