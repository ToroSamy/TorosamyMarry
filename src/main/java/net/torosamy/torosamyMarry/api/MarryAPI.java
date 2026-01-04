package net.torosamy.torosamyMarry.api;

import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.config.ConfigFile;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.marry.DivorceRecord;
import net.torosamy.torosamyMarry.marry.Marry;
import net.torosamy.torosamyMarry.marry.MarryRequest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarryAPI {
    //uuid request
    private static Map<String, MarryRequest> requests = new HashMap();

    private static List<DivorceRecord> records = new ArrayList<>();
    
    private static List<Marry> marries = new ArrayList<>();

    public static boolean isMarried(String playerName) {
        for (Marry marry : marries) {
            if (marry.getManName().equals(playerName)) {
                return true;
            }

            if (marry.getWomanName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public static int getDivorceTimes(String playerName) {
        int sum = 0;
        for (DivorceRecord record : records) {
            if (record.getManName().equals(playerName) ||
                record.getWomanName().equals(playerName)) {
                sum ++;
            }
        }
        return sum;
    }
    
    public static void save() {
        for (Marry marry : marries) {
            marry.save();
        }
        YamlConfiguration config = new YamlConfiguration();
        for (DivorceRecord record : records) {
            record.save(config);
        }
        File file = new File(TorosamyMarry.instance.getDataFolder(), "records.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void add(Marry marry) {
        marries.add(marry);
    }
    
    public static void load() {
        records.clear();
        marries.clear();
        requests.values().forEach(it->it.getAutoCancelTask().cancel());
        requests.clear();

        for (ConfigurationSection section : TorosamyCoreAPI.getConfigs(TorosamyMarry.instance, List.of("marry")).values()) {
            Marry marry = Marry.load(section);
            if (marry == null) {
                continue;
            }
            marries.add(marry);
        }
        
        YamlConfiguration config = new ConfigFile(TorosamyMarry.instance, "records.yml").getConfig();

        for (String key : config.getKeys(false)) {
            DivorceRecord record = DivorceRecord.load(config.getConfigurationSection(key));
            if (record == null) {
                continue;
            }
            records.add(record);
        }
        
    }
    
    public static boolean divorce(Marry marry) {
        if (marry == null) {
            return false;
        }

        if (!marries.remove(marry)) {
            return false;
        }

        records.add(new DivorceRecord(marry.getStartTime(), marry.getWomanName(), marry.getManName()));

        return marry.delete();
    }
    
    public static Marry getMarry(String playerName) {
        for (Marry marry : marries) {
            if (marry.getWomanName().equals(playerName)) {
                return marry;
            }

            if (marry.getManName().equals(playerName)) {
                return marry;
            }
        }
        return null;
    }

    public static boolean isRepeat(String senderName, String receiverName) {
        for (MarryRequest request : requests.values()) {
            if (request.getSenderName().equals(senderName) && request.getReceiverName().equals(receiverName)) {
                return true;
            }

            if (request.getReceiverName().equals(senderName) && request.getSenderName().equals(receiverName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeRequest(String uuid) {
        MarryRequest request = requests.get(uuid);
        if (request == null) {
            return false;
        }

        request.getAutoCancelTask().cancel();
        requests.remove(uuid);
        return true;
    }

    public static void putRequest(MarryRequest request) {
        requests.put(request.getUuid(), request);
    }

    public static MarryRequest getRequest(String senderName, String receiverName) {
        for (MarryRequest request : requests.values()) {
            if (request.getSenderName().equals(senderName) && request.getReceiverName().equals(receiverName)) {
                return request;
            }

            if (request.getReceiverName().equals(senderName) && request.getSenderName().equals(receiverName)) {
                return request;
            }
        }
        return null;
    }
}
