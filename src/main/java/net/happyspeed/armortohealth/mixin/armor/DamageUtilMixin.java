package net.happyspeed.armortohealth.mixin.armor;

import net.happyspeed.armortohealth.config.ModConfigs;
import net.minecraft.entity.DamageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DamageUtil.class, priority = 500)
public class DamageUtilMixin {
    @Inject(method = "getDamageLeft", at = @At(value = "RETURN"), cancellable = true)
    private static void ArmorOverride(float damage, float armor, float armorToughness, CallbackInfoReturnable<Float> cir) {
        if (ModConfigs.ARMORTOHEALTHENABLED) {
            float newDamage = damage;
            if (ModConfigs.HALFDAMAGE) {
                newDamage = damage / 2;
            }
            cir.setReturnValue(newDamage);
            cir.cancel();
        }
    }
}
