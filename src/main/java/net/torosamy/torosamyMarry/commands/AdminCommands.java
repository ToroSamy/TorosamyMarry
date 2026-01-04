package net.torosamy.torosamyMarry.commands;

import net.torosamy.torosamyCore.utils.MessageUtil;
import net.torosamy.torosamyMarry.api.MarryAPI;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

public class AdminCommands {
    @Command(value = "marryadmin reload")
    @Permission("torosamymarry.admin.reload")
    @CommandDescription("重载TorosamyMarry配置文件")
    public void reloadConfig(CommandSender sender) {
        ConfigUtil.reloadConfig();

        MarryAPI.load();

        sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.reloadMessage));
    }

    @Command(value = "marryadmin list")
    @Permission("torosamymarry.admin.list")
    @CommandDescription("重载TorosamyMarry配置文件")
    public void listMarry(CommandSender sender) {
        
    }

    @Command(value = "marryadmin count <player>")
    @Permission("torosamymarry.admin.count")
    @CommandDescription("统计玩家离婚次数")
    public void count(CommandSender sender, @Argument("player") String playerName) {
        int count = MarryAPI.getDivorceTimes(playerName);
        sender.sendMessage(String.valueOf(count));
    }
}
