package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.library.ModBrewingRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.shared.TinkerEffects;

public class ModPotions {
    protected static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, SlimeWorld.MODID);

    public static final RegistryObject<Potion> ConductivePotion = POTIONS.register("conductive_potion", () -> new Potion(new MobEffectInstance(TinkerEffects.conductive.get(), 600, 0)));
    public static final RegistryObject<Potion> ConductivePotionStrong = POTIONS.register("conductive_potion_strong", () -> new Potion(new MobEffectInstance(TinkerEffects.conductive.get(), 300, 1)));
    public static final RegistryObject<Potion> ConductivePotionLong = POTIONS.register("conductive_potion_long", () -> new Potion(new MobEffectInstance(TinkerEffects.conductive.get(), 1200, 0)));
    public static final RegistryObject<Potion> VenomPotion = POTIONS.register("venom_potion", () -> new Potion(new MobEffectInstance(TinkerEffects.venom.get(), 600, 0)));
    public static final RegistryObject<Potion> VenomPotionStrong = POTIONS.register("venom_potion_strong", () -> new Potion(new MobEffectInstance(TinkerEffects.venom.get(), 300, 1)));
    public static final RegistryObject<Potion> VenomPotionLong = POTIONS.register("venom_potion_long", () -> new Potion(new MobEffectInstance(TinkerEffects.venom.get(), 1200, 0)));
    public static final RegistryObject<Potion> BouncyPotion = POTIONS.register("bouncy_potion", () -> new Potion(new MobEffectInstance(TinkerEffects.bouncy.get(), 1200, 0)));
    public static final RegistryObject<Potion> BouncyPotionStrong = POTIONS.register("bouncy_potion_strong", () -> new Potion(new MobEffectInstance(TinkerEffects.bouncy.get(), 600, 1)));
    public static final RegistryObject<Potion> BouncyPotionLong = POTIONS.register("bouncy_potion_long", () -> new Potion(new MobEffectInstance(TinkerEffects.bouncy.get(), 2400, 0)));
    public static final RegistryObject<Potion> BlessingPotion = POTIONS.register("blessing_potion", () -> new Potion(new MobEffectInstance(ModEffects.Blessing.get(), 3600, 0)));
    public static final RegistryObject<Potion> BlessingPotionStrong = POTIONS.register("blessing_potion_strong", () -> new Potion(new MobEffectInstance(ModEffects.Blessing.get(), 1800, 1)));
    public static final RegistryObject<Potion> BlessingPotionLong = POTIONS.register("blessing_potion_long", () -> new Potion(new MobEffectInstance(ModEffects.Blessing.get(), 9600, 0)));
    public static final RegistryObject<Potion> CursePotion = POTIONS.register("curse_potion", () -> new Potion(new MobEffectInstance(ModEffects.Curse.get(), 3600, 0)));
    public static final RegistryObject<Potion> CursePotionStrong = POTIONS.register("curse_potion_strong", () -> new Potion(new MobEffectInstance(ModEffects.Curse.get(), 1800, 1)));
    public static final RegistryObject<Potion> CursePotionLong = POTIONS.register("curse_potion_long", () -> new Potion(new MobEffectInstance(ModEffects.Curse.get(), 9600, 0)));
    public static final RegistryObject<Potion> DoubleJumpPotion = POTIONS.register("double_jump_potion", () -> new Potion(new MobEffectInstance(TinkerEffects.doubleJump.get(), 1200, 0)));
    public static final RegistryObject<Potion> DoubleJumpPotionStrong = POTIONS.register("double_jump_potion_strong", () -> new Potion(new MobEffectInstance(TinkerEffects.doubleJump.get(), 600, 1)));
    public static final RegistryObject<Potion> DoubleJumpPotionLong = POTIONS.register("double_jump_potion_long", () -> new Potion(new MobEffectInstance(TinkerEffects.doubleJump.get(), 2400, 0)));

    public static void init() {
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.ConductivePotion.get(), ModItems.FieryFlower.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.ConductivePotion.get()), ModPotions.ConductivePotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.ConductivePotion.get()), ModPotions.ConductivePotionLong.get(), Items.REDSTONE);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.VenomPotion.get(), ModItems.PotionFlower.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.VenomPotion.get()), ModPotions.VenomPotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.VenomPotion.get()), ModPotions.VenomPotionLong.get(), Items.REDSTONE);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.BouncyPotion.get(), ModItems.SpringyFlower.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.BouncyPotion.get()), ModPotions.BouncyPotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.BouncyPotion.get()), ModPotions.BouncyPotionLong.get(), Items.REDSTONE);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.BlessingPotion.get(), ModItems.ConsecratedFlower.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.BlessingPotion.get()), ModPotions.BlessingPotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.BlessingPotion.get()), ModPotions.BlessingPotionLong.get(), Items.REDSTONE);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.CursePotion.get(), ModItems.GraveyardFlower.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.CursePotion.get()), ModPotions.CursePotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.CursePotion.get()), ModPotions.CursePotionLong.get(), Items.REDSTONE);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD), ModPotions.DoubleJumpPotion.get(), ModItems.OceanCongealedSlime.asItem());
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DoubleJumpPotion.get()), ModPotions.DoubleJumpPotionStrong.get(), Items.GLOWSTONE_DUST);
        potionBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DoubleJumpPotion.get()), ModPotions.DoubleJumpPotionLong.get(), Items.REDSTONE);
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(new ItemStack(Items.GLASS_BOTTLE), Ingredient.of(ModItems.OceanSlimeBottle), new ItemStack(ModItems.OceanSlimeBottle.get())));
    }

    private static void potionBrewing(ItemStack inputPot, Potion pot, Item item) {
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(inputPot, Ingredient.of(item), PotionUtils.setPotion(new ItemStack(Items.POTION), pot)));
    }

    public static void registers(IEventBus bus) {
        POTIONS.register(bus);
    }
}
