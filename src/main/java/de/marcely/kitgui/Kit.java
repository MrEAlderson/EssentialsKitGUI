/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.kitgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.User;

public class Kit implements Serializable {
	private static final long serialVersionUID = 100042053024876811L;
	
	private String name;
	private String iconName;
	private short iconID;
	private String prefix = "";
	private List<String> lores = new ArrayList<String>();
	
	public Kit(String name, ItemStack icon){
		setName(name);
		setIcon(icon);
	}
	
	public Kit(String name, ItemStack icon, String prefix){
		setName(name);
		setIcon(icon);
		setPrefix(prefix);
	}
	
	public void addLore(String lore){
		this.lores.add(lore);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setIcon(ItemStack icon){
		this.iconName = icon.getType().name();
		this.iconID = icon.getDurability();
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
	public String getName(){
		return this.name;
	}
	
	public ItemStack getIcon(){
		return new ItemStack(Material.getMaterial(this.iconName), 1, this.iconID);
	}
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public List<String> getLores(){
		return this.lores;
	}
	
	public boolean removeLore(String lore){
		return this.lores.remove(lore);
	}
	
	public void give(Player player){
		final com.earth2me.essentials.Kit kit = Util.getKit(getName());
		final User user = EssentialsKitGUI.es.getUser(player);
		
		// check, if he is allowed to
		try { kit.checkDelay(user); } catch (Exception e) { return; }
		
		player.sendMessage(Language.Giving.getMessage().replace("{kit}", Util.firstCharCaps(kit.getName())));
		
		// give items
		try { kit.expandItems(user); } catch (Exception e) { }
		
		// add to the scheduler from essentials
		try { kit.setTime(user); } catch (Exception e) { e.printStackTrace(); }
	}
}
