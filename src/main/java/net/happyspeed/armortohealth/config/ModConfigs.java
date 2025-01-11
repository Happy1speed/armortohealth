package net.happyspeed.armortohealth.config;

import com.mojang.datafixers.util.Pair;
import net.happyspeed.armortohealth.ArmorToHealthMod;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static int HEALTHPERARMORPOINT;
    public static int HEALTHPERARMORTOUGHNESSPOINT;
    public static boolean HALFDAMAGE;
    public static boolean MOBHEALING;
    public static int MOBHEALINGAMOUNT;
    public static int MOBHEALINGTIME;
    public static int MOBHEALINGTIMESINCEATTACK;
    public static boolean PLAYERPASSIVEREGEN;
    public static int PLAYERPASSIVEREGENAMOUNT;
    public static int PLAYERPASSIVEREGENTIME;
    public static int PLAYERPASSIVEREGENTIMESINCEATTACK;


    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(ArmorToHealthMod.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("health_per_armor_point", 4));
        configs.addKeyValuePair(new Pair<>("health_per_armor_toughness_point", 4));
        configs.addKeyValuePair(new Pair<>("half_damage", false));
        configs.addKeyValuePair(new Pair<>("mob_healing", false));
        configs.addKeyValuePair(new Pair<>("mob_healing_amount", 1));
        configs.addKeyValuePair(new Pair<>("mob_healing_time", 40));
        configs.addKeyValuePair(new Pair<>("mob_healing_time_since_attack", 40));
        configs.addKeyValuePair(new Pair<>("player_passive_regen", false));
        configs.addKeyValuePair(new Pair<>("player_passive_regen_amount", 1));
        configs.addKeyValuePair(new Pair<>("player_passive_regen_time", 100));
        configs.addKeyValuePair(new Pair<>("player_passive_regen_time_since_attack", 40));
    }

    private static void assignConfigs() {
        HEALTHPERARMORPOINT = CONFIG.getOrDefault("health_per_armor_point", 4);
        HEALTHPERARMORTOUGHNESSPOINT = CONFIG.getOrDefault("health_per_armor_toughness_point", 4);
        HALFDAMAGE = CONFIG.getOrDefault("half_damage", false);
        MOBHEALING = CONFIG.getOrDefault("mob_healing", false);
        MOBHEALINGAMOUNT = CONFIG.getOrDefault("mob_healing_amount", 1);
        MOBHEALINGTIME = CONFIG.getOrDefault("mob_healing_time", 20);
        MOBHEALINGTIMESINCEATTACK = CONFIG.getOrDefault("mob_healing_time_since_attack", 40);
        PLAYERPASSIVEREGEN = CONFIG.getOrDefault("player_passive_regen", false);
        PLAYERPASSIVEREGENAMOUNT = CONFIG.getOrDefault("player_passive_regen_amount", 1);
        PLAYERPASSIVEREGENTIME = CONFIG.getOrDefault("player_passive_regen_time", 100);
        PLAYERPASSIVEREGENTIMESINCEATTACK = CONFIG.getOrDefault("player_passive_regen_time_since_attack", 40);




        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}