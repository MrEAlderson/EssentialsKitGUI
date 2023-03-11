package de.marcely.kitgui.command.config;

import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.command.Command;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebugCommand extends Command.Executor {

    public DebugCommand(KitGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final IEssentials ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        for (String key : new ArrayList<>(ess.getKits().getKitKeys()))
            ess.getKits().removeKit(key);


        for (int i=0; i<Integer.parseInt(args[0]); i++) {
            ess.getKits().addKit("kit" + i, Collections.emptyList(), 0);
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}