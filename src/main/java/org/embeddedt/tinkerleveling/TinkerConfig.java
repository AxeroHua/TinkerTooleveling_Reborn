package org.embeddedt.tinkerleveling;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class TinkerConfig {
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.IntValue maximumLevels;
    public static final ForgeConfigSpec.IntValue defaultBaseXP;
    public static final ForgeConfigSpec.DoubleValue levelMultiplier;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> toolBaseXpOverrides;
    public static final ForgeConfigSpec.EnumValue<SlotRewardType> slotRewardType;
    public static final ForgeConfigSpec.IntValue slotsPerLevel;
    public static final ForgeConfigSpec.BooleanValue allowArmorExploits;

    static {
        SERVER_BUILDER.push("leveling");
        maximumLevels = SERVER_BUILDER
                .comment("Maximum tool level. Set to 0 for no level limit.")
                .defineInRange("maximumLevels", 0, 0, Integer.MAX_VALUE);
        defaultBaseXP = SERVER_BUILDER
                .comment("XP required to advance from level 1 to level 2 when no tool override exists.")
                .defineInRange("defaultBaseXP", 250, 1, Integer.MAX_VALUE);
        levelMultiplier = SERVER_BUILDER
                .comment("Multiplier applied to the XP requirement after every level.",
                        "Formula: defaultBaseXP * levelMultiplier^(currentLevel - 1).")
                .defineInRange("levelMultiplier", 2.0D, 1.0D, Double.MAX_VALUE);
        toolBaseXpOverrides = SERVER_BUILDER
                .comment("Optional per-tool base XP entries in the form \"namespace:item=250\".",
                        "Example: [\"tconstruct:pickaxe=500\", \"tconstruct:cleaver=750\"]")
                .defineListAllowEmpty("toolBaseXpOverrides", List.of(), TinkerConfig::isValidToolXpOverride);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("rewards");
        slotRewardType = SERVER_BUILDER
                .comment("Slot granted on level-up. Allowed values: UPGRADE or ABILITY.",
                        "Changing this value also changes the type of all slots earned previously.")
                .defineEnum("slotRewardType", SlotRewardType.UPGRADE);
        slotsPerLevel = SERVER_BUILDER
                .comment("Number of slots granted for each level gained.")
                .defineInRange("slotsPerLevel", 1, 1, 64);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("gameplay");
        allowArmorExploits = SERVER_BUILDER
                .comment("Allow non-mob damage, such as fire or falling, to grant armor XP.")
                .define("allowArmorExploits", false);
        SERVER_BUILDER.pop();

        COMMON_CONFIG = SERVER_BUILDER.build();
    }

    private TinkerConfig() {
    }

    public static int getBaseXpForTool(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        for (String entry : toolBaseXpOverrides.get()) {
            int separator = entry.lastIndexOf('=');
            if (itemId.toString().equals(entry.substring(0, separator).trim())) {
                return Integer.parseInt(entry.substring(separator + 1).trim());
            }
        }
        return defaultBaseXP.get();
    }

    public static boolean canLevelUp(int currentLevel) {
        return maximumLevels.get() <= 0 || currentLevel < maximumLevels.get();
    }

    private static boolean isValidToolXpOverride(Object value) {
        if (!(value instanceof String entry)) {
            return false;
        }

        int separator = entry.lastIndexOf('=');
        if (separator <= 0 || separator == entry.length() - 1) {
            return false;
        }

        ResourceLocation itemId = ResourceLocation.tryParse(entry.substring(0, separator).trim());
        if (itemId == null) {
            return false;
        }

        try {
            return Integer.parseInt(entry.substring(separator + 1).trim()) > 0;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public enum SlotRewardType {
        UPGRADE,
        ABILITY
    }
}
