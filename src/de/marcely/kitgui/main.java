package de.marcely.kitgui;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.marcely.kitgui.command.kit;
import de.marcely.kitgui.config.KitConfig;
import de.marcely.kitgui.config.config;
import net.ess3.api.IEssentials;


public class main extends JavaPlugin {
	public static String version = "1.3";
	
	public static IEssentials es = null;
	
	public static String CONFIG_INVTITLE = ChatColor.DARK_AQUA + "Kits";
	public static boolean CONFIG_FIRSTCHARCAPS = false;
	public static LanguageType CONFIG_LANGUAGE = LanguageType.English;
	
	public static KitConfig kits = new KitConfig();
	
	@Override
	public void onEnable(){
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
		
		config.load();
		if(KitConfig.exists()) kits = KitConfig.load();
	}
	
	@Override
	public void onDisable(){
		
	}
	
	private Listener listener = new Listener(){
		@EventHandler
		public void onInventoryClick(InventoryClickEvent event){
			kit.onInventoryClick(event);
		}
	};
	
	public static ArrayList<Kit> getKits(Player player){
		ArrayList<Kit> list = new ArrayList<Kit>();
		ConfigurationSection kits = es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, es);
				if(player.hasPermission("essentials.kits." + kit.getName())){
					Kit k = main.kits.getKit(kit.getName());
					if(k == null)
						list.add(new Kit(kit.getName(), new ItemStack(Material.CLAY_BALL)));
					else
						list.add(new Kit(k.getName(), k.getIcon(), k.getPrefix()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static com.earth2me.essentials.Kit getKit(String kitname){
		ConfigurationSection kits = es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, es);
				if(kit.getName().equals(kitname)){
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
}
