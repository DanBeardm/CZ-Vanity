package tech.thatgravyboat.vanity.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import tech.thatgravyboat.vanity.common.menu.provider.CommandStylingProvider;

public final class VanityCommand {

    private VanityCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("vanity")
                        // only players can use it (no console)
                        .requires(source -> source.getEntity() instanceof ServerPlayer)
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            new CommandStylingProvider().openMenu(player); // opens your styling GUI
                            return 1;
                        })
        );
    }
}