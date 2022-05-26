package dev.fischmann.mc.betterfarming.util;

import org.bukkit.Location;

public class FLocation extends Location {

    public FLocation(Location location) {
        super(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }
}
