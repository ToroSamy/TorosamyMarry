package net.torosamy.torosamyMarry.listener;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.api.MarryAPI;
import net.torosamy.torosamyMarry.marry.Marry;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!MarryAPI.isMarried(player.getName())) {
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        
        if (marry == null) {
            return;
        }

        Player partner = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (partner == null || !partner.isOnline()) {
            long logoutTime = marry.isMan(player.getName()) ? marry.getWomanLogoutTime() : marry.getManLogoutTime();
            
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.partnerOfflineMessage.replaceAll("%time%", MessageUtil.formatTimestamp(logoutTime))));
            return;
        }
        
        player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.partnerOnlineMessage));
        partner.sendTitle(
                MessageUtil.format(ConfigUtil.langConfig.partnerOnlineBigTitle),
                MessageUtil.format(ConfigUtil.langConfig.partnerOnlineSmallTitle)
        );
        partner.playSound(partner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!MarryAPI.isMarried(player.getName())) {
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());

        if (marry == null) {
            return;
        }

        if (marry.isMan(player.getName())) {
            marry.setManLogoutTime(System.currentTimeMillis());
        }else {
            marry.setWomanLogoutTime(System.currentTimeMillis());
        }
        marry.save();
        Player partner = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (partner == null || !partner.isOnline()) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.partnerOfflineMessage));
            return;
        }

        partner.sendTitle(
                MessageUtil.format(ConfigUtil.langConfig.partnerOfflineBigTitle),
                MessageUtil.format(ConfigUtil.langConfig.partnerOfflineSmallTitle)
        );
        partner.playSound(partner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
    }
}
