package de.marcely.kitgui.command.config;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.Message;
import de.marcely.kitgui.command.Command;
import de.marcely.kitgui.util.ChatColorUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DisplayNameCommand extends Command.Executor {

    public DisplayNameCommand(KitGUIPlugin plugin) {
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

        if (args[1].equals("RESET"))
            kit.setDisplayName("&f" + kit.getHook().getName());
        else
            kit.setDisplayName("&f" + args[1]);

        kit.updateDisplayedIcon();
        kit.save();

        Message.COMMAND_SET_DISPLAYNAME
                .placeholder("kit", kit.getName())
                .placeholder("display-name", ChatColorUtil.translate(kit.getDisplayName()))
                .send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                return getKits("");
            case 1:
                return getKits(args[0]);
            default:
                return Collections.emptyList();
        }
    }
}