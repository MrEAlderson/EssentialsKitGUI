package de.marcely.kitgui.command.config;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.Message;
import de.marcely.kitgui.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends Command.Executor {

    public ListCommand(KitGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final int count = this.plugin.getProvider().getKits().size();
        final String kits = count != 0 ?
                this.plugin.getContainer().getAll().stream()
                        .filter(GUIKit::hasHook)
                        .map(GUIKit::getName)
                        .sorted(String::compareTo)
                        .collect(Collectors.joining(", ")) :
                (ChatColor.RED + Message.NONE.get());

        Message.COMMAND_LIST
                .placeholder("amount", "" + count)
                .placeholder("kits", kits)
                .send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}