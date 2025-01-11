package net.happyspeed.armortohealth.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = InGameHud.class, priority = 507)
public class InGameHudMixin {
    @ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 4))
    public int BreakArmorIconRender(int constant) {
        return 0;
    }
}
