package net.torosamy.torosamyMarry.utils;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.config.Config;
import net.torosamy.torosamyCore.config.ConfigFile;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.config.LangConfig;
import net.torosamy.torosamyMarry.config.MainConfig;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
    private static final List<Config> configs = new ArrayList<>();

    public static final MainConfig mainConfig = new MainConfig();
    public static final LangConfig langConfig = new LangConfig();

    public static void initConfig() {
        configs.clear();
        configs.add(new Config(mainConfig, new ConfigFile(TorosamyMarry.instance, "config.yml")));
        configs.add(new Config(langConfig, new ConfigFile(TorosamyMarry.instance, "lang.yml")));
    }

    public static void reloadConfig() {
        for (Config config : configs) {
            config.load();
        }
    }

    public static void saveConfig() {
        for (Config config : configs) {
            config.save();
        }
    }
}
