package de.marcely.kitgui.kit;

import de.marcely.kitgui.kit.provider.EssentialsXKitProvider;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Locale;

public class EssentialsXKit implements Kit {

    @Getter
    private final String name;
    @Getter
    private final EssentialsXKitProvider provider;

    public EssentialsXKit(EssentialsXKitProvider provider, String name) {
        this.provider = provider;
        this.name = name;
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission("essentials.kit." + this.name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void give(Player player) {
        // simulate a command execution
        this.provider.getKitCommand().execute(
                player,
                this.provider.getKitCommand().getLabel(),
                new String[] { this.name }
        );
    }
}
