package de.marcely.kitgui;

import de.marcely.kitgui.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Message {

    COMMAND_USAGE("&eCorrect usage: &6{usage}"),
    COMMAND_LIST("&eAll kits ({amount}): &7{kits}"),
    COMMAND_SET_DISPLAYNAME("&eThe display name of the kit &6{kit} has been changed to {display-name}"),
    COMMAND_SET_ICON("&eThe icon of the kit &6{kit} &ehas been changed to &6{icon}"),
    COMMAND_CLEAR_LORE("&eRemoved all lore of the kit &6{kit}"),
    COMMAND_ADD_LORE("&eAdded lore to the kit &6{kit}&e: {lore}"),
    COMMAND_REMOVE_LORE("&eRemoved lore from kit &6{kit}&e: {lore}"),
    COMMAND_SET_LORE("&eSet lore in kit &6{kit}&e at &6{pos}&e: {lore}"),
    COMMAND_RELOAD("&eConfigurations have been reloaded"),
    COMMAND_SET_SLOT("&eKit &6{kit} &eis now being displayed at &6X{x} Y{y}"),
    COMMAND_RESET_SLOT("&eKit &6{kit} &eis now being normally added to the GUI"),

    COORD_OUT_OF_BOUNDS("&cThe coordinates &4X{x} Y{y} &care out of bounds"),
    LINE_OUT_OF_BOUNDS("&cThe line &4{line} &cis out of bounds"),
    UNKNOWN_KIT("&cUnknown kit &4{kit}"),
    UNKNOWN_COMMAND("&cUnknown command &4{command}"),
    UNKNOWN_PLAYER("&cUnknown player &4{player}"),
    INVALID_MATERIAL("&cInvalid material &4{material}"),
    NOT_NUMBER("&4{number} &cis not a number"),
    PLAYERS_ONLY("Only players may execute the command"),
    INSUFFICIENT_PERMISSIONS("&cInsufficient permissions\""),

    NEXT_PAGE("&aNext page"),
    PREV_PAGE("&aPrevious page"),
    PAGE_INFO("&7Go to page {new-page}."),

    NONE("None"),
    KIT_HOOK_DISAPPEAR("&cOops, it seems like the kit does not exist anymore. Please re-open the GUI");

    @Getter
    private String message = name(), configMessage = name();

    Message(String value) {
        setConfigMessage(value);
    }

    public void setConfigMessage(String msg) {
        this.configMessage = msg;
        this.message = ChatColorUtil.translate(msg);
    }

    public Construct placeholder(String key, String value) {
        return new Construct(this).placeholder(key, value);
    }

    public void send(CommandSender... sender) {
        new Construct(this).send(sender);
    }

    public String get() {
        return this.message;
    }

    public String[] getLines() {
        return this.message.split("\\n");
    }

    public String getKey() {
        return name().toLowerCase(Locale.ENGLISH).replace("_", "-");
    }

    @Nullable
    public static Message getByKey(String key) {
        for (Message msg : values()) {
            if (msg.getKey().equalsIgnoreCase(key))
                return msg;
        }

        return null;
    }



    public static class Construct {

        private String message;

        private Construct(Message msg) {
            this.message = msg.get();
        }

        public Construct placeholder(String key, String value) {
            this.message = this.message.replace("{" + key + "}", value);

            return this;
        }

        public void send(CommandSender... sender) {
            final String[] lines = getLines();

            if (lines.length == 0 || lines.length == 1 && lines[0].isEmpty())
                return;

            for (CommandSender s : sender)
                s.sendMessage(lines);
        }

        public String get() {
            return this.message;
        }

        public String[] getLines() {
            return this.message.split("\\n");
        }
    }
}
