package dev.fischmann.mc.betterfarming;

import dev.fischmann.mc.betterfarming.util.FLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

import static java.util.Map.entry;

public class TreeFarming {


    private static final Map<Material, List<Material>> treeMap = Map.ofEntries(
            entry(Material.ACACIA_LOG, List.of(Material.ACACIA_LOG)),
            entry(Material.BIRCH_LOG, List.of(Material.BIRCH_LOG)),
            entry(Material.DARK_OAK_LOG, List.of(Material.DARK_OAK_LOG)),
            entry(Material.JUNGLE_LOG, List.of(Material.JUNGLE_LOG)),
            entry(Material.OAK_LOG, List.of(Material.OAK_LOG)),
            entry(Material.SPRUCE_LOG, List.of(Material.SPRUCE_LOG)),
            entry(Material.WARPED_STEM, List.of(Material.WARPED_STEM, Material.SHROOMLIGHT, Material.WARPED_WART_BLOCK)),
            entry(Material.CRIMSON_STEM, List.of(Material.CRIMSON_STEM, Material.SHROOMLIGHT, Material.NETHER_WART_BLOCK))
    );

    private static boolean isValidTree(Block b) {
        return treeMap.containsKey(b.getType());
    }

    public static boolean farmTree(Block b, Player p) {
        if (!isValidTree(b)) {
            return false;
        }

        switch (b.getType()) {

            case OAK_LOG, JUNGLE_LOG -> {
                doTreeFarming(b, p, 5);
                return true;
            }

            case BIRCH_LOG -> {
                doTreeFarming(b, p, 0);
                return true;
            }

            case DARK_OAK_LOG -> {
                doTreeFarming(b, p, 2);
                return true;
            }

            case SPRUCE_LOG -> {
                doTreeFarming(b, p, 1);
                return true;
            }

            case ACACIA_LOG -> {
                doAcaciaBreaking(b, p);
                return true;
            }

            case WARPED_STEM, CRIMSON_STEM -> {
                doTreeFarming(b, p, 2, true);
                return true;
            }


            default -> {

            }
        }
        return false;
    }


        private static void doTreeFarming(Block b, Player p, int radiusBigTree) {
            doTreeFarming(b, p, radiusBigTree, false);
        }

        private static void doTreeFarming(Block b, Player p, int radiusBigTree, boolean forceBig) {

        Location locMax = maxHight(b);
        Location locStart = b.getLocation();

        if (forceBig || (radiusBigTree > 0 && isBigTree(b.getLocation(), locMax))) {
            breakPossibleBranches(locStart, locMax, radiusBigTree, p);
        } else  {
            breakPossibleBranches(locStart, locMax, 0, p);
        }
    }

    private static Location maxHight (Block b) {
        Location locMax = new FLocation(b.getLocation());
        World w = b.getWorld();
        Material logTypeToBreak = b.getLocation().getBlock().getType();
        while (logTypeToBreak.equals(w.getBlockAt(locMax.getBlockX(), locMax.getBlockY() + 1, locMax.getBlockZ()).getType())) {
            locMax = locMax.add(0, 1, 0);
        }
        locMax.add(0, 1, 0);
        return locMax;
    }

    private static boolean isBigTree(Location locStart, Location locMax) {

        World w = locStart.getWorld();
        Material logTypeToBreak = locStart.getBlock().getType();
        Location[] locations = {
                new Location(locStart.getWorld(), locStart.getBlockX() + 1, locStart.getBlockY(), locStart.getBlockZ()),
                new Location(locStart.getWorld(), locStart.getBlockX() + 1, locStart.getBlockY(), locStart.getBlockZ() + 1),
                new Location(locStart.getWorld(), locStart.getBlockX(), locStart.getBlockY(), locStart.getBlockZ() + 1),
                new Location(locStart.getWorld(), locStart.getBlockX() - 1, locStart.getBlockY(), locStart.getBlockZ() + 1),
                new Location(locStart.getWorld(), locStart.getBlockX() - 1, locStart.getBlockY(), locStart.getBlockZ()),
                new Location(locStart.getWorld(), locStart.getBlockX() - 1, locStart.getBlockY(), locStart.getBlockZ() - 1),
                new Location(locStart.getWorld(), locStart.getBlockX(), locStart.getBlockY(), locStart.getBlockZ() - 1),
                new Location(locStart.getWorld(), locStart.getBlockX() + 1, locStart.getBlockY(), locStart.getBlockZ() - 1)
        };

        for (Location location : locations) {
            for (int i = location.getBlockY(); i <= locMax.getBlockY(); i++) {
                if (logTypeToBreak.equals(w.getType(location))) {
                    return true;
                }
                location = location.add(0, 1, 0);
            }
        }
        return false;
    }

