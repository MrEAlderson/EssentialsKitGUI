/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.config;

import de.marcely.kitgui.Language;
import de.marcely.kitgui.EssentialsKitGUI;

public class Config {
	public static ConfigManager manager = new ConfigManager("Essentials_KitGUI", "config.yml");
	
	public static void load(){
		manager.load();
		String version = manager.getConfigString("config-version");
		String invtitle = manager.getConfigString("inv-title");
		boolnull firstcharcaps = manager.getConfigBoolean("first-char-caps");
		boolnull incl_kits = manager.getConfigBoolean("includecmd-kits");
		
		if(invtitle != null)
			EssentialsKitGUI.CONFIG_INVTITLE = Language.stringToChatColor(invtitle);
		if(firstcharcaps != boolnull.NULL)
			EssentialsKitGUI.CONFIG_FIRSTCHARCAPS = firstcharcaps.toBoolean();
		if(incl_kits != boolnull.NULL)
			EssentialsKitGUI.CONFIG_INCLCMD_KITS = incl_kits.toBoolean();
		
		if(version == null || version != null && !version.equals(EssentialsKitGUI.getVersion()))
			save();
	}
	
	public static void save(){
		manager.clear();
		manager.addComment("Don't change this");
		manager.addConfig("config-version", EssentialsKitGUI.getVersion());
		
		manager.addEmptyLine();
		
		manager.addComment("Set the title from the inventory");
		manager.addConfig("inv-title", Language.chatColorToString(EssentialsKitGUI.CONFIG_INVTITLE));
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, the first character in the name of the kit is in caps");
		manager.addConfig("first-char-caps", EssentialsKitGUI.CONFIG_FIRSTCHARCAPS);
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, /kits will open the GUI too");
		manager.addConfig("includecmd-kits", EssentialsKitGUI.CONFIG_INCLCMD_KITS);
		
		manager.save();
	}
}
