package de.marcely.kitgui.config;

import de.marcely.kitgui.Kit;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class KitConfig implements Serializable {

    private static final long serialVersionUID = -1266449831520034396L;

    @Getter
    private List<Kit> kits = new ArrayList<>();

    @Nullable
    public static KitConfig load(Logger logger) {
        final File file = getFile();

        if (!file.exists())
            return null;

        try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file.toPath()))){
            return (KitConfig) stream.readObject();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Failed to load legacy kits data", e);
        }

        return null;
    }

    public static File getFile() {
        return new File("plugins/Essentials_KitGUI/kits.cfg");
    }
}