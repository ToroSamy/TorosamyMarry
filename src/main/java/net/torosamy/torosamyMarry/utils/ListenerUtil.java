package net.torosamy.torosamyMarry.utils;


import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.listener.JoinQuitListener;
//import net.torosamy.torosamyMarry.listener.DeathListener;
//import net.torosamy.torosamyMarry.listener.TeleportListener;
//import net.torosamy.torosamyMarry.listener.UpdateListener;

public class ListenerUtil {
    public static void registerListener() {
        TorosamyMarry.instance.getServer().getPluginManager().registerEvents(new JoinQuitListener(), TorosamyMarry.instance);
//        TorosamyMarry.instance.getServer().getPluginManager().registerEvents(new DeathListener(), TorosamyMarry.instance);
//        TorosamyMarry.instance.getServer().getPluginManager().registerEvents(new TeleportListener(), TorosamyMarry.instance);
    }
}
