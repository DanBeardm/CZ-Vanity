package tech.thatgravyboat.vanity.common.menu.provider;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

import java.util.List;

public class CommandStylingProvider implements ContentMenuProvider<StylingMenuContent> {

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        // "virtual/command" mode (no block access required)
        return new StylingMenu(id, inv, ContainerLevelAccess.NULL, DesignManager.get(true));
    }

    @Override
    public StylingMenuContent createContent(ServerPlayer player) {
        return new StylingMenuContent(player.blockPosition(), List.of());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentConstants.CONTAINER_TITLE;
    }

    @Override
    public boolean resetMouseOnOpen() {
        return false;
    }
}