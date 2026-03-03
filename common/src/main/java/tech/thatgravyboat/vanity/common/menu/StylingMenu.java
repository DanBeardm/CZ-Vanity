package tech.thatgravyboat.vanity.common.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import tech.thatgravyboat.vanity.common.menu.container.AwareContainer;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;
import tech.thatgravyboat.vanity.common.registries.ModItems;
import tech.thatgravyboat.vanity.common.registries.ModMenuTypes;
import tech.thatgravyboat.vanity.common.registries.ModSounds;
import tech.thatgravyboat.vanity.common.Vanity;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StylingMenu extends BaseContainerMenu {

    private final DesignManager manager;

    // input[0] = item to style, input[1] = design token
    private final Container input = new AwareContainer(2, () -> {
        this.updateResult();
        this.slotsChanged(this.input);
    });
    public static final ResourceLocation REMOVE_DESIGN = Vanity.id("remove_design");

    private final Container result = new AwareContainer(1, () -> this.slotsChanged(this.input));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StylingMenu(int syncId, Inventory inv, Optional<StylingMenuContent> content) {
        this(
                syncId,
                inv,
                content.map(StylingMenuContent.access(inv.player)).orElse(ContainerLevelAccess.NULL),
                DesignManager.get(true)
        );
    }

    public StylingMenu(int syncId, Inventory inv, ContainerLevelAccess access, DesignManager manager) {
        // True anvil inventory layout
        super(syncId, ModMenuTypes.STYLING.get(), inv, access, 84, 142);
        this.manager = manager;

        // Left input (item)
        this.addSlot(new Slot(this.input, 0, 27, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getCount() == 1 && DesignHelper.getDesign(stack) == null;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                StylingMenu.this.updateResult();
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);
                StylingMenu.this.updateResult();
            }
        });

        // Right input (token)
        this.addSlot(new Slot(this.input, 1, 76, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                // Token is any stack that has a DESIGN component
                return DesignHelper.getDesign(stack) != null;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                StylingMenu.this.updateResult();
            }
        });

        // Output
        this.addSlot(new Slot(this.result, 0, 134, 47) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(@NotNull Player player) {
                ItemStack out = this.getItem();
                if (out.isEmpty()) return false;

                ItemStack in = StylingMenu.this.input.getItem(0);
                if (in.isEmpty()) return false;

                Pair<ResourceLocation, String> oldStyle = DesignHelper.getStyle(in);
                Pair<ResourceLocation, String> newStyle = DesignHelper.getStyle(out);

                // If input already styled, only allow removal (output must be unstyled)
                if (oldStyle != null) {
                    return newStyle == null;
                }

                // Removing a style is always allowed (covers the case oldStyle == null too)
                if (newStyle == null) return true;

                // Otherwise, require token matching the design being applied
                ResourceLocation tokenDesign = DesignHelper.getDesign(StylingMenu.this.input.getItem(1));
                return tokenDesign != null && tokenDesign.equals(newStyle.getFirst());
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack out) {
                // sound (works when opened via block; no-op for command)
                StylingMenu.this.access.execute((level, pos) ->
                        level.playSound(null, pos, ModSounds.TAKE_RESULT_STYLING_TABLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F)
                );

                ItemStack in = StylingMenu.this.input.getItem(0);
                ItemStack tokenStack = StylingMenu.this.input.getItem(1);

                Pair<ResourceLocation, String> oldStyle = DesignHelper.getStyle(in);
                Pair<ResourceLocation, String> newStyle = DesignHelper.getStyle(out);

                boolean removing = oldStyle != null && newStyle == null;
                boolean applying = oldStyle == null && newStyle != null;
                boolean swapping = oldStyle != null && newStyle != null && !oldStyle.getFirst().equals(newStyle.getFirst());

                // consume left input (like anvil)
                StylingMenu.this.input.removeItem(0, 1);

                // consume token when applying/swapping
                if ((applying || swapping) && newStyle != null) {
                    ResourceLocation tokenDesign = DesignHelper.getDesign(tokenStack);
                    if (tokenDesign != null && tokenDesign.equals(newStyle.getFirst())) {
                        StylingMenu.this.input.removeItem(1, 1);
                    }
                }

                // refund token when removing/swapping away from old
                if (removing && oldStyle != null) {
                    // If player has put a matching token in the token slot, don't refund another copy
                    ResourceLocation tokenDesign = DesignHelper.getDesign(tokenStack);
                    if (tokenDesign == null || !tokenDesign.equals(oldStyle.getFirst())) {
                        StylingMenu.this.refundDesignToken(player, oldStyle.getFirst());
                    }
                }

                StylingMenu.this.updateResult();
                super.onTake(player, out);
            }
        });

        this.updateResult();
    }
    // Legacy UI compatibility: older widgets/buttons still call menu.select(...)
    public void select(ResourceLocation design, @Nullable String style) {
        ItemStack target = this.input.getItem(0);
        if (target.isEmpty()) return;

        ItemStack out = target.copy();

        // Remove cosmetic
        if (REMOVE_DESIGN.equals(design)) {
            Pair<ResourceLocation, String> oldStyle = DesignHelper.getStyle(target);
            if (oldStyle == null) return;
            DesignHelper.setDesignAndStyle(out, oldStyle.getFirst(), null);
            this.result.setItem(0, out);
            return;
        }

        // Apply selected style (only if provided)
        if (style == null) return;
        DesignHelper.setDesignAndStyle(out, design, style);
        this.result.setItem(0, out);
    }

    private void updateResult() {
        ItemStack target = this.input.getItem(0);
        ItemStack token = this.input.getItem(1);

        this.result.setItem(0, ItemStack.EMPTY);
        if (target.isEmpty()) return;

        Pair<ResourceLocation, String> oldStyle = DesignHelper.getStyle(target);

// If it's already styled: ONLY allow removal, regardless of what's in the token slot
        if (oldStyle != null) {
            ItemStack out = target.copy();
            DesignHelper.setDesignAndStyle(out, null, null); // fully clear
            this.result.setItem(0, out);
            return;
        }

// If not styled and no token, nothing to do
        if (token.isEmpty()) return;

        // APPLY: token determines design; choose first compatible style
        ResourceLocation designId = DesignHelper.getDesign(token);
        if (designId == null) return;

        Optional<Design> designOpt = this.manager.getDesign(designId);
        if (designOpt.isEmpty()) return;

        List<String> stylesForItem = designOpt.get().getStylesForItem(target);
        if (stylesForItem.isEmpty()) return;

        String styleToApply = stylesForItem.get(0);

        ItemStack out = target.copy();
        DesignHelper.setDesignAndStyle(out, designId, styleToApply);
        this.result.setItem(0, out);
    }

    private void refundDesignToken(Player player, ResourceLocation designId) {
        ItemStack token = this.manager.getDesign(designId)
                .map(design -> DesignHelper.createDesignItem(designId, design))
                .orElseGet(() -> {
                    // fallback if design data missing
                    ItemStack stack = ModItems.DESIGN.get().getDefaultInstance().copyWithCount(1);
                    stack.set(tech.thatgravyboat.vanity.common.registries.ModDataComponents.DESIGN.get(), designId);
                    return stack;
                });

        if (!player.getInventory().add(token)) {
            player.drop(token, false);
        }
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.getSlot(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        // BaseContainerMenu always adds 36 player slots first (0..35)
        final int PLAYER_END = 36;

        // In THIS menu we add 3 slots after the player slots, in this order:
        // 36 = item input, 37 = token input, 38 = output
        final int ITEM_SLOT = PLAYER_END;
        final int TOKEN_SLOT = PLAYER_END + 1;
        final int RESULT_SLOT = PLAYER_END + 2;

        // 1) Shift-click OUTPUT -> move to player inventory then trigger onTake (consumes inputs)
        if (index == RESULT_SLOT) {
            if (!this.moveItemStackTo(stack, 0, PLAYER_END, true)) {
                return ItemStack.EMPTY;
            }

            // clear output slot
            if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            // IMPORTANT: run result slot logic (consumes tool + token / refunds on remove)
            slot.onTake(player, copy);
            return copy;
        }

        // 2) Shift-click from our menu inputs -> move back to player inventory
        if (index >= PLAYER_END) {
            if (!this.moveItemStackTo(stack, 0, PLAYER_END, true)) {
                return ItemStack.EMPTY;
            }
        }
        // 3) Shift-click from player inventory -> route to correct input slot
        else {
            boolean isToken = DesignHelper.getDesign(stack) != null;

            if (isToken) {
                // token goes to token slot only
                if (!this.moveItemStackTo(stack, TOKEN_SLOT, TOKEN_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // tool/armour goes to item slot only
                if (!this.moveItemStackTo(stack, ITEM_SLOT, ITEM_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        // Always return inputs (works for command-opened menus too)
        this.clearContainer(player, this.input);
    }
}