package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AnimalArmorDisplay extends EntityDisplay {

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 7, 0);
        super.render(graphics, x, y, width, height, rotation);
        graphics.pose().popPose();
    }

    @Override
    public LivingEntity setValue(Level level, ItemStack stack) {
        if (stack.getItem() instanceof AnimalArmorItem armorItem) {
            if (armorItem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
                Horse horse = EntityType.HORSE.create(level);
                if (horse != null) {
                    horse.setBodyArmorItem(stack);
                }
                return horse;
            } else if (armorItem.getBodyType() == AnimalArmorItem.BodyType.CANINE) {
                Wolf wolf = EntityType.WOLF.create(level);
                if (wolf != null) {
                    wolf.setBodyArmorItem(stack);
                }
                return wolf;
            }
        }
        return EntityType.PIG.create(level);
    }
}
