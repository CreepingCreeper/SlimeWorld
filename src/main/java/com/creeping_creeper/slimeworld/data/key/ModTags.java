package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static slimeknights.mantle.Mantle.commonResource;


public class ModTags {
    public static class Items {
        public static final TagKey<Item> OCEAN_SLIME_BALL = common("slimeball/ocean");
        public static final TagKey<Item> GLOWSTONE_ORE = common("ores/glowstone");
        public static final TagKey<Item> RAW_BRONZE_NUGGET = common("raw_nuggets/bronze");
        public static final TagKey<Item> RAW_COPPER_NUGGET = common("raw_nuggets/copper");
        public static final TagKey<Item> RAW_IRON_NUGGET = common("raw_nuggets/iron");
        public static final TagKey<Item> RAW_GOLD_NUGGET = common("raw_nuggets/gold");
        public static final TagKey<Item> RAW_NUGGET = common("raw_nuggets");
        public static final TagKey<Item> STRIPPED_LOGS = common("stripped_logs");

        public static final TagKey<Item> SULFUR_CUBE_SWALLOWABLE = local("sulfur_cube_swallowable");
        public static final TagKey<Item> ARCHETYPE_BOUNCY = local("sulfur_cube_archetype/bouncy");
        public static final TagKey<Item> ARCHETYPE_FAST_FLAT = local("sulfur_cube_archetype/fast_flat");
        public static final TagKey<Item> ARCHETYPE_FAST_SLIDING = local("sulfur_cube_archetype/fast_sliding");
        public static final TagKey<Item> ARCHETYPE_HIGH_RESISTANCE = local("sulfur_cube_archetype/high_resistance");
        public static final TagKey<Item> ARCHETYPE_LIGHT = local("sulfur_cube_archetype/light");
        public static final TagKey<Item> ARCHETYPE_REGULAR = local("sulfur_cube_archetype/regular");
        public static final TagKey<Item> ARCHETYPE_SLOW_BOUNCY = local("sulfur_cube_archetype/slow_bouncy");
        public static final TagKey<Item> ARCHETYPE_SLOW_FLAT = local("sulfur_cube_archetype/slow_flat");
        public static final TagKey<Item> ARCHETYPE_SLOW_SLIDING = local("sulfur_cube_archetype/slow_sliding");
        public static final TagKey<Item> ARCHETYPE_STICKY = local("sulfur_cube_archetype/sticky");
        public static final TagKey<Item> ARCHETYPE_EXPLOSIVE = local("sulfur_cube_archetype/explosive");
        public static final TagKey<Item> ARCHETYPE_HOT = local("sulfur_cube_archetype/hot");
        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, SlimeWorld.getResource(name));
        }
        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, commonResource(name));
        }
    }
    public static class Blocks {
        public static final TagKey<Block> GLOWSTONE_ORE = common("ores/glowstone");
        public static final TagKey<Block> STRIPPED_LOGS = common("stripped_logs");

        public static final TagKey<Block> ANIMALS_SPAWNABLE = local("animals_spawnable");
        /** any entity types that immunize to the damage from ore berry bushes */
        public static final TagKey<Block> NECROTIC_CLONABLE = local("necrotic_clonable");
        public static final TagKey<Block> ICHOR_CAVES_REPLACEABLE = local("ichor_caves_replaceable");
        public static final TagKey<Block> ICHOR_SLIME_SPAWN = local("ichor_slime_spawn");
        public static final TagKey<Block> SULFUR_FEATURE_BASE = local("sulfur_feature_base");
        public static final TagKey<Block> TERRACUBE_SPAWN = local("terracube_spawn");
        public static final TagKey<Block> SLIMY = local("slimy");
        public static final TagKey<Block> CAUSES_CONTINUOUS_GEYSER_ERUPTIONS = local("causes_continuous_geyser_eruptions");
        public static final TagKey<Block> CAUSES_PERIODIC_GEYSER_ERUPTIONS = local("causes_periodic_geyser_eruptions");

        public static final TagKey<Block> POTENT_SULFUR = local("potent_sulfur");
        private static TagKey<Block> local(String name) {
            return TagKey.create(Registries.BLOCK, SlimeWorld.getResource(name));
        }
        private static TagKey<Block> common(String name) {return TagKey.create(Registries.BLOCK, commonResource(name));}
    }
    public static class DamageTypes {
        public static final TagKey<DamageType> SULFUR_CUBE_IMMUNE = local("sulfur_cube_with_block_immune_to");
        private static TagKey<DamageType> local(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, SlimeWorld.getResource(name));
        }
        private static TagKey<Block> common(String name) {return TagKey.create(Registries.BLOCK, commonResource(name));}
    }
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SLIME = common("slimes");
        public static final TagKey<EntityType<?>> SLIME_GOLEM = local("slime_golems");
        /** any entity types that immunize to the damage from ore berry bushes */
        public static final TagKey<EntityType<?>> ORE_BERRY_BUSHES_IMMUNE = local("ore_berry_bushes_immune");
        public static final TagKey<EntityType<?>> ANTIGRAVITY = local("antigravity");
        public static final TagKey<EntityType<?>> PLAY_SULFUR_CUBE = local("play_sulfur_cube");
        public static final TagKey<EntityType<?>> SUMMON_TOMATO_SLIME = local("summon_tomato_slime");
        private static TagKey<EntityType<?>> local(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, SlimeWorld.getResource(name));
        }
        private static TagKey<EntityType<?>> common(String name) {return TagKey.create(Registries.ENTITY_TYPE, commonResource(name));}
    }
    public static class Biomes {
        /**
         *
         */
        public static final TagKey<Biome> IS_SLIMEWORLD = local("is_slimeworld");
        public static final TagKey<Biome> ICHOR_SLIME_SPAWN = local("ichor_slime_spawn");
        public static final TagKey<Biome> TERRACUBE_SPAWN = local("terracube_spawn");
        public static final TagKey<Biome> SULFUR_CUBE_SPAWN = local("sulfur_cube_spawn");
        public static final TagKey<Biome> SKY_VARIANT_GRASS = local("sky_variant_grass");
        public static final TagKey<Biome> BLOOD_VARIANT_GRASS = local("blood_variant_grass");
        public static final TagKey<Biome> ENDER_VARIANT_GRASS = local("ender_variant_grass");

        public static final TagKey<Biome> BAKERY = structure("bakery");
        public static final TagKey<Biome> GREAT_WALL = structure("great_wall");
        public static final TagKey<Biome> GROUT = structure("grout");
        public static final TagKey<Biome> MAGIC_TOWER = structure("magic_tower");
        public static final TagKey<Biome> SMELTING_WORKSHOP = structure("smelting_workshop");
        public static final TagKey<Biome> CAT_CAFE = structure("cat_cafe");

        private static TagKey<Biome> structure(String name) {
            return TagKey.create(Registries.BIOME, SlimeWorld.getResource("has_structure/" + name));
        }
        private static TagKey<Biome> local(String name) {
            return TagKey.create(Registries.BIOME, SlimeWorld.getResource(name));
        }
    }
}
