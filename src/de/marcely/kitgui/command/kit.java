package de.marcely.kitgui.command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.User;

import de.marcely.kitgui.Kit;
import de.marcely.kitgui.language;
import de.marcely.kitgui.main;

public class kit implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				if(player.hasPermission("essentials.kit")){
					player.openInventory(getKitInventory(player));
				}else{
					sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
				}
			}else{
				String kitname = args[0];
				com.earth2me.essentials.Kit kit = main.getKit(kitname.toLowerCase());
				if(kit != null){
					if(sender.hasPermission("essentials.kits." + kitname.toLowerCase()))
						giveKit(player, kit);
					else
						sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
				}else{
					player.sendMessage(ChatColor.DARK_RED + language.theKit +  " " + ChatColor.RED + args[0] + ChatColor.DARK_RED + " " + language.doesntExists);
				}
			}
		}else{
			sender.sendMessage(ChatColor.DARK_RED + language.notAPlayer);
		}
		return true;
	}
	
	
	public static void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack is = event.getCurrentItem();
		if(inv.getTitle() != null && inv.getTitle() == main.CONFIG_INVTITLE && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			player.closeInventory();
			
			String name = is.getItemMeta().getDisplayName().toLowerCase();
			com.earth2me.essentials.Kit kit = main.getKit(name);
			
			giveKit(player, kit);
		}
	}
	
	public static void giveKit(Player player, com.earth2me.essentials.Kit kit){
		User user = main.es.getUser(player);
		
		// check, if he is allowed to
		try { kit.checkDelay(user); } catch (Exception e) { return; }
		
		player.sendMessage(ChatColor.DARK_GREEN + language.givingYouThe + " " + ChatColor.GREEN + main.firstCharCaps(kit.getName()) + ChatColor.DARK_GREEN + " " + language.kit);
		
		// give items
		try { kit.expandItems(user); } catch (Exception e) { }
		
		// add to the scheduler from essentials
		try { kit.setTime(user); } catch (Exception e) { e.printStackTrace(); }
	}
	
	public Inventory getKitInventory(Player player){
		ArrayList<Kit> kits = main.getKits(player);
		Inventory inv = Bukkit.createInventory(player, getInvSize(kits.size()), main.CONFIG_INVTITLE);
		for(Kit kit:kits){
			ItemStack is = kit.getIcon();
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', kit.getPrefix() + main.firstCharCaps(kit.getName())));
			is.setItemMeta(im);
			
			inv.addItem(is);
		}
		return inv;
	}
	
	public int getInvSize(int size){
		for(int i=1; i<=10; i++){
			if(size >= i*9-9 && size < i*9)
				return i*9;
		}
		return 10*9;
	}
}
