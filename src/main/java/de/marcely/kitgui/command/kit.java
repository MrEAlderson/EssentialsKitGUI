/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.marcely.kitgui.Kit;
import de.marcely.kitgui.Language;
import de.marcely.kitgui.Util;
import de.marcely.kitgui.EssentialsKitGUI;

public class kit implements CommandExecutor {
	private static int MAXPERPAGE = 36;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		onCommand(sender, label, args);
		return true;
	}
	
	public static void onCommand(CommandSender sender, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				if(Util.getKits(player).size() >= 1)
					player.openInventory(getKitInventory(player, 1));
				else
					player.sendMessage(Language.No_Kits.getMessage());
			}else{
				Kit kit = EssentialsKitGUI.kits.getKit(args[0]);
				if(kit != null){
					if(Util.hasPermission(sender, "essentials.kits." + kit.getName().toLowerCase()) || Util.hasPermission(sender, "essentials.kits.*"))
						kit.give(player);
					else
						sender.sendMessage(Language.No_Permissions.getMessage());
				}else{
					player.sendMessage(Language.DoesntExist_Kit.getMessage().replace("{kit}", args[0]));
				}
			}
		}else{
			sender.sendMessage(Language.NotA_Player.getMessage());
		}
	}
	
	
	public static void onInventoryClickEvent(InventoryClickEvent event){
		final Player player = (Player) event.getWhoClicked();
		final ItemStack is = event.getCurrentItem();
		
		InventoryView inventario = event.getView();
		
		if(inventario != null && inventario.getTitle() != null && inventario.getTitle().startsWith(EssentialsKitGUI.CONFIG_INVTITLE) && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			
			if(is == null || is.getType() == null || is.getType() == Material.AIR)
				return;
			
			// get page
			int page = 1;
			String p = inventario.getTitle().replace(EssentialsKitGUI.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA, "");
			if(Util.isInteger(p))
				page = Integer.valueOf(p);
			
			// get kit
			Kit kit = getKitAt(
					player, 
					event.getSlot(), 
					page);
			
			// give
			if(kit != null){
				player.closeInventory();
				kit.give(player);
			}
			
			// change page if --> or <--
			else{
				String name = Util.getItemStackName(is);
				
				if(name != null){
					
					int newPage = page;
					
					if(name.equals(ChatColor.GREEN + "-->"))
						newPage++;
					else if(name.equals(ChatColor.RED + "<--"))
						newPage--;
					
					// change page
					if(newPage != page){
						
						Inventory newInv = getKitInventory(player, newPage);
						player.openInventory(newInv);
					}
				}
			}
		}
	}
	
	public static void onInventoryDragEvent(InventoryDragEvent event){
		InventoryView inventario = event.getView();
		
		if(inventario.getTitle() != null && inventario.getTitle().startsWith(EssentialsKitGUI.CONFIG_INVTITLE))
			event.setCancelled(true);
	}
	
	public static Inventory getKitInventory(Player player, int page){
		List<Kit> kits = Util.getKits(player);
		Inventory inv = null;
		if(kits.size() > MAXPERPAGE)
			inv = Bukkit.createInventory(player, getInvSize(kits.size()), EssentialsKitGUI.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA + page);
		else
			inv = Bukkit.createInventory(player, getInvSize(kits.size()), EssentialsKitGUI.CONFIG_INVTITLE);
		
		for(ItemStack is:getKitsByPage(player, page))
			inv.addItem(is);
		if(page < (double) kits.size() / MAXPERPAGE && inv.getSize() > MAXPERPAGE + 17)
			inv.setItem(MAXPERPAGE + 17, Util.getItemStack(new ItemStack(Material.LEGACY_STAINED_CLAY, 1, (short) 5), ChatColor.GREEN + "-->"));
		if(page > 1 && inv.getSize() > MAXPERPAGE + 9)
			inv.setItem(MAXPERPAGE + 9, Util.getItemStack(new ItemStack(Material.LEGACY_STAINED_CLAY, 1, (short) 14), ChatColor.RED + "<--"));
		if(kits.size() > MAXPERPAGE){
			for(int i=MAXPERPAGE; i<MAXPERPAGE + 9; i++)
				inv.setItem(i, Util.getItemStack(new ItemStack(Material.LEGACY_STAINED_GLASS_PANE, 1, (short) 15), " "));
		}
		return inv;
	}
	
	private static List<ItemStack> getKitsByPage(Player player, int page){
		List<ItemStack> warps = new ArrayList<ItemStack>();
		int c = 1;
		for(ItemStack is:getKits(player)){
			if(c >= (page - 1) * MAXPERPAGE && c <= page * MAXPERPAGE){
				warps.add(is);
			}
			c++;
		}
		return warps;
	}
	
	private static List<ItemStack> getKits(Player player){
		List<ItemStack> warps = new ArrayList<ItemStack>();
		
		for(Kit warp:Util.getKits(player)){
			ItemStack is = warp.getIcon();
			ItemMeta im = is.getItemMeta();
			
			// name
			im.setDisplayName(ChatColor.WHITE + Language.stringToChatColor(warp.getPrefix() + Util.firstCharCaps(warp.getName())));
			
			// lores
			List<String> lores = new ArrayList<String>();
			for(String lore:warp.getLores())	
				lores.add(ChatColor.GRAY + Language.stringToChatColor(lore));
			im.setLore(lores);
			
			is.setItemMeta(im);
			warps.add(is);
		}
		return warps;
	}
	
	public static int getInvSize(int size){
		for(int i=1; i<=10; i++){
			if(size >= i*9-9 && size < i*9)
				return i*9;
		}
		return 10*9;
	}
	
	public static Kit getKitAt(Player player, int at, int page){
		List<Kit> kits = Util.getKits(player);
		
		if(at > MAXPERPAGE)
			return null;
		
		int slot = (page - 1) * MAXPERPAGE + at;
		if(page > 1)
			slot--;
		
		if(slot < kits.size())
			return kits.get(slot);
		else
			return null;
	}
}
