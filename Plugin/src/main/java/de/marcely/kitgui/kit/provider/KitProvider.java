package de.marcely.kitgui.kit.provider;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.GUIKitContainer;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.kit.Kit;
import de.marcely.kitgui.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface KitProvider <K extends Kit> {

    KitGUIPlugin getPlugin();

    String getName();

    String getVersion();

    void register() throws Exception;

    void unregister();

    Collection<K> getKits();

    @Nullable
    K getKit(String name);

    void fetchKits();


    default void updateGUIHooks() {
        final GUIKitContainer container = getPlugin().getContainer();

        // update existing
        for (GUIKit gKit : new ArrayList<>(container.getAll())) {
            final Kit hKit = getKit(gKit.getName());

            gKit.setHook(hKit);

            if (hKit == null && gKit.isDefault())
                container.remove(gKit);
        }

        // add new ones
        for (Kit hKit : getKits()) {
            final GUIKit gKit = new GUIKit(container, hKit.getName());

            gKit.setHook(hKit);

            container.add(gKit);
        }
    }


    @Nullable
    static KitProvider<?> findProvider(KitGUIPlugin guiPlugin) {
        final AtomicReference<KitProvider<?>> foundPlugin = new AtomicReference<>();
        final TriConsumer<String, String, Function<Plugin, KitProvider<?>>> testPlugin = (name, requiredClass, factory) -> {
            if (foundPlugin.get() != null)
                return;

            final Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin == null)
                return;

            try {
                Class.forName(requiredClass);

                foundPlugin.set(factory.apply(plugin));
            } catch (ClassNotFoundException e) { }
        };

        // top gets prioritized
        testPlugin.accept(
                "EssentialsX",
                "net.essentialsx.api.v2.services.BalanceTop",
                plugin -> new EssentialsXKitProvider(guiPlugin, plugin));
        testPlugin.accept(
                "Essentials",
                "net.essentialsx.api.v2.services.BalanceTop",
                plugin -> new EssentialsXKitProvider(guiPlugin, plugin));

        testPlugin.accept(
                "EssentialsX",
                "com.earth2me.essentials.Essentials",
                plugin -> new EssentialsKitProvider(guiPlugin, plugin));
        testPlugin.accept(
                "Essentials",
                "com.earth2me.essentials.Essentials",
                plugin -> new EssentialsKitProvider(guiPlugin, plugin));

        return foundPlugin.get();
    }

    static String[] getLegalProviderPlugins() {
        return new String[] {
                "EssentialsX", "Essentials"
        };
    }
}