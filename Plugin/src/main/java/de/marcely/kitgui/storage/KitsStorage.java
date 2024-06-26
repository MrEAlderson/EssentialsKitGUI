package de.marcely.kitgui.storage;

import de.marcely.kitgui.GUIKit;
import de.marcely.kitgui.GUIKitContainer;
import de.marcely.kitgui.Kit;
import de.marcely.kitgui.config.KitConfig;
import de.marcely.kitgui.util.AdaptedGson;
import de.marcely.kitgui.util.ItemStackStringifier;
import de.marcely.kitgui.util.ItemStackUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KitsStorage {

    public static void loadAll(GUIKitContainer container) {
        try {
            final File folder = getFolder(container.getPlugin());

            container.clear();

            if (!folder.exists() || folder.list().length == 0) {
                // try to convert from legacy
                final File legacyFile = KitConfig.getFile();
                final Logger logger = container.getPlugin().getLogger();

                if (legacyFile.exists()) {
                    logger.info("Detected that you are coming from a legacy version! Auto-upgrading the kits...");

                    final ItemStackStringifier isStringifier = container.getPlugin().getItemStackStringifier();
                    final KitConfig legacy = KitConfig.load(logger);

                    if (legacy == null)
                        return;

                    boolean allSuccessful = true;

                    for (Kit lKit : legacy.getKits()) {
                        final GUIKit nKit = new GUIKit(container, lKit.getName());
                        final String baseIconStr = lKit.getIconName() + ":" + lKit.getIconID();
                        ItemStack baseIcon = isStringifier.parse(baseIconStr);

                        if (baseIcon == null)
                            logger.warning("Failed to parse legacy icon data \"" + baseIconStr + "\" for kit \"" + lKit.getName() + "\". Resetting it");
                        else
                            nKit.setBaseIcon(baseIcon);

                        nKit.setDisplayName(lKit.getPrefix() + nKit.getDisplayName());
                        nKit.setLore(lKit.getLores());

                        container.add(nKit);

                        if (!save(nKit))
                            allSuccessful = false;

                        logger.info("Converted kit \"" + nKit.getName() + "\"");
                    }

                    if (allSuccessful)
                        legacyFile.delete();
                }

                return;
            }

            for (File file : folder.listFiles()) {
                if (!file.getName().endsWith(".json"))
                    continue;

                load(container, file);
            }

        } finally {
            // re-add missing ones
            container.getPlugin().getProvider().updateGUIHooks();
        }
    }

    private static void load(GUIKitContainer container, File file) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            final GUIKit kit = AdaptedGson.get().fromJson(reader, GUIKit.class);

            container.add(kit);
        }catch (Exception e) {
            container.getPlugin().getLogger().log(Level.SEVERE, "Failed to load kit " + file.getName() +
                    " (Possibly missing permissions or corrupted file?)", e);
        }
    }

    public static boolean save(GUIKit kit) {
        try {
            final File file = getFile(kit);

            file.getParentFile().mkdirs();

            if (!kit.exists() || kit.isDefault()) {
                file.delete();
                return true;
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                AdaptedGson.get().toJson(kit, writer);
            }

            return true;
        } catch(Exception e) {
            kit.getContainer().getPlugin().getLogger().log(Level.SEVERE, "Failed to save kit " + kit.getName() +
                    " (Possible out of storage or missing permissions?)", e);
            return false;
        }
    }

    private static File getFolder(Plugin plugin) {
        return new File(plugin.getDataFolder(), "kits");
    }

    private static File getFile(GUIKit kit) {
        final File folder = getFolder(kit.getContainer().getPlugin());
        final Pattern pattern = Pattern.compile("[\\\\/:*?\\\"<>|\"?*.$]");
        String fileName = kit.getName().replace("@", "$");
        Matcher match = null;

        while ((match = pattern.matcher(fileName)).find()) {
            final String invalidChars = fileName.substring(match.start(), match.end());
            final String replacement = invalidChars.chars()
                    .mapToObj(i -> "@" + Integer.toHexString(i) + "@")
                    .collect(Collectors.joining(""));

            fileName = fileName.replace(
                    invalidChars,
                    replacement);
        }

        return new File(folder, fileName + ".json");
    }
}
