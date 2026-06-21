package de.marcely.kitgui.util;

import org.jetbrains.annotations.Nullable;

public class StringUtil {

    @Nullable
    public static Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public static int parseInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return def;
        }
    }

    public static byte parseByte(String str, int def) {
        try {
            return (byte) Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return (byte) def;
        }
    }
}
