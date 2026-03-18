package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.effect.SlimeResonanceEffect;
import com.creeping_creeper.slimeworld.init.effect.StandEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EnumDeferredRegister;
import slimeknights.mantle.registration.deferred.PotionDeferredRegister;
import slimeknights.mantle.registration.deferred.PotionDeferredRegister.PotionType;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.shared.TinkerAttributes;
import slimeknights.tconstruct.shared.TinkerEffects;

public class ModEffects {
    protected static final EnumDeferredRegister<MobEffect> MOB_EFFECTS = new EnumDeferredRegister<>(Registries.MOB_EFFECT, SlimeWorld.MODID);
    protected static final PotionDeferredRegister POTIONS = new PotionDeferredRegister(SlimeWorld.MODID);

    public static final RegistryObject<MobEffect> SlimeResonance = MOB_EFFECTS.register("slime_resonance",() -> new SlimeResonanceEffect(MobEffectCategory.NEUTRAL, 0x8CD782,true));
    public static final RegistryObject<TinkerEffect> Blessing = MOB_EFFECTS.register("blessing",() -> new TinkerEffect(MobEffectCategory.BENEFICIAL, 0xE8E5D2,true)
            .addAttributeModifier(TinkerAttributes.GOOD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170001",0.25,AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(TinkerAttributes.BAD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170002",-0.25,AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<TinkerEffect> Curse = MOB_EFFECTS.register("curse",() -> new TinkerEffect(MobEffectCategory.HARMFUL, 0xA2935E,true)
            .addAttributeModifier(TinkerAttributes.GOOD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170003",-0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(TinkerAttributes.BAD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170004",0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> Stand = MOB_EFFECTS.register("stand",() -> new StandEffect(MobEffectCategory.BENEFICIAL, 0x8CD782,true));

    public static final EnumObject<PotionType, Potion> ConductivePotion = POTIONS.registerTypes(TinkerEffects.conductive, 30 * 20, 0).withStrong().withLong().build();
    public static final EnumObject<PotionType, Potion> VenomPotion = POTIONS.registerTypes(TinkerEffects.venom, 30 * 20, 0).withStrong().withLong().build();
    public static final EnumObject<PotionType, Potion> BouncyPotion = POTIONS.registerTypes(TinkerEffects.bouncy, 60 * 20, 0).withStrong().withLong().build();
    public static final EnumObject<PotionType, Potion> BlessingPotion = POTIONS.registerTypes(Blessing).withStrong().withLong().build();
    public static final EnumObject<PotionType, Potion> CursePotion = POTIONS.registerTypes(Curse).withStrong().withLong().build();
    public static final EnumObject<PotionType, Potion> DoubleJumpPotion = POTIONS.registerTypes(TinkerEffects.doubleJump, 60 * 20, 0).withStrong().withLong().build();

    public static void init() {
        brewing(ConductivePotion, Potions.AWKWARD, ModItems.FieryFlower);
        brewing(VenomPotion, Potions.AWKWARD, ModItems.PoisonFlower);
        brewing(BouncyPotion, Potions.AWKWARD, ModItems.SpringyFlower);
        brewing(BlessingPotion, Potions.AWKWARD, ModItems.ConsecratedFlower);
        brewing(CursePotion, Potions.AWKWARD, ModItems.GraveyardFlower);
        brewing(DoubleJumpPotion, Potions.AWKWARD, ModItems.OceanCongealedSlime);
    }

    private static void brewing(EnumObject<PotionDeferredRegister.PotionType,Potion> potion, Potion base, ItemLike item) {
        Potion normal = potion.get(PotionDeferredRegister.PotionType.NORMAL);
        PotionBrewing.POTION_MIXES.add(new PotionBrewing.Mix<>(ForgeRegistries.POTIONS, base, Ingredient.of(item), normal));
        Potion longer = potion.getOrNull(PotionDeferredRegister.PotionType.LONG);
        if (longer != null) {
            PotionBrewing.addMix(normal, Items.REDSTONE, longer);
        }
        Potion strong = potion.getOrNull(PotionDeferredRegister.PotionType.STRONG);
        if (strong != null) {
            PotionBrewing.addMix(normal, Items.GLOWSTONE_DUST, strong);
        }
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(ModItems.OceanCongealedSlime), ModItems.OceanSlimeBottle.get().getDefaultInstance()));
    }

    public static void registers(IEventBus bus) {
        MOB_EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}
