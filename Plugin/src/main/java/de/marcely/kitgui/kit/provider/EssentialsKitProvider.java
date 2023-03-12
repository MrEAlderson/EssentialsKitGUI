package de.marcely.kitgui.kit.provider;

import com.earth2me.essentials.Kits;
import de.marcely.kitgui.KitGUIPlugin;
import de.marcely.kitgui.kit.EssentialsXKit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;

/**
 * Legacy Essentials and EssentialsX prior 2.19
 */
public class EssentialsKitProvider extends EssentialsXKitProvider {

    private Method getKitsMethod;

    public EssentialsKitProvider(KitGUIPlugin guiPlugin, Plugin essentialsPlugin) {
        super(guiPlugin, essentialsPlugin);
    }

    @Override
    public String getName() {
        return "Essentials";
    }

    @Override
    public void register() throws Exception {
        this.getKitsMethod = Kits.class.getMethod("getKits");

        super.register();
    }

    @Override
    public void fetchKits() {
        this.kits.clear();

        Collection<String> kitNames = null;

        try {
            final ConfigurationSection section = (ConfigurationSection) this.getKitsMethod.invoke(this.hook.getKits());

            kitNames = section.getKeys(false);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (String name : kitNames) {
            this.kits.put(
                    name.toLowerCase(Locale.ENGLISH),
                    new EssentialsXKit(this, name));
        }

        updateGUIHooks();
    }
}