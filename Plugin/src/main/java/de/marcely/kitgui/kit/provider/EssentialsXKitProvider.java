package de.marcely.kitgui.kit.provider;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IConf;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.kit.EssentialsXKit;
import lombok.Getter;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EssentialsXKitProvider implements KitProvider<EssentialsXKit> {

    @Getter
    protected final KitGUIPlugin plugin;
    protected final IEssentials hook;

    protected final Map<String, EssentialsXKit> kits = new ConcurrentHashMap<>();

    @Getter
    private PluginCommand kitCommand;
    private IConf reloadListener;

    public EssentialsXKitProvider(KitGUIPlugin guiPlugin, Plugin essentialsPlugin) {
        this.plugin = guiPlugin;
        this.hook = (IEssentials) essentialsPlugin;
    }

    @Override
    public String getName() {
        return "EssentialsX";
    }

    @Override
    public String getVersion() {
        return this.hook.getDescription().getVersion();
    }

    @Override
    public void register() throws Exception {
        if ((this.kitCommand = ((JavaPlugin) this.hook).getCommand("kit")) == null)
            throw new IllegalStateException("EssentialsX didn't register kit command");

        this.hook.addReloadListener(this.reloadListener = () -> {
            // a tick later to make sure that everything has been loaded
            Bukkit.getScheduler().runTaskLater(
                    this.plugin,
                    this::fetchKits,
                    1
            );
        });

        fetchKits();
    }

    @Override
    public void unregister() {
        try{
            // try to remove reload listener. yes, it's actually that complicated.
            final Field field = Essentials.class.getDeclaredField("confList");

            field.setAccessible(true);
            ((Collection<IConf>) field.get(this.hook)).remove(this.reloadListener);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Collection<EssentialsXKit> getKits() {
        return this.kits.values();
    }

    @Override
    public @Nullable EssentialsXKit getKit(String name) {
        return this.kits.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void fetchKits() {
        this.kits.clear();

        for (String name : this.hook.getKits().getKitKeys()) {
            this.kits.put(
                    name.toLowerCase(Locale.ENGLISH),
                    new EssentialsXKit(this, name));
        }

        updateGUIHooks();
    }
}
