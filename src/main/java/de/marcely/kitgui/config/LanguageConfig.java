/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.config;

import java.util.Map.Entry;

import de.marcely.kitgui.EssentialsKitGUI;
import de.marcely.kitgui.Language;

public class LanguageConfig {
	public static ConfigManager cm = new ConfigManager(EssentialsKitGUI.plugin.getName(), "messages.yml", false);
	
	public static void load(){
		if(cm.exists()){
			cm.load();
			
			for(Entry<String, Object> entry:cm.getInside(0).entrySet()){
				String key = entry.getKey();
				String value = (String) entry.getValue();
				
				Language l = Language.getLanguage(key);
				if(l != null)
					Language.setTranslation(l, Language.stringToChatColor(value));
			}
			
		}
		
		save();
	}
	
	public static void save(){
		cm.clear();
		
		for(Language l:Language.values())
			cm.addConfig(l.name(), Language.chatColorToString(l.getMessage()));
		
		cm.save();
	}
}