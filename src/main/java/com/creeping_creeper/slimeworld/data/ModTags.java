package com.creeping_creeper.slimeworld.data;

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
        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, SlimeWorld.getResource(name));
        }
        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, commonResource(name));
        }
    }
    public static class Blocks {
        /** any entity types that immunize to the damage from ore berry bushes */
        public static final TagKey<Block> NecroticClonable = local("necrotic_clonable");
        public static final TagKey<Block> IchorSlimeSpawn = local("ichor_slime_spawn");
        public static final TagKey<Block> TerracubeSpawn = local("terracube_spawn");
        public static final TagKey<Block> Slimy = local("slimy");
        public static final TagKey<Block> BerryBush = local("berry_bush");
        public static final TagKey<Block> BerryBushStage3 = local("berry_bush/stage3");
        public static final TagKey<Block> BerryBushStage7 = local("berry_bush/stage7");
        private static TagKey<Block> local(String name) {
            return TagKey.create(Registries.BLOCK, SlimeWorld.getResource(name));
        }
        private static TagKey<Block> common(String name) {return TagKey.create(Registries.BLOCK, commonResource(name));}
    }
    public static class DamageTypes {

        private static TagKey<DamageType> local(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, SlimeWorld.getResource(name));
        }
        private static TagKey<Block> common(String name) {return TagKey.create(Registries.BLOCK, commonResource(name));}
    }
    public static class EntityTypes {
        /** any entity types that immunize to the damage from ore berry bushes */
        public static final TagKey<EntityType<?>> OreBerryBushesImmune = local("ore_berry_bushes_immune");
        private static TagKey<EntityType<?>> local(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, SlimeWorld.getResource(name));
        }
        private static TagKey<EntityType<?>> common(String name) {return TagKey.create(Registries.ENTITY_TYPE, commonResource(name));}
    }
    public static class Biomes {
        /**
         *
         */
        public static final TagKey<Biome> IchorSlimeSpawn = local("ichor_slime_spawn");
        public static final TagKey<Biome> TerracubeSpawn = local("terracube_spawn");
        private static TagKey<Biome> local(String name) {
            return TagKey.create(Registries.BIOME, SlimeWorld.getResource(name));
        }
    }
}
