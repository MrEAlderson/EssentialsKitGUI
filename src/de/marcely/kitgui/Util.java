/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3.1
* @website http://marcely.de/ 
*/

package de.marcely.kitgui;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.marcely.kitgui.library.Vault;

public class Util {
	
	public static ArrayList<Kit> getKits(Player player){
		ArrayList<Kit> list = new ArrayList<Kit>();
		for(String kitName:main.es.getSettings().getKits().getKeys(false)){
			
			if(hasPermission(player, "essentials.kits." + kitName) || hasPermission(player, "essentials.kits.*")){
				Kit kit = main.kits.getKit(kitName);
				list.add(kit);
			}
			
		}
		return list;
	}
	
	public static com.earth2me.essentials.Kit getKit(String kitname){
		ConfigurationSection kits = main.es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, main.es);
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
		ConfigurationSection kits = main.es.getSettings().getKits();
		for (String kitItem:kits.getKeys(false)){
			try {
				com.earth2me.essentials.Kit kit = new com.earth2me.essentials.Kit(kitItem, main.es);
				if(kit.getName().equals(kitname)){
					kit.expandItems(main.es.getUser(player));
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static String firstCharCaps(String str){
		if(main.CONFIG_FIRSTCHARCAPS == true)
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
	
	public static boolean hasPermission(CommandSender sender, String permission){
		if(sender instanceof Player){
			
			Player player = (Player) sender;
			Boolean bl = Vault.hasPermission(player, permission);
			if(bl != null)
				return bl;
			else
				return player.hasPermission(permission);
		}else
			return true;
	}
}
