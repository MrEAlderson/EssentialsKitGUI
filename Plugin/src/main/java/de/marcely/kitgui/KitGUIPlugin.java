package de.marcely.kitgui;

import de.marcely.kitgui.command.KitCommandInjector;
import de.marcely.kitgui.command.KitConfigCommand;
import de.marcely.kitgui.kit.provider.KitProvider;
import de.marcely.kitgui.storage.GeneralConfig;
import de.marcely.kitgui.storage.KitsStorage;
import de.marcely.kitgui.storage.MessagesConfig;
import de.marcely.kitgui.util.AdaptedGson;
import de.marcely.kitgui.util.ItemStackStringifier;
import de.marcely.kitgui.util.gui.GUIContainer;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class KitGUIPlugin extends JavaPlugin {

    @Getter
    private KitProvider<?> provider = null;
    @Getter
    private final GUIKitContainer container = new GUIKitContainer(this);
    @Getter
    private final GUIKitRenderer renderer = new GUIKitRenderer(container);

    @Getter
    private final ItemStackStringifier itemStackStringifier = new ItemStackStringifier(this);

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

        try {
            this.provider.register();
        } catch(Exception e) {
            getLogger().log(
                    Level.SEVERE,
                    "Failed to register the provider " + this.provider.getName() + " v" + this.provider.getVersion() +
                            ". Updating it could potentially fix it.",
                    e);
            getLogger().warning("Shutting down plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // init and load everything
        GUIContainer.init(this);
        AdaptedGson.init(this);

        Bukkit.getPluginManager().registerEvents(new KitCommandInjector(this.renderer), this);

        reloadConfig();

        {
            final KitConfigCommand cmd = new KitConfigCommand();

            cmd.registerDefaultCommands(this);

            getCommand("kitcfg").setExecutor(cmd);
            getCommand("kitcfg").setTabCompleter(cmd);
        }

        // metrics
        {
            final Metrics metrics = new Metrics(this, 17926);

            metrics.addCustomChart(new SimplePie("used_provider", () -> {
                return this.provider.getName();
            }));
            metrics.addCustomChart(new SimplePie("used_provider_version", () -> {
                return this.provider.getName() + " v" + this.provider.getVersion();
            }));
            metrics.addCustomChart(new SingleLineChart("kits_amount", () -> {
                return this.provider.getKits().size();
            }));
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
