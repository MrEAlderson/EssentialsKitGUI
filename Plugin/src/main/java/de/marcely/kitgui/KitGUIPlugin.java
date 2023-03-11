package de.marcely.kitgui;

import de.marcely.kitgui.command.KitCommandInjector;
import de.marcely.kitgui.command.KitConfigCommand;
import de.marcely.kitgui.kit.provider.KitProvider;
import de.marcely.kitgui.storage.GeneralConfig;
import de.marcely.kitgui.storage.KitsStorage;
import de.marcely.kitgui.storage.MessagesConfig;
import de.marcely.kitgui.util.AdaptedGson;
import de.marcely.kitgui.util.gui.GUIContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class KitGUIPlugin extends JavaPlugin {

    @Getter
    private KitProvider<?> provider = null;
    @Getter
    private final GUIKitContainer container = new GUIKitContainer(this);
    @Getter
    private final GUIKitRenderer renderer = new GUIKitRenderer(container);

    @Override
    public void onEnable() {
        // find provider
        this.provider = KitProvider.findProvider(this);

        if (this.provider == null) {
            getLogger().warning("Could not find a valid provider!");
            getLogger().warning("Make sure that you have either of the following plugins installed: " +
                    String.join(", ", KitProvider.getLegalProviderPlugins()));
            getLogger().warning("Shutting down plugin...");

            Bukkit.getPluginManager().disablePlugin(this);

            return;
        }

        // init and load everything
        GUIContainer.init(this);
        AdaptedGson.init(this);

        this.provider.register();
        Bukkit.getPluginManager().registerEvents(new KitCommandInjector(this.renderer), this);

        reloadConfig();

        {
            final KitConfigCommand cmd = new KitConfigCommand();

            cmd.registerDefaultCommands(this);

            getCommand("kitcfg").setExecutor(cmd);
            getCommand("kitcfg").setTabCompleter(cmd);
        }
    }

    @Override
    public void onDisable() {
        if (GUIContainer.getInstance() != null)
            GUIContainer.getInstance().closeAll();

        if (this.provider != null)
            this.provider.unregister();
    }

    @Override
    public void reloadConfig() {
        GUIContainer.getInstance().closeAll();

        MessagesConfig.loadAndUpdate(this);
        GeneralConfig.loadAndUpdate(this);
        KitsStorage.loadAll(this.container);
    }
}
