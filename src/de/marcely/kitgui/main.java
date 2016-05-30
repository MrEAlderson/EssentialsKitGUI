/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.marcely.kitgui.command.kit;
import de.marcely.kitgui.config.KitConfig;
import de.marcely.kitgui.config.Config;
import de.marcely.kitgui.config.LanguageConfig;
import net.ess3.api.IEssentials;

public class main extends JavaPlugin {
	public static Plugin plugin;
	
	public static IEssentials es = null;
	
	public static String CONFIG_INVTITLE = ChatColor.DARK_AQUA + "Kits";
	public static boolean CONFIG_FIRSTCHARCAPS = false;
	public static boolean CONFIG_INCLCMD_KITS = true;
	
	public static KitConfig kits = new KitConfig();
	
	public void onEnable(){
		plugin = this;
		
		// get essentials variable
		es = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if(es == null) // if he isn't using the spigot version of spigot
			es = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("EssentialsX");
		if(es == null) // he isn't using essentials or essentialsX
			new NullPointerException("You aren't using the spigot version of Essentials or EssentialsX!").printStackTrace();
		
		// setup
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("kit").setExecutor(new de.marcely.kitgui.command.kit());
		getCommand("kitcfg").setExecutor(new de.marcely.kitgui.command.kitcfg());
		
		// load config
		File dir = new File("plugins/Essentials_KitGUI");
		if(!dir.exists()) dir.mkdir();
		
		Config.load();
		LanguageConfig.load();
		if(KitConfig.exists()) kits = KitConfig.load();
	}
	
	private Listener listener = new Listener(){
		@EventHandler
		public void onInventoryClick(InventoryClickEvent event){
			kit.onInventoryClickEvent(event);
		}
		
		@EventHandler
		public void onInventoryDragEvent(InventoryDragEvent event){
			kit.onInventoryDragEvent(event);
		}
		
		@EventHandler
		public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
			String[] strs = event.getMessage().split(" ");
			String label = strs[0].replace("/", "");
			String[] args = new String[strs.length - 1];
			
			for(int i=1; i<strs.length; i++)
				args[i - 1] = strs[i];
			
			if(CONFIG_INCLCMD_KITS && label.equalsIgnoreCase("kits")){
				kit.onCommand(event.getPlayer(), label, args);
				event.setCancelled(true);
			}
		}
	};
	
	public static ArrayList<Kit> getKits(Player player){
		ArrayList<Kit> list = new ArrayList<Kit>();
		for(String kitName:es.getSettings().getKits().getKeys(false)){
			
			Kit kit = kits.getKit(kitName);
			list.add(kit);
			
		}
		return list;
	}
	
	public static com.earth2me.essentials.Kit getKit(String kitname){
		ConfigurationSection kits = es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, es);
				if(kit.getName().equalsIgnoreCase(kitname)){
					return kit;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean givePlayerItems(Player player, String kitname){
		ConfigurationSection kits = es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, es);
				if(kit.getName().equals(kitname)){
					kit.expandItems(es.getUser(player));
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static String firstCharCaps(String str){
		if(CONFIG_FIRSTCHARCAPS == true)
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		return str;
	}
	
	public static boolean isInteger(String str){
		try{
			Integer.valueOf(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static String getVersion(){
		return plugin.getDescription().getVersion();
	}
}
