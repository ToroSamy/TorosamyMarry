package net.torosamy.torosamyMarry;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.api.MarryAPI;
import net.torosamy.torosamyMarry.api.MarryPlaceholderAPI;
import net.torosamy.torosamyMarry.utils.CommandUtil;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import net.torosamy.torosamyMarry.utils.ListenerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TorosamyMarry extends JavaPlugin {
    public static TorosamyMarry instance;
    
    public static final MarryPlaceholderAPI MARRY_PLACEHOLDER_API = new MarryPlaceholderAPI();
    
    @Override
    public void onEnable() {
        instance = this;
        ConfigUtil.initConfig();
        ConfigUtil.reloadConfig();
        
        CommandUtil.registerCommand();
        ListenerUtil.registerListener();
        MarryAPI.load();
        MARRY_PLACEHOLDER_API.register();
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&a插件 &eTorosamyMarry &a成功开启喵~"));

    }

    @Override
    public void onDisable() {
        ConfigUtil.saveConfig();
        MarryAPI.save();
        MARRY_PLACEHOLDER_API.unregister();
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c插件 &eTorosamyMarry &c成功关闭喵~"));
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c作者 &eTorosamy|yweiyang"));
    }
}