    private static void breakPossibleBranches(Location locStart, Location locMax, int radius, Player p) {
        int count = 0;
        Material logTypeToBreak = locStart.getBlock().getType();

        while (count < 3 && locMax.getBlockY() >= locStart.getBlockY()) {


            for (int i = radius; count < 3 && i > 0; i--) {

                int x = i;
                int y = locMax.getBlockY();
                int z = i;

                for (int xx = x * -1; count < 3 && xx < x; xx++) {
                    Location loc = new FLocation(locMax);
                    loc.add(xx, 0, z);
                    count += breakIfLog(loc, logTypeToBreak, p);
                }

                for (int zz = z; count < 3 && zz > z * -1; zz--) {
                    Location loc = new FLocation(locMax);
                    loc.add(x, 0, zz);
                    count += breakIfLog(loc, logTypeToBreak, p);
                }

                for (int xx = x; count < 3 && xx > x * -1; xx--) {
                    Location loc = new FLocation(locMax);
                    loc.add(xx, 0, z * -1);
                    count += breakIfLog(loc, logTypeToBreak, p);
                }

                for (int zz = z * -1; count < 3 && zz < z; zz++) {
                    Location loc = new FLocation(locMax);
                    loc.add(x * -1, 0, zz);
                    count += breakIfLog(loc, logTypeToBreak, p);
                }
            }

            if (count < 3) {
                count += breakIfLog(locMax, logTypeToBreak, p);
            }

            locMax.add(0, -1, 0);

        }
    }

    public static int breakIfLog(Location loc, Material logMaterial, Player p) {
        Block b = loc.getBlock();
        if (treeMap.get(logMaterial).contains(b.getType())) {
            FarmerData.INSTANCE.addFarmerLocation(p.getUniqueId(), b.getLocation());
            p.breakBlock(b);
            return 1;
        }
        return 0;
    }


    private static void doAcaciaBreaking (Block b, Player p) {
        Location[] locationsToBreak = acaciaBlocksToFarm(b);

        for (int i = locationsToBreak.length -1; i >= Math.max(0, locationsToBreak.length -3); i--){
            Block bBreak = locationsToBreak[i].getBlock();
            FarmerData.INSTANCE.addFarmerLocation(p.getUniqueId(), bBreak.getLocation());
            p.breakBlock(bBreak);
        }
    }

    private static Location[] acaciaBlocksToFarm (Block b) {
        Location locStart = b.getLocation();

        LinkedHashSet<Location> locationsToCheck = new LinkedHashSet<>();
        LinkedHashSet<Location> logLocations = new LinkedHashSet<>();

        logLocations.add(locStart);
        locationsToCheck.add(locStart);

        while (!locationsToCheck.isEmpty()) {
            LinkedHashSet<Location> buffer = new LinkedHashSet<>();

            for (Location location : locationsToCheck) {
                buffer.addAll(checkForMoreALogs(location));
            }

            locationsToCheck.clear();
            for (Location l : buffer) {
                if (!logLocations.contains(l)) {
                    locationsToCheck.add(l);
                }
            }
            logLocations.addAll(buffer);
        }

        return logLocations.toArray(new Location[logLocations.size()]);
    }

    private static Set<Location> checkForMoreALogs (Location loc) {
        LinkedHashSet<Location> logLocations = new LinkedHashSet<>();

        Location[] locations = {
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ()),

                new Location(loc.getWorld(), loc.getBlockX()+1, loc.getBlockY()+1, loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY()+1, loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ()+1),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ()-1),

                new Location(loc.getWorld(), loc.getBlockX()+1, loc.getBlockY(), loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX()-1, loc.getBlockY(), loc.getBlockZ()),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()+1),
                new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()-1),
        };

        for (Location location : locations) {
            if (Material.ACACIA_LOG.equals(location.getBlock().getType())){
                logLocations.add(location);
            }
        }
        return logLocations;
    }
}
