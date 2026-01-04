package net.torosamy.torosamyMarry.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.torosamy.torosamyMarry.TorosamyMarry;
import net.torosamy.torosamyMarry.utils.ConfigUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarryPlaceholderAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "marry";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Torosamy";
    }

    @Override
    public @NotNull String getVersion() {
        return TorosamyMarry.instance.getDescription().getVersion();
    }
    
    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if ("status".equals(params)) {
            return MarryAPI.isMarried(player.getName()) ?
                    ConfigUtil.langConfig.placeholderApiMarried :
                    ConfigUtil.langConfig.placeholderApiUnmarried;
        }
        return null;    
    }
    
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return onRequest(player, params);
    }
}
