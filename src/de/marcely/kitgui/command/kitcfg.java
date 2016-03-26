package de.marcely.kitgui.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.marcely.kitgui.Kit;
import de.marcely.kitgui.language;
import de.marcely.kitgui.main;
import de.marcely.kitgui.config.KitConfig;
import de.marcely.kitgui.config.config;

public class kitcfg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("kitgui.cfg")){
			if(args.length >= 1){
				String subcommand = args[0];
				if(subcommand.equalsIgnoreCase("help")){
					sendCommands(sender);
				}else if(subcommand.equalsIgnoreCase("seticon")){
					if(args.length >= 3){
						String kitname = args[1];
						String[] splits = args[2].split(":");
						Material icon = getMaterial(splits[0]);
						int id = 0;
						if(splits.length >= 2 && isNumber(splits[1]))
							id = Integer.valueOf(splits[1]);
						if(main.getKit(kitname.toLowerCase()) != null){
							if(icon != null){
								setIcon(kitname, icon, id);
								sender.sendMessage(ChatColor.GREEN + language.iconChangedTo + " " + ChatColor.DARK_GREEN + icon.name().toLowerCase().replace("_", " ") + ChatColor.GREEN + "!");
							}else{
								sender.sendMessage(ChatColor.DARK_RED + language.unkownMaterial + " " + ChatColor.RED + args[2] + ChatColor.DARK_RED + "!");
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theKit + " " + ChatColor.RED + kitname + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/kitcfg seticon <kit name> <material>");
					}
				}else if(subcommand.equalsIgnoreCase("prefix")){
					if(args.length >= 2){
						if(main.getKit(args[1].toLowerCase()) != null){
							Kit kit = main.kits.getKit(args[1]);
							String kitname = main.getKit(args[1].toLowerCase()).getName();
							String bPrefix = null;
							
							if(kit != null && kit.getPrefix() != null)
								bPrefix = kit.getPrefix();
							
							if(args.length >= 4 && args[2].equalsIgnoreCase("set")){
								String aPrefix = args[3];
								main.kits.setPrefix(kitname, aPrefix);
								KitConfig.save(main.kits);
								sender.sendMessage(ChatColor.GREEN + language.thePrefixByTheKit + " " + ChatColor.DARK_GREEN + kitname + ChatColor.GREEN + " " + language.hasBeenSuccessfullyChanged);
							}else{
								for(int i=0; i<7; i++)
									sender.sendMessage("");
								if(bPrefix != null)
									sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.WHITE + bPrefix + kitname);
								else
									sender.sendMessage(ChatColor.RED + kitname + " " + ChatColor.DARK_RED + language.gotNoPrefix);
								sender.sendMessage("");
								sender.sendMessage(ChatColor.GOLD + language.write + ChatColor.YELLOW + "/kitcfg prefix <kit name> set <prefix> " + ChatColor.GOLD + language.toChangeThePrefix);
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theKit + " " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/kitcfg prefix <kit name>");
					}
				}else if(subcommand.equalsIgnoreCase("lore")){
					if(args.length >= 2){
						if(main.getKit(args[1].toLowerCase()) != null){
							if(args.length >= 4 && args[2].equalsIgnoreCase("add")){
								
							}else if(args.length >= 4 && args[2].equalsIgnoreCase("remove")){
								
							}else{
								
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theKit + " " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/kitcfg lore <kit name>");
					}
				}else if(subcommand.equalsIgnoreCase("reload")){
					config.load();
					sender.sendMessage(ChatColor.GREEN + language.reloadedConfig);
				}else{
					sender.sendMessage(ChatColor.DARK_RED + language.unkownSubcommand + " " + ChatColor.RED + subcommand + ChatColor.DARK_RED + "!");
				}
			}else
				sendCommands(sender);
		}else{
			sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
		}
		return true;
	}
	
	public void sendCommands(CommandSender sender){
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + " ------------ " + ChatColor.GOLD + "Commands" + ChatColor.YELLOW + " ------------ ");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg help " + ChatColor.AQUA + language.saysYouTheCommandForKitcfg);
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg seticon <kitName> <material> " + ChatColor.AQUA + language.changeTheIconFromAKit);
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg prefix <kitName> " + ChatColor.AQUA + language.changeThePrefixFromAKit);
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg lore <kitname> " + ChatColor.AQUA + language.addOrRemoveLoresFromAKit);
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg reload " + ChatColor.AQUA + language.reloadTheConfig);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GREEN + "KitGUI " + language.ressourceBy + " " + ChatColor.DARK_GREEN + "Marcely1199");
		sender.sendMessage(ChatColor.GREEN + "KitGUI " + language.version + " " + ChatColor.DARK_GREEN + main.version);
	}
	
	public void setIcon(String kitname, Material icon, int id){
		main.kits.setIcon(kitname, icon, (short) id);
		de.marcely.kitgui.config.KitConfig.save(main.kits);
	}
	
	public boolean isNumber(String str){
		try{
			Integer.valueOf(str);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public Material getMaterial(String str){
		for(Material m:Material.values()){
			if(str.equalsIgnoreCase(m.name()) ||
			   str.equalsIgnoreCase(m.name().replace("_", "")) ||
			   m.name().equalsIgnoreCase(str + "_item") ||
			   isNumber(str) && Integer.valueOf(str) == m.getId())
				return m;
		}
		return null;
	}
}
