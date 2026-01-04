package net.torosamy.torosamyMarry.commands;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.api.MarryAPI;
import net.torosamy.torosamyMarry.marry.Marry;
import net.torosamy.torosamyMarry.marry.MarryRequest;
import net.torosamy.torosamyMarry.scheduler.TeleportTask;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.time.Duration;

public class PlayerCommands {
    @Command(value = "marry sethome", requiredSender = Player.class)
    @Permission("torosamymarry.sethome")
    @CommandDescription("设置脚下为共同的家")
    public void setHome(CommandSender commandSender) {
        Player player = (Player) commandSender;
        
        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }
        
        marry.setHome(player.getLocation());
        player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.setHomeSelf));
        Player other = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (other != null && other.isOnline()) {
            other.sendMessage(MessageUtil.format(ConfigUtil.langConfig.setHomeOther));
        }
    }
    @Command(value = "marry help", requiredSender = Player.class)
    @Permission("torosamymarry.help")
    @CommandDescription("查看帮助")
    public void help(CommandSender commandSender) {
        Player player = (Player) commandSender; 
        ConfigUtil.langConfig.help.forEach(it-> {
            player.sendMessage(MessageUtil.format(player, it));
        });
    }
    @Command(value = "marry bag", requiredSender = Player.class)
    @Permission("torosamymarry.bag")
    @CommandDescription("查看您伴侣的背包")
    public void lookupBag(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Player other = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (other == null || !other.isOnline()) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }

        player.openInventory(other.getInventory());
    }

    @Command(value = "mtp", requiredSender = Player.class)
    @Permission("torosamymarry.tp")
    @CommandDescription("传送至您的伴侣身边")
    public void mtp(CommandSender commandSender) {
        teleport(commandSender);
    }
    @Command(value = "marry tp", requiredSender = Player.class)
    @Permission("torosamymarry.tp")
    @CommandDescription("传送至您的伴侣身边")
    public void tp(CommandSender commandSender) {
        teleport(commandSender);
    }
    public void teleport(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Player other = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (other == null || !other.isOnline()) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }

        if (!TeleportTask.startTask(player, other.getLocation())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.teleportTaskUnsafe));
        }
    }

    @Command(value = "marry home", requiredSender = Player.class)
    @Permission("torosamymarry.home")
    @CommandDescription("传送回共同的家")
    public void home(CommandSender commandSender) {
        teleportHome(commandSender);
    }
    @Command(value = "mhome", requiredSender = Player.class)
    @Permission("torosamymarry.home")
    @CommandDescription("传送回共同的家")
    public void mh(CommandSender commandSender) {
        teleportHome(commandSender);
    }
    private void teleportHome(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Location home = marry.getHome();
        
        if (home == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.notSetAnyHome));
            return;
        }

        if (!TeleportTask.startTask(player, home)) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.teleportTaskUnsafe));
        }
    }

    @Command(value = "marry time", requiredSender = Player.class)
    @Permission("torosamymarry.time")
    @CommandDescription("查看结婚时长")
    public void duration(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        String startTime = MessageUtil.formatTimestamp(marry.getStartTime());

        player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.lookupStartTime.replaceAll("%time%", startTime)));
        player.sendMessage(formatDuration(System.currentTimeMillis() - marry.getStartTime()));
    }

    @Command(value = "marry invite-as-man <player>", requiredSender = Player.class)
    @Permission("torosamymarry.invite")
    @CommandDescription("作为男方向玩家发送结婚请求")
    public void inviteAsMan(CommandSender commandSender, @Argument("player") Player receiver) {
        invite(commandSender, receiver, true);
    }

    @Command(value = "marry invite-as-woman <player>", requiredSender = Player.class)
    @Permission("torosamymarry.invite")
    @CommandDescription("作为女方向玩家发送结婚请求")
    public void inviteAsWoman(CommandSender commandSender, @Argument("player") Player receiver) {
        invite(commandSender, receiver, false);
    }

    @Command(value = "kiss")
    @Permission("torosamymarry.kiss")
    @CommandDescription("亲吻您的伴侣")
    public void kiss(Player player) {
        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Player partner = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (partner == null || !partner.isOnline()) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }

        Location playerLocation = player.getLocation();
        Location partnerLocation = partner.getLocation();

        if (ConfigUtil.mainConfig.kissDistance > 0) {
            if (!playerLocation.getWorld().getName().equals(partnerLocation.getWorld().getName()) ||
                playerLocation.distance(partnerLocation) > ConfigUtil.mainConfig.kissDistance) {

                player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.notNearby));
                return;
            }
        }
        
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        partner.playSound(partner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);
        partner.getWorld().spawnParticle(Particle.HEART, partner.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);

        
        partner.sendTitle(
                MessageUtil.format(ConfigUtil.langConfig.kissBigTitle),
                MessageUtil.format(ConfigUtil.langConfig.kissSmallTitle)
        );
    }

    @Command(value = "hug")
    @Permission("torosamymarry.hug")
    @CommandDescription("拥抱您的伴侣")
    public void hug(Player player) {
        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Player partner = Bukkit.getPlayer(marry.getOtherName(player.getName()));
        if (partner == null || !partner.isOnline()) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }

        Location playerLocation = player.getLocation();
        Location partnerLocation = partner.getLocation();

        if (ConfigUtil.mainConfig.hugDistance > 0) {
            if (!playerLocation.getWorld().getName().equals(partnerLocation.getWorld().getName()) ||
                playerLocation.distance(partnerLocation) > ConfigUtil.mainConfig.hugDistance) {
                
                player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.notNearby));
                return;
            }
        }

        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);
        partner.getWorld().spawnParticle(Particle.HEART, partner.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);

        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);
        partner.getWorld().spawnParticle(Particle.HEART, partner.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0.1);


        partner.sendTitle(
                MessageUtil.format(ConfigUtil.langConfig.hugBigTitle),
                MessageUtil.format(ConfigUtil.langConfig.hugSmallTitle)
        );
    }

    public void invite(CommandSender commandSender, Player receiver, boolean senderIsMan) {
        if (commandSender.getName().equals(receiver.getName())) {
            commandSender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.selfError));
            return;
        }
        
        Player sender = (Player) commandSender;

        if (!receiver.isOnline()) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }

        if (MarryAPI.isMarried(sender.getName())) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.selfHasMarried));
            return;
        }

        if (MarryAPI.isMarried(receiver.getName())) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.targetHasMarried));
            return;
        }

        if (MarryAPI.isRepeat(sender.getName(), receiver.getName())) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.requestRepeat));
            return;
        }

        new MarryRequest(sender, receiver, senderIsMan);
        sender.sendMessage(MessageUtil.format(receiver, ConfigUtil.langConfig.requestSend));
        receiver.sendMessage(MessageUtil.format(sender, ConfigUtil.langConfig.requestReceive));
    }

    @Command(value = "marry accept <player>", requiredSender = Player.class)
    @Permission("torosamymarry.accept")
    @CommandDescription("同意对方的结婚请求")
    public void accept(CommandSender commandSender, @Argument("player") Player sender) {
        if (commandSender.getName().equals(sender.getName())) {
            commandSender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.selfError));
            return;
        }
        
        Player receiver = (Player) commandSender;

        if (!sender.isOnline()) {
            receiver.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound));
            return;
        }
        
        if (MarryAPI.isMarried(receiver.getName())) {
            receiver.sendMessage(MessageUtil.format(ConfigUtil.langConfig.selfHasMarried));
            return;
        }

        if (MarryAPI.isMarried(sender.getName())) {
            receiver.sendMessage(MessageUtil.format(ConfigUtil.langConfig.targetHasMarried));
            return;
        }

        MarryRequest request = MarryAPI.getRequest(sender.getName(), receiver.getName());
        
        if (request == null) {
            receiver.sendMessage(MessageUtil.format(ConfigUtil.langConfig.requestNotFound));
            return;
        }

        boolean senderIsMan = request.isSenderIsMan();
        MarryAPI.removeRequest(request.getUuid());
        
        String manName = senderIsMan ? sender.getName() : receiver.getName();
        String womanName = senderIsMan ? receiver.getName() : sender.getName();

        Marry marry = new Marry(manName, womanName);
        marry.save();
        MarryAPI.add(marry);


        String bigTitle = MessageUtil.format(ConfigUtil.langConfig.requestAcceptBigTitle
                .replaceAll("%woman_name%", womanName)
                .replaceAll("%man_name%", manName)
        );

        String smallTitle = MessageUtil.format(ConfigUtil.langConfig.requestAcceptSmallTitle
                .replaceAll("%woman_name%", womanName)
                .replaceAll("%man_name%", manName)
        );
        
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            onlinePlayer.sendTitle(bigTitle, smallTitle);
        }
    }
    @Command(value = "marry divorce", requiredSender = Player.class)
    @Permission("torosamymarry.divorce")
    @CommandDescription("离婚")
    public void divorce(CommandSender commandSender) {
        Player player = (Player) commandSender;

        if (!MarryAPI.isMarried(player.getName())) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        Marry marry = MarryAPI.getMarry(player.getName());
        
        if (marry == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }

        String otherName = marry.getOtherName(player.getName());

        if (!MarryAPI.divorce(marry)) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.singleError));
            return;
        }
        
        
        player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.divorceSender));

        Player other = Bukkit.getPlayer(otherName);
        
        if (other != null && other.isOnline()) {
            other.sendMessage(MessageUtil.format(ConfigUtil.langConfig.divorceReceiver));
        }
    }

    public String formatDuration(long diffMs) {
        Duration duration = Duration.ofMillis(diffMs);

        String days = String.valueOf(duration.toDays());
        String hours = String.valueOf(duration.toHours() % 24);
        String minutes = String.valueOf(duration.toMinutes() % 60);
        String seconds = String.valueOf(duration.getSeconds() % 60);

        return MessageUtil.format(ConfigUtil.langConfig.durationFormat
                .replaceAll("%s%", seconds)
                .replaceAll("%d%", days)
                .replaceAll("%h%", hours)
                .replaceAll("%m%", minutes)
        );
    }
}
