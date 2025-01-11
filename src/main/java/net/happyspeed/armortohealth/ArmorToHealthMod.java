package net.happyspeed.armortohealth;

import net.fabricmc.api.ModInitializer;

import net.happyspeed.armortohealth.config.ModConfigs;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorToHealthMod implements ModInitializer {
	public static final String MOD_ID = "armortohealth";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static String AddedHealthUUID = "66d5b9f8-e3da-45fc-92de-a055739f8cee";

	@Override
	public void onInitialize() {
		LOGGER.info("Healthy Defense");
		ModConfigs.registerConfigs();
	}
}