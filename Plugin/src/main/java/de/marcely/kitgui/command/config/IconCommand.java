package de.marcely.kitgui.command.config;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.Message;
import de.marcely.kitgui.command.Command;
import de.marcely.kitgui.util.ItemStackUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class IconCommand extends Command.Executor {

    public IconCommand(KitGUIPlugin plugin) {
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

        final ItemStack icon = ItemStackUtil.parse(args[1]);

        if (icon == null) {
            Message.INVALID_MATERIAL
                    .placeholder("material", args[1])
                    .send(sender);
            return;
        }

        kit.setBaseIcon(icon);
        kit.updateDisplayedIcon();
        kit.save();

        Message.COMMAND_SET_ICON
                .placeholder("kit", kit.getName())
                .placeholder("icon", ItemStackUtil.serialize(kit.getBaseIcon()))
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