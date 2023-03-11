package de.marcely.kitgui.kit.provider;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.GUIKitContainer;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface KitProvider <K extends Kit> {

    KitGUIPlugin getPlugin();

    void register();

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
        final BiConsumer<String, Function<Plugin, KitProvider<?>>> testPlugin = (name, factory) -> {
            if (foundPlugin.get() != null)
                return;

            final Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin == null)
                return;

            foundPlugin.set(factory.apply(plugin));
        };

        // top gets prioriztied
        testPlugin.accept("EssentialsX", plugin -> new EssentialsXKitProvider(guiPlugin, plugin));
        testPlugin.accept("Essentials", plugin -> new EssentialsXKitProvider(guiPlugin, plugin));

        return foundPlugin.get();
    }

    static String[] getLegalProviderPlugins() {
        return new String[] {
                "EssentialsX", "Essentials"
        };
    }
}