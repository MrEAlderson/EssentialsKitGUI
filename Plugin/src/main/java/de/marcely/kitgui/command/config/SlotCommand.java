package de.marcely.kitgui.command.config;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.Message;
import de.marcely.kitgui.command.Command;
import de.marcely.kitgui.util.StringUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SlotCommand extends Command.Executor {

    public SlotCommand(KitGUIPlugin plugin) {
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

        if (args[1].equalsIgnoreCase("RESET")) {
            kit.setSlotX(-1);
            kit.setSlotY(-1);
            kit.save();

            Message.COMMAND_RESET_SLOT
                    .placeholder("kit", kit.getName())
                    .send(sender);

            return;
        }

        if (args.length == 2) {
            Message.COMMAND_USAGE
                    .placeholder("usage", "/kitcfg slot " + kit.getName() + " <x> <y>")
                    .send(sender);
            return;
        }

        final Integer x = StringUtil.parseInt(args[1]);
        final Integer y = StringUtil.parseInt(args[2]);

        if (x == null) {
            Message.NOT_NUMBER
                    .placeholder("number", args[1])
                    .send(sender);
            return;
        }

        if (y == null) {
            Message.NOT_NUMBER
                    .placeholder("number", args[2])
                    .send(sender);
            return;
        }

        if (x < 0 || x > 8 || y < 0) {
            Message.COORD_OUT_OF_BOUNDS
                    .placeholder("x", "" + x)
                    .placeholder("y", "" + y)
                    .send(sender);
            return;
        }

        kit.setSlotX(x);
        kit.setSlotY(y);
        kit.save();

        Message.COMMAND_SET_SLOT
                .placeholder("kit", kit.getName())
                .placeholder("x", "" + x)
                .placeholder("y", "" + y)
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