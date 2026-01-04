package net.torosamy.torosamyMarry.utils;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.commands.CommandManager;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.commands.AdminCommands;
import net.torosamy.torosamyMarry.commands.PlayerCommands;

public class CommandUtil {
    private static final CommandManager commandManager = new CommandManager(TorosamyMarry.instance);
    
    public static final AdminCommands ADMIN_COMMANDS = new AdminCommands();
    public static final PlayerCommands PLAYER_COMMANDS = new PlayerCommands();
    
    public static void registerCommand() {
        commandManager.annotationParser.parse(ADMIN_COMMANDS);
        commandManager.annotationParser.parse(PLAYER_COMMANDS);
    }
}
