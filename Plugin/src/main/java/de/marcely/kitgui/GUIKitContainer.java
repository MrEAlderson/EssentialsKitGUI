package de.marcely.kitgui;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIKitContainer {

    @Getter
    private final KitGUIPlugin plugin;

    private final Map<String, GUIKit> kits = new ConcurrentHashMap<>();

    public GUIKitContainer(KitGUIPlugin plugin) {
        this.plugin = plugin;
    }

    public Collection<GUIKit> getAll() {
        return this.kits.values();
    }

    public void clear() {
        this.kits.clear();
    }

    public boolean add(GUIKit kit) {
        if (kit.getContainer() != this)
            throw new IllegalStateException("Kit container mismatch");

        kit.updateAttributes();

        return this.kits.putIfAbsent(kit.getName().toLowerCase(Locale.ENGLISH), kit) == null;
    }

    public boolean remove(GUIKit kit) {
        return this.kits.remove(kit.getName().toLowerCase(Locale.ENGLISH), kit);
    }

    @Nullable
    public GUIKit getHooked(String name) {
        final GUIKit kit = this.kits.get(name.toLowerCase(Locale.ENGLISH));

        if (kit == null || !kit.hasHook())
            return null;

        return kit;
    }
}
