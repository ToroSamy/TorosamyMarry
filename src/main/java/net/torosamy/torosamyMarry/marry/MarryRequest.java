package net.torosamy.torosamyMarry.marry;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.api.MarryAPI;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MarryRequest {
    private final String senderName;
    
    private final String receiverName;
    
    private final String uuid;
    
    private final boolean senderIsMan;

    private final BukkitTask autoCancelTask;

    public MarryRequest(Player sender, Player receiver, boolean senderIsMan) {
        this.senderName = sender.getName();
        this.receiverName = receiver.getName();
        this.uuid = UUID.randomUUID().toString();
        this.senderIsMan = senderIsMan;

        this.autoCancelTask = Bukkit.getScheduler().runTaskLaterAsynchronously(TorosamyMarry.instance, () -> {
            MarryAPI.removeRequest(this.uuid);

            if (sender.isOnline()) {
                sender.sendMessage(MessageUtil.format(receiver, ConfigUtil.langConfig.requestSenderExpired));
            }

            if (receiver.isOnline()) {
                receiver.sendMessage(MessageUtil.format(sender, ConfigUtil.langConfig.requestReceiverExpired));
            }
        }, 20L * ConfigUtil.mainConfig.requestExpiredTime);

        MarryAPI.putRequest(this);
    }
  

    public String getSenderName() {
        return senderName;
    }

    public boolean isSenderIsMan() {
        return senderIsMan;
    }

    public String getUuid() {
        return uuid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public BukkitTask getAutoCancelTask() {
        return autoCancelTask;
    }
}
