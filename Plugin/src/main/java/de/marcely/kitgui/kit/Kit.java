package de.marcely.kitgui.kit;

import de.marcely.kitgui.kit.provider.KitProvider;
import org.bukkit.entity.Player;

public interface Kit {

    String getName();

    KitProvider<?> getProvider();

    boolean hasPermission(Player player);

    void give(Player player);

    default boolean exists() {
        return getProvider().getKits().contains(this);
    }
}