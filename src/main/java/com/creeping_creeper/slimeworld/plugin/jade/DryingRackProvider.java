package com.creeping_creeper.slimeworld.plugin.jade;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.block.entity.DryingRackBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum DryingRackProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = SlimeWorld.getResource("drying_rack");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        IElementHelper helper = IElementHelper.get();
        if (data.contains("progress")) {
            int progress = data.getInt("progress");
            IElement icon = helper.item(Items.CLOCK.getDefaultInstance(), 0.5f).size(new Vec2(10, 10)).translate(new Vec2(0, -1));
            tooltip.add(icon);
            tooltip.append(Component.translatable("tooltip.slimeworld.drying_rack_progress", progress));
        }

    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        DryingRackBlockEntity dryingRack = (DryingRackBlockEntity) accessor.getBlockEntity();
        int dryingTime = dryingRack.getDryingTime();
        if (dryingTime >= 0){
            data.putInt("progress", (dryingTime - dryingRack.getTimer()) / 20);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}