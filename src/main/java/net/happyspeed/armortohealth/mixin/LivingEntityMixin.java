package net.happyspeed.armortohealth.mixin;

import net.happyspeed.armortohealth.ArmorToHealthMod;
import net.happyspeed.armortohealth.config.ModConfigs;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.HealthBoostStatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.UUID;

@Mixin(value = LivingEntity.class, priority = 507)
abstract class LivingEntityMixin extends Entity {
	@Shadow @Final private AttributeContainer attributes;


	@Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

	@Shadow public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow public abstract float getMaxHealth();

	@Shadow public abstract boolean isAlive();

	@Shadow public abstract float getHealth();

	@Shadow public abstract void setHealth(float health);

	@Unique private int damagedTime1 = 0;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
    private void AdjustHealth(EntityType entityType, World world, CallbackInfo ci) {
		this.healing(this.getMaxHealth());
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

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V"), cancellable = true)
	public void detectDamaged (DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		this.damagedTime1 = 0;
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void Tick(CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			if (this.damagedTime1 < 20000) {
				this.damagedTime1++;
			}

			if (this.isAlive() && this.isLiving() && !this.getType().equals(EntityType.ENDER_DRAGON) && !this.getType().equals(EntityType.WITHER) && !this.getType().equals(EntityType.WARDEN)) {
				if (ModConfigs.MOBHEALING) {
					if (!this.isPlayer() && !this.isOnFire() && this.damagedTime1 > ModConfigs.MOBHEALINGTIMESINCEATTACK && this.getHealth() < this.getMaxHealth()) {
						if (this.getHealth() < this.getMaxHealth() && this.age % ModConfigs.MOBHEALINGTIME == 0) {
							this.healing(ModConfigs.MOBHEALINGAMOUNT);
						}
					}
				}
				if (ModConfigs.PLAYERPASSIVEREGEN) {
					if (this.isPlayer() && !this.isOnFire() && this.damagedTime1 > ModConfigs.PLAYERPASSIVEREGENTIMESINCEATTACK && this.getHealth() < this.getMaxHealth()) {
						if (this.getHealth() < this.getMaxHealth() && this.age % ModConfigs.PLAYERPASSIVEREGENTIME == 0) {
							this.healing(ModConfigs.PLAYERPASSIVEREGENAMOUNT);
						}
					}
				}
			}
		}
	}
	@Unique
    private void healing(float amount) {
		float f = this.getHealth();
		if (f > 0.0f) {
			if (f + amount <= this.getMaxHealth()) {
				this.setHealth(f + amount);
			} else {
				this.setHealth(this.getMaxHealth());
			}
		}
	}
}