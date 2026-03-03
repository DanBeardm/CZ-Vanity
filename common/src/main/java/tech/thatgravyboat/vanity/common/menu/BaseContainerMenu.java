package tech.thatgravyboat.vanity.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseContainerMenu extends AbstractContainerMenu {

    protected final ContainerLevelAccess access;

    protected BaseContainerMenu(int id, @Nullable MenuType<?> type, Inventory inventory, ContainerLevelAccess access, int invY, int hotbarY) {
        super(type, id);
        this.access = access;

        // Player inventory (3 rows) - slots 0..26
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, invY + y * 18));
            }
        }

        // Hotbar - slots 27..35
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, hotbarY));
        }
    }

    protected BaseContainerMenu(int id, @Nullable MenuType<?> type, Inventory inventory, ContainerLevelAccess access) {
        this(id, type, inventory, access, 138, 196); // default (old styling-table layout)
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.getSlot(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        // Slot layout:
        // 0..26  = player inventory
        // 27..35 = hotbar
        // 36..   = menu slots (added by child menus after super())
        int playerInvStart = 0;
        int playerInvEnd = 27;     // exclusive
        int hotbarStart = 27;
        int hotbarEnd = 36;        // exclusive

        int menuStart = 36;
        int menuEnd = this.slots.size();

        // From menu -> player
        if (index >= menuStart) {
            if (!this.moveItemStackTo(stack, playerInvStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
        }
        // From player -> menu
        else {
            if (!this.moveItemStackTo(stack, menuStart, menuEnd, false)) {
                // fallback between inv/hotbar like vanilla
                if (index < hotbarStart) {
                    if (!this.moveItemStackTo(stack, hotbarStart, hotbarEnd, false)) return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(stack, playerInvStart, playerInvEnd, false)) return ItemStack.EMPTY;
                }
            }
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.access.evaluate(
                (level, pos) -> {
                    if (level.getBlockEntity(pos) == null) return false;
                    return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
                },
                true
        );
    }

    @Nullable
    public BlockPos getBlockPos() {
        return this.access.evaluate((world, pos) -> pos).orElse(null);
    }
}