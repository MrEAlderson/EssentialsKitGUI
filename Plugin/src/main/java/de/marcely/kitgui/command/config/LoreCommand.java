package de.marcely.kitgui.command.config;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.Message;
import de.marcely.kitgui.command.Command;
import de.marcely.kitgui.util.ChatColorUtil;
import de.marcely.kitgui.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoreCommand extends Command.Executor {

    public LoreCommand(KitGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final GUIKit kit = this.plugin.getContainer().getHooked(args[0]);

        if (kit == null) {
            Message.UNKNOWN_KIT
                    .placeholder("kit", args[0])
                    .send(sender);

            return;
        }

        final String command = "/kitcfg lore " + args[0];

        if (args.length == 1) {
            if (!kit.getLore().isEmpty()) {
                int num = 1;

                for (String line : kit.getLore()) {
                    sender.sendMessage("" + ChatColor.GRAY + num + ": " + line);
                    num++;
                }

                sender.sendMessage("");
            }

            sender.sendMessage(ChatColor.YELLOW + command + " add <text>");

            if (!kit.getLore().isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + command + " set <line> <text>");
                sender.sendMessage(ChatColor.YELLOW + command + " remove <line>");
                sender.sendMessage(ChatColor.YELLOW + command + " clear");
            }

            return;
        }

        switch (args[1].toLowerCase()) {
            case "add": {
                if (args.length == 2) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " add <text>")
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(kit.getLore());

                lore.add("&f" + args[2]);

                kit.setLore(lore);
                kit.updateDisplayedIcon();
                kit.save();

                Message.COMMAND_ADD_LORE
                        .placeholder("kit", kit.getName())
                        .placeholder("lore", ChatColorUtil.translate(args[2]))
                        .send(sender);
            }
            break;

            case "set": {
                if (args.length <= 3) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " set <line> <text>")
                            .send(sender);
                    return;
                }

                final Integer line = StringUtil.parseInt(args[2]);
                final String text = "&f" + args[3];

                if (line == null) {
                    Message.NOT_NUMBER
                            .placeholder("number", args[2])
                            .send(sender);
                    return;
                }

                if (line < 1 || line > kit.getLore().size()) {
                    Message.LINE_OUT_OF_BOUNDS
                            .placeholder("line", "" + line)
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(kit.getLore());

                lore.set(line-1, text);

                kit.setLore(lore);
                kit.updateDisplayedIcon();
                kit.save();

                Message.COMMAND_SET_LORE
                        .placeholder("kit", kit.getName())
                        .placeholder("pos", "" + line)
                        .placeholder("lore", ChatColorUtil.translate(args[2]))
                        .send(sender);
            }
            break;

            case "remove": {
                if (args.length == 2) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " remove <line>")
                            .send(sender);
                    return;
                }

                final Integer line = StringUtil.parseInt(args[2]);

                if (line == null) {
                    Message.NOT_NUMBER
                            .placeholder("number", args[2])
                            .send(sender);
                    return;
                }

                if (line < 1 || line > kit.getLore().size()) {
                    Message.LINE_OUT_OF_BOUNDS
                            .placeholder("line", "" + line)
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(kit.getLore());

                lore.remove(line-1);

                kit.setLore(lore);
                kit.updateDisplayedIcon();
                kit.save();

                Message.COMMAND_REMOVE_LORE
                        .placeholder("kit", kit.getName())
                        .placeholder("pos", "" + line)
                        .send(sender);
            }
            break;

            case "clear": {
                kit.setLore(Collections.emptyList());
                kit.updateDisplayedIcon();
                kit.save();

                Message.COMMAND_CLEAR_LORE
                        .placeholder("kit", kit.getName())
                        .send(sender);
            }
            break;

            default: {
                Message.UNKNOWN_COMMAND
                        .placeholder("command", args[1])
                        .send(sender);
            }
            break;
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                return getKits("");
            case 1:
                return getKits(args[0]);
            case 2:
                return getArray(args[1], "add", "set", "remove", "clear");
            default:
                return Collections.emptyList();
        }
    }
}