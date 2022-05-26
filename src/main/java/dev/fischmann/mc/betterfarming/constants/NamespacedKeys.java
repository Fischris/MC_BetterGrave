package dev.fischmann.mc.betterfarming.constants;

import dev.fischmann.mc.betterfarming.Main;
import org.bukkit.NamespacedKey;

public enum NamespacedKeys {
    FARMER_UUID("farmer-uuid");

    private final String name;
    private Main plugin;

    NamespacedKeys(String name) {
        this.name = name;
    }
    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }
    public Main getPlugin() {
        return this.plugin;
    }

    public NamespacedKey getKey (){
        return new NamespacedKey(plugin, name);
    }
}
