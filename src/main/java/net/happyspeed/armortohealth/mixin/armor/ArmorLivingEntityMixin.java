package net.happyspeed.armortohealth.mixin.armor;

import net.happyspeed.armortohealth.ArmorToHealthMod;
import net.happyspeed.armortohealth.config.ModConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = LivingEntity.class, priority = 507)
abstract class ArmorLivingEntityMixin extends Entity {

    @Shadow @Nullable
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract boolean isAlive();

    @Shadow public abstract float getHealth();

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract void heal(float amount);

    public ArmorLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void AdjustHealth(EntityType entityType, World world, CallbackInfo ci) {
        this.heal(this.getMaxHealth());
    }

    @Inject(method = "sendEquipmentChanges()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;sendEquipmentChanges(Ljava/util/Map;)V"))
    private void LivingEntityEquipmentChange(CallbackInfo ci) {
        EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (entityAttributeInstance != null) {
            EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(UUID.fromString(ArmorToHealthMod.AddedHealthUUID), EntityAttributes.GENERIC_MAX_HEALTH.getTranslationKey(), (this.getAttributeValue(EntityAttributes.GENERIC_ARMOR) * ModConfigs.HEALTHPERARMORPOINT) + (this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS) * ModConfigs.HEALTHPERARMORTOUGHNESSPOINT), EntityAttributeModifier.Operation.ADDITION);
            entityAttributeInstance.removeModifier(entityAttributeModifier);
            entityAttributeInstance.addPersistentModifier(entityAttributeModifier);
            if (this.getHealth() > this.getMaxHealth()) {
                this.setHealth(this.getMaxHealth());
            }
        }
    }
}
