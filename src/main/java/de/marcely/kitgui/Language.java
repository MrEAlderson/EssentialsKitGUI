/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.kitgui;

import java.util.HashMap;

import org.bukkit.ChatColor;

public enum Language {
	No_Permissions(ChatColor.RED + "You have got no permissions for this command!"),
	No_Prefix(ChatColor.RED + "The kit " + ChatColor.DARK_RED + "{kit} " + ChatColor.RED + "has no prefix"),
	No_Kits(ChatColor.RED + "There're no avaible kits for you!"),
	DoesntExist_Kit(ChatColor.RED + "The kit " + ChatColor.DARK_RED + "{kit} " + ChatColor.RED + "doesn't exist"),
	NotA_Player(ChatColor.RED + "You are not a player!"),
	NotA_Number(ChatColor.DARK_RED + "{number} " + ChatColor.RED + "is not a number!"),
	Changed_Icon(ChatColor.GREEN + "The icon has been changed to " + ChatColor.DARK_GREEN + "{icon}"),
	Changed_Prefix(ChatColor.GREEN + "The prefix by the kit " + ChatColor.DARK_GREEN + "{kit}" + ChatColor.GREEN + " has been changed"),
	Removed_Prefix(ChatColor.GREEN + "The prefix by the kit " + ChatColor.DARK_GREEN + "{kit}" + ChatColor.GREEN + " has been removed"),
	Added_Lore(ChatColor.GREEN + "A lore has been added to the kit " + ChatColor.DARK_GREEN + "{kit}"),
	Removed_Lore(ChatColor.GREEN + "The lore with the ID " + ChatColor.DARK_GREEN + "{id} " + ChatColor.GREEN + "has been removed from the kit " + ChatColor.DARK_GREEN + "{kit}"),
	Unkown_Material(ChatColor.RED + "Unkown material " + ChatColor.DARK_RED + "{material}"),
	Unkown_Argument(ChatColor.RED + "Unkown argument " + ChatColor.DARK_RED + "{arg}"),
	Unkown_ID(ChatColor.RED + "Unkown ID " + ChatColor.DARK_RED + "{id}"),
	Usage(ChatColor.GOLD + "Usage: " + ChatColor.YELLOW + "{usage}"),
	Usage_Add_Lore(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to add a lore"),
	Usage_Remove_Lore(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to remove a lore"),
	Usage_Change_Prefix(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to change the prefix"),
	Usage_Remove_Prefix(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to remove the prefix"),
	Reloaded_Config(ChatColor.GREEN + "The config has been successfully reloaded!"),
	Giving(ChatColor.GOLD + "You got the kit " + ChatColor.YELLOW + "{kit}" + ChatColor.GOLD + "!"),
	Giving_Cooldown(ChatColor.GOLD + "Wait " + ChatColor.YELLOW + "{time} " + ChatColor.GOLD + " until you can take the kit " + ChatColor.YELLOW + "{kit} " + ChatColor.GOLD + "again!"),
	Info_MadeBy(ChatColor.GREEN + "Made by " + ChatColor.DARK_GREEN + "{info}"),
	Info_Version(ChatColor.GREEN + "Version " + ChatColor.DARK_GREEN + "{info}");
	
	private String selected_msg;
	private static HashMap<Language, String> translations = new HashMap<Language, String>();
	
	private Language(String msg){
		this.selected_msg = msg;
	}
	
	public String getMessage(){
		if(translations.containsKey(this))
			return translations.get(this);
		else
			return this.selected_msg;
	}
	
	public static String chatColorToString(String str){
		for(ChatColor c:ChatColor.values()){
			str = str.replace("" + c, "&" + c.getChar());
		}
		
		return str;
	}
	
	public static String stringToChatColor(String str){
		for(ChatColor c:ChatColor.values()){
			str = str.replace("&" + c.getChar(), "" + c);
		}
		
		return str;
	}
	
	public static Language getLanguage(String str){
		for(Language l:Language.values()){
			if(l.name().equalsIgnoreCase(str) ||
			   l.getMessage().equalsIgnoreCase(str))
				return l;
		}
		
		return null;
	}
	
	public static void setTranslation(Language language, String message){
		if(language == null || message == null){
			new NullPointerException().printStackTrace();
			return;
		}
		if(translations.containsKey(language))
			translations.replace(language, message);
		else
			translations.put(language, message);
	}
}
