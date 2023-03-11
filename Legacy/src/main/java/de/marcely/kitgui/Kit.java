package de.marcely.kitgui;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Data
public class Kit implements Serializable {

    private static final long serialVersionUID = 100042053024876811L;

    private String name;
    private String iconName; // org.bukkit.Material
    private short iconID; // ItemStack durability
    private String prefix = "";
    private List<String> lores = new ArrayList<>();
}