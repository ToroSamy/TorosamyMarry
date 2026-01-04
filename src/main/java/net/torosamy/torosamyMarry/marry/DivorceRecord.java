package net.torosamy.torosamyMarry.marry;

import net.torosamy.torosamyMarry.TorosamyMarry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DivorceRecord {
    private final String manName;
    
    private final String womanName;
    
    private final Long startTime;
    
    private final Long endTime;
    
    private final String uuid;

    public DivorceRecord(Long startTime, String womanName, String manName) {
        this.startTime = startTime;
        this.womanName = womanName;
        this.manName = manName;
        this.endTime = System.currentTimeMillis();
        this.uuid = UUID.randomUUID().toString();
    }
    
    public YamlConfiguration save(YamlConfiguration config) {
        config.set(uuid + ".man-name", manName);
        config.set(uuid + ".woman-name", womanName);
        config.set(uuid + ".start-time", startTime);
        config.set(uuid + ".end-time", endTime);
        config.set(uuid + ".uuid", uuid);
        
        return config;
    }

    public DivorceRecord(String manName, String womanName, Long startTime, Long endTime, String uuid) {
        this.manName = manName;
        this.womanName = womanName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.uuid = uuid;
    }

    public static DivorceRecord load(ConfigurationSection config) {
        String manName = config.getString("man-name", null);
        if (manName == null) {
            return null;
        }

        String womanName = config.getString("woman-name", null);
        if (womanName == null) {
            return null;
        }

        long startTime = config.getLong("start-time", -1);
        if (startTime == -1) {
            return null;
        }

        String uuid = config.getString("uuid", null);
        if (uuid == null) {
            return null;
        }
        
        long endTime = config.getLong("end-time", -1);
        if (endTime == -1) {
            return null;
        }
        
        return new DivorceRecord(manName, womanName, startTime, endTime, uuid);
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getWomanName() {
        return womanName;
    }

    public String getManName() {
        return manName;
    }
}
