package net.gummycraft.wafsChests;

import me.libraryaddict.Hungergames.Events.GameStartEvent;
import me.libraryaddict.Hungergames.Managers.PlayerManager;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class LibHungerGameListener implements Listener {

	public LibHungerGameListener(Chests plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onLibGameStart(GameStartEvent event) {
		PlayerManager pm = HungergamesApi.getPlayerManager();
		int numPlayers = pm.getGamers().size();
		PopulateWorld.go(numPlayers);
	}
}
