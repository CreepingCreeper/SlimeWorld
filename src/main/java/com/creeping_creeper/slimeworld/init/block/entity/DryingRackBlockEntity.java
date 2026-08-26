package com.creeping_creeper.slimeworld.init.block.entity;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModOthers;
import com.creeping_creeper.slimeworld.init.misc.DryingRackRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.recipe.container.ISingleStackContainer;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.tables.block.entity.table.RetexturedTableBlockEntity;

public class DryingRackBlockEntity extends RetexturedTableBlockEntity implements WorldlyContainer {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    private static final String TAG_TIMER = "timer";
    private static final String TAG_RECIPE = "recipe";
    private static final Component NAME = SlimeWorld.makeTranslation("gui", "drying");
    private int timer = 0;
    private int dryingTime = -1;
    private DryingRackRecipe currentRecipe;
    private ResourceLocation recipeName;
    private DryingRackRecipe lastDryingRecipe;
    private final ISingleStackContainer dryingInventory;

    public static final BlockEntityTicker<DryingRackBlockEntity> SERVER_TICKER = (level, pos, state, self) -> self.serverTick(level, pos);
    public static final BlockEntityTicker<DryingRackBlockEntity> CLIENT_TICKER = (level, pos, state, self) -> self.clientTick(level, pos);

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.DryingRackEntity.get(), pos, state, NAME, 2);
        this.dryingInventory = new DryingWrapper(this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{INPUT, OUTPUT};
    }

    public void interact(Player player, InteractionHand hand) {
        // skip client side
        if (level == null || level.isClientSide) {
            return;
        }
        // first try interacting with the table as a tank. If that fails, run normal item swap logic
        // normal item swap logic should only run if we lack a fluid though
        ItemStack held = player.getItemInHand(hand);

        ItemStack input = getItem(INPUT);
        ItemStack output = getItem(OUTPUT);

        // recipes failed, so do normal pickup
        // completely empty -> insert current item into input
        if (input.isEmpty() && output.isEmpty()) {
            if (!held.isEmpty()) {
                ItemStack stack = held.split(1);
                player.setItemInHand(hand, held.isEmpty() ? ItemStack.EMPTY : held);
                setItem(INPUT, stack);
            }
        } else {
            // stack in either slot, take one out
            // prefer output stack, as often the input is a Dry that we want to use again
            int slot = output.isEmpty() ? INPUT : OUTPUT;

            // Additional info: Only 1 item can be put into the Drying block usually, however recipes
            // can have ItemStacks with stacksize > 1 as output
            // we therefore spill the whole contents on extraction.
            ItemStack stack = getItem(slot);
            ItemHandlerHelper.giveItemToPlayer(player, stack, player.getInventory().selected);
            setItem(slot, ItemStack.EMPTY);

            // send a block update for the comparator, needs to be done after the stack is removed
            if (slot == OUTPUT) {
                level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
            }
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index == INPUT && !isStackInSlot(OUTPUT);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == OUTPUT;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        ItemStack oldStack = getItem(slot);
        super.setItem(slot, newStack);

        if (slot == INPUT && !ItemStack.isSameItem(oldStack, newStack)) {
            reset();
            findDryingRecipe();
            DryingRackRecipe match = findDryingRecipe();
            if(match != null){
                currentRecipe = match;
                dryingTime = currentRecipe.getDryingTime();
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return null;
    }


    /** Handles cooling the Drying recipe */
    private void serverTick(Level level, BlockPos pos) {
        if (currentRecipe == null) {
            return;
        }
        
        if (dryingTime >= 0) {
            timer++;
            if (timer >= dryingTime) {
                if (!currentRecipe.matches(dryingInventory, level)) {
                    // if lost our recipe or the recipe needs more fluid then we have, we are done
                    // will come around later for the proper fluid amount
                    currentRecipe = findDryingRecipe();
                    recipeName = null;
                    if (currentRecipe == null) {
                        timer = 0;
                        return;
                    }
                }

                // actual recipe result
                ItemStack output = currentRecipe.assemble(dryingInventory, level.registryAccess());
                setItem(INPUT, ItemStack.EMPTY);
                super.setItem(OUTPUT, output);
                reset();
            } 
        }
    }

    /** Handles animating the recipe */
    private void clientTick(Level level, BlockPos pos) {
        if (currentRecipe == null) {
            return;
        }
        timer++;
        if (level.random.nextFloat() < 0.02F) {
            level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, pos.getX() + level.random.nextDouble(), pos.getY() + 0.3D, pos.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    private DryingRackRecipe findDryingRecipe() {
        if (level == null) return null;
        if (this.lastDryingRecipe != null && this.lastDryingRecipe.matches(dryingInventory, level)) {
            return this.lastDryingRecipe;
        }
        DryingRackRecipe dryingRecipe = level.getRecipeManager().getRecipeFor(ModOthers.DryingRecipeType.get(), dryingInventory, level).orElse(null);
        if (dryingRecipe != null) {
            this.lastDryingRecipe = dryingRecipe;
        }
        return dryingRecipe;
    }

    private void reset() {
        timer = 0;
        currentRecipe = null;
        recipeName = null;
        dryingTime = -1;
    }

    private void loadRecipe(Level level, ResourceLocation name) {
        // if the tank is empty, ignore old recipe
        // fetch recipe by name
        RecipeHelper.getRecipe(level.getRecipeManager(), name, DryingRackRecipe.class).ifPresent(recipe -> {
            this.currentRecipe = recipe;
            dryingTime = recipe.getDryingTime();
        });
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        // if we have a recipe name, swap recipe name for recipe instance
        if (recipeName != null) {
            loadRecipe(pLevel, recipeName);
            recipeName = null;
        }
    }

    @Override
    public void saveSynced(CompoundTag tags) {
        super.saveSynced(tags);
        if (currentRecipe != null || recipeName != null) {
            tags.putInt(TAG_TIMER, timer);
        }
        if (currentRecipe != null) {
            tags.putString(TAG_RECIPE, currentRecipe.getId().toString());
        } else if (recipeName != null) {
            tags.putString(TAG_RECIPE, recipeName.toString());
        }
    }

    @SuppressWarnings("removal")
    @Override
    public void load(CompoundTag tags) {
        super.load(tags);
        timer = tags.getInt(TAG_TIMER);
        if (tags.contains(TAG_RECIPE, CompoundTag.TAG_STRING)) {
            ResourceLocation name = new ResourceLocation(tags.getString(TAG_RECIPE));
            // if we have a level, fetch the recipe
            if (level != null) {
                loadRecipe(level, name);
            } else {
                // otherwise fetch the recipe when the level is set
                recipeName = name;
            }
        }
    }

    public int getTimer(){
        return this.timer;
    }

    public int getDryingTime(){
        return this.dryingTime;
    }

    @Nullable
    public static <CAST extends DryingRackBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> check, BlockEntityType<CAST> casting) {
        return BlockEntityHelper.castTicker(check, casting, level.isClientSide ? CLIENT_TICKER : SERVER_TICKER);
    }
}
