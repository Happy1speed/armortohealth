package net.happyspeed.armortohealth.mixin;

import net.happyspeed.armortohealth.ArmorToHealthMod;
import net.happyspeed.armortohealth.config.ModConfigs;
import net.happyspeed.armortohealth.util.ModTags;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.HealthBoostStatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
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

	@Shadow public abstract float getMaxHealth();

	@Shadow public abstract boolean isAlive();

	@Shadow public abstract float getHealth();

	@Shadow public abstract void setHealth(float health);

	@Unique private int damagedTime1 = 0;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
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

			if (this.isAlive() && this.isLiving()) {
				if (ModConfigs.MOBHEALING && !this.getType().isIn(ModTags.Entity.NO_NATURAL_REGEN_MOB)) {
					if (!this.isPlayer() && !this.isOnFire() && this.damagedTime1 > ModConfigs.MOBHEALINGTIMESINCEATTACK && this.getHealth() < this.getMaxHealth()) {
						if (this.getHealth() < this.getMaxHealth() && this.age % ModConfigs.MOBHEALINGTIME == 0) {
							this.healing2(ModConfigs.MOBHEALINGAMOUNT);
						}
					}
				}
				if (ModConfigs.PLAYERPASSIVEREGEN) {
					if (this.isPlayer() && !this.isOnFire() && this.damagedTime1 > ModConfigs.PLAYERPASSIVEREGENTIMESINCEATTACK && this.getHealth() < this.getMaxHealth()) {
						if (this.getHealth() < this.getMaxHealth() && this.age % ModConfigs.PLAYERPASSIVEREGENTIME == 0) {
							this.healing2(ModConfigs.PLAYERPASSIVEREGENAMOUNT);
						}
					}
				}
			}
		}
	}
	@Unique
    private void healing2(float amount) {
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