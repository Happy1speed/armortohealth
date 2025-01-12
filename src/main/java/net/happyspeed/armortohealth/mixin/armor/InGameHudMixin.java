package net.happyspeed.armortohealth.mixin.armor;

import net.happyspeed.armortohealth.config.ModConfigs;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = InGameHud.class, priority = 507)
public class InGameHudMixin {
    @ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 4))
    public int BreakArmorIconRender(int constant) {
        if (ModConfigs.ARMORTOHEALTHENABLED) {
            return 0;
        }
        return constant;
    }

}
