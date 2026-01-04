package net.torosamy.torosamyMarry.scheduler;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class TeleportTask extends BukkitRunnable {
    private final Player player;
    private final Location targetLocation;
    private final Location startLocation;
    private int countdown = ConfigUtil.mainConfig.teleportWaitTime;

    public static boolean startTask(Player player, Location targetLocation) {
        if (player == null || targetLocation == null) {
            return false;
        }
        new TeleportTask(player, targetLocation)
                .runTaskTimer(TorosamyMarry.instance, 0L, 20L);
        return true;
    }

    private TeleportTask(Player player, Location targetLocation) {
        this.player = player;
        this.targetLocation = targetLocation;
        this.startLocation = player.getLocation();
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }
        
        if (targetLocation.getWorld() == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.teleportTaskUnsafe));
            this.cancel();
            return;
        }
        
        if (!isPlayerStill(player.getLocation(), startLocation)) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.teleportTaskMove));
            this.cancel();
            return;
        }
        
        if (player.hasPermission(ConfigUtil.mainConfig.teleportWaitTimeBypassPermission) || countdown <= 0) {
            player.teleport(targetLocation);
            this.cancel();
            return;
        }
        
        String bigTitle = ConfigUtil.langConfig.teleportTaskBigTitle.replaceAll("%s", String.valueOf(countdown));
        String smallTitle = ConfigUtil.langConfig.teleportTaskSmallTitle.replaceAll("%s", String.valueOf(countdown));
        
        player.sendTitle(MessageUtil.format(bigTitle), MessageUtil.format(smallTitle));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
        countdown--;
    }
    
    private boolean isPlayerStill(Location current, Location start) {
        return current.getX() == start.getX() &&
                current.getY() == start.getY() &&
                current.getZ() == start.getZ();
    }
}