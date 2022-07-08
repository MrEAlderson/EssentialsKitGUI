/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.config;

public enum boolnull {
	True,
	False,
	NULL;
	
	public boolean toBoolean(){
		if(this == True)
			return true;
		
		return false;
	}
	
	public static boolnull valueOf(boolean bool){
		if(bool == true)
			return True;
		else if(bool == false)
			return False;
		return NULL;
	}
}
