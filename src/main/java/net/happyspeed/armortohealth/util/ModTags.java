package net.happyspeed.armortohealth.util;

import net.happyspeed.armortohealth.ArmorToHealthMod;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {

    public static class Entity {
        public static final TagKey<EntityType<?>> NO_NATURAL_REGEN_MOB =
                createTag("no_natural_regen_mob");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(ArmorToHealthMod.MOD_ID, name));
        }
    }
}
