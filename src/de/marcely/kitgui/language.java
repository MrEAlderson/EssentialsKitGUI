package de.marcely.kitgui;

public class language {
	public static String noPermissions = "You have got no permissions for this command!";
	public static String theWarp = "The warp";
	public static String doesntExists = "doesn't exists!";
	public static String notAPlayer = "You are not a player!";
	public static String givingYouThe = "Giving you the";
	public static String kit = "kit";
	public static String iconChangedTo = "The icon has been changed to";
	public static String unkownMaterial = "Unkown material";
	public static String howToUseThisCommand = "How to use this command";
	public static String reloadedConfig = "The config has been successfully reloaded!";
	public static String unkownSubcommand = "Unkown subcommand";
	public static String ressourceBy = "ressource by";
	public static String version = "version";
	public static String reloadTheConfig = "Reload the config";
	public static String changeTheIconFromAKit = "Change the icon from a kit";
	public static String changeThePrefixFromAKit = "Change the prefix from a kit";
	public static String addOrRemoveLoresFromAKit = "Add or remove lores from a kit";
	public static String saysYouTheCommandForKitcfg = "Says you the commands for /kitcfg";
	public static String gotNoPrefix = "got no prefix.";
	public static String write = "Write";
	public static String toChangeThePrefix = "to change the prefix";
	public static String thePrefixByTheWarp = "The prefix by the kit";
	public static String hasBeenSuccessfullyChanged = "has been successfully changed!";
	
	public static void updateLanguage(){
		if(main.CONFIG_LANGUAGE == LanguageType.English){
			noPermissions = "You have got no permissions for this command!";
			theWarp = "The warp";
			doesntExists = "doesn't exists!";
			notAPlayer = "You are not a player!";
			givingYouThe = "Giving you the";
			kit = "kit";
			iconChangedTo = "The icon has been changed to";
			unkownMaterial = "Unkown material";
			howToUseThisCommand = "How to use this command";
			reloadedConfig = "The config has been successfully reloaded!";
			unkownSubcommand = "Unkown subcommand";
			ressourceBy = "ressource by";
			version = "version";
			reloadTheConfig = "Reload the config";
			changeTheIconFromAKit = "Change the icon from a kit";
			changeThePrefixFromAKit = "Change the prefix from a kit";
			addOrRemoveLoresFromAKit = "Add or remove lores from a kit";
			saysYouTheCommandForKitcfg = "Says you the commands for /kitcfg";	
			gotNoPrefix = "got no prefix.";
			write = "Write";
			toChangeThePrefix = "to change the prefix";
			thePrefixByTheWarp = "The prefix by the warp";
			hasBeenSuccessfullyChanged = "has been successfully changed!";
		}else if(main.CONFIG_LANGUAGE == LanguageType.German){
			noPermissions = "Du hast keine Rechte für diesen Befehl";
			theWarp = "Den Warp";
			doesntExists = "gibt es nicht!";
			notAPlayer = "Du bist kein Spieler!";
			givingYouThe = "Gebe dir das";
			kit = "Kit";
			iconChangedTo = "Das Icon wurde geändert zu";
			unkownMaterial = "Unbekanntes Material";
			howToUseThisCommand = "Wie man den Befehl benutzt";
			reloadedConfig = "Die Einstellungen wurden erfolgreich aktualisiert!";
			unkownSubcommand = "Unbekanntes subbefehl";
			ressourceBy = "ressource von";
			version = "version";
			reloadTheConfig = "Einstellungen neuladen";
			changeTheIconFromAKit = "Das Icon vom Kit ändern";
			changeThePrefixFromAKit = "Den prefix von einem Kit ändern";
			addOrRemoveLoresFromAKit = "Die Beschreibung von einem Kit ändern";
			saysYouTheCommandForKitcfg = "Sagt dir die Befehle für /kitcfg";	
			gotNoPrefix = "hat keinen Prefix.";
			write = "Schreibe";
			toChangeThePrefix = "um den Prefix zu ändern";
			thePrefixByTheWarp = "Das Prefix von dem Warp";
			hasBeenSuccessfullyChanged = "wurde erfolgreich geändert!";
		}
	}
}
