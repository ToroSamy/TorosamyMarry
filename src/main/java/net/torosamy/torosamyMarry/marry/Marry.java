package net.torosamy.torosamyMarry.marry;

import net.torosamy.torosamyMarry.TorosamyMarry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Marry {
    private final String uuid;

    private final String manName;

    private final String womanName;
    
    private final Long startTime;
    
    private Long manLogoutTime;
    
    private Long womanLogoutTime;

    private Location home = null;
    
    
    public Marry(String manName, String womanName) {
        this.manName = manName;
        this.womanName = womanName;
        this.startTime = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString();
    }

    public Marry(String uuid, String manName, String womanName, Long manLogoutTime, Long womanLogoutTime, Location home, Long startTime) {
        this.uuid = uuid;
        this.manName = manName;
        this.womanName = womanName;
        this.manLogoutTime = manLogoutTime;
        this.womanLogoutTime = womanLogoutTime;
        this.home = home;
        this.startTime = startTime;
    }

    public String getOtherName(String playerName) {
        if (womanName.equals(playerName)) {
            return manName;
        }
        return womanName;
    }
    
    public boolean delete() {
        File marryFolder = new File(TorosamyMarry.instance.getDataFolder(), "marry");

        if (!marryFolder.exists()) {
            return true;
        }
        
        File file = new File(marryFolder, uuid + ".yml");
        
        if (!file.exists()) {
            return true;
        }

        return file.delete();
    }
    
    public void save() {
        File marryFolder = new File(TorosamyMarry.instance.getDataFolder(), "marry");

        if (!marryFolder.exists()) {
            marryFolder.mkdirs();
        }
        
        YamlConfiguration config = new YamlConfiguration();
        saveManName(config);
        saveWomanName(config);
        saveHome(config);
        saveStartTime(config);
        saveUUID(config);
        saveManLogoutTime(config);
        saveWomanLogoutTime(config);
        
        
        File file = new File(marryFolder, uuid + ".yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private YamlConfiguration saveManLogoutTime(YamlConfiguration config) {
        config.set("man-logout-time", manLogoutTime);
        return config;
    }

    private YamlConfiguration saveWomanLogoutTime(YamlConfiguration config) {
        config.set("woman-logout-time", womanLogoutTime);
        return config;
    }

    private YamlConfiguration saveManName(YamlConfiguration config) {
        config.set("man-name", manName);
        return config;
    }


    private YamlConfiguration saveWomanName(YamlConfiguration config) {
        config.set("woman-name", womanName);
        return config;
    }
    private YamlConfiguration saveUUID(YamlConfiguration config) {
        config.set("uuid", uuid);
        return config;
    }
    private YamlConfiguration saveStartTime(YamlConfiguration config) {
        config.set("start-time", startTime);
        return config;
    }
    
    private YamlConfiguration saveHome(YamlConfiguration config) {
        if (this.home == null) {
            return config;
        }
        
        config.set("homes.x", home.getX());
        config.set("homes.y", home.getY());
        config.set("homes.z", home.getZ());
        config.set("homes.pitch", home.getPitch());
        config.set("homes.yaw", home.getYaw());
        config.set("homes.world", home.getWorld().getName());
        
        return config;
    }

    
    public static Marry load(ConfigurationSection config) {
        String manName = loadManName(config);
        if (manName == null) {
            return null;
        }

        String womanName = loadWomanName(config);
        if (womanName == null) {
            return null;
        }

        String uuid = loadUUID(config);
        if (uuid == null) {
            return null;
        }

        long startTime = loadStartTime(config);
        if (startTime == -1) {
            return null;
        }

        long manLogoutTime = loadManLogoutTime(config);

        long womanLogoutTime = loadWomanLogoutTime(config);

        Location home = loadHome(config);
        
        return new Marry(uuid, manName, womanName, manLogoutTime, womanLogoutTime, home, startTime);
    }
    private static long loadManLogoutTime(ConfigurationSection config) {
        return config.getLong("man-logout-time", System.currentTimeMillis());
    }
    

    private static long loadWomanLogoutTime(ConfigurationSection config) {
        return config.getLong("woman-logout-time", System.currentTimeMillis());
    }
    private static String loadManName(ConfigurationSection config) {
        return config.getString("man-name", null);
    }


    private static String loadWomanName(ConfigurationSection config) {
        return config.getString("woman-name", null);
    }
    
    private static String loadUUID(ConfigurationSection config) {
        return config.getString("uuid", null);
    }
    
    private static long loadStartTime(ConfigurationSection config) {
        return config.getLong("start-time", -1);
    }

    private static Location loadHome(ConfigurationSection config) {
        String worldName = config.getString("homes.world", null);
        if (worldName == null) {
            return null;
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }
        

        double x = config.getDouble("homes.x", 0);
        double y = config.getDouble("homes.y", 0);
        double z = config.getDouble("homes.z", 0);
        float yaw = (float) config.getDouble("homes.yaw", 0);
        float pitch = (float) config.getDouble("homes.pitch", 0);
        
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public boolean isMan(String playerName) {
        return manName.equals(playerName);
    }
    
    
    public String getManName() {
        return manName;
    }

    public String getWomanName() {
        return womanName;
    }
    
    public Long getStartTime() {
        return startTime;
    }

    public Location getHome() {
        return home;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setHome(Location home) {
        this.home = home;
    }

    public Long getWomanLogoutTime() {
        return womanLogoutTime;
    }

    public void setWomanLogoutTime(Long womanLogoutTime) {
        this.womanLogoutTime = womanLogoutTime;
    }

    public Long getManLogoutTime() {
        return manLogoutTime;
    }

    public void setManLogoutTime(Long manLogoutTime) {
        this.manLogoutTime = manLogoutTime;
    }
}
