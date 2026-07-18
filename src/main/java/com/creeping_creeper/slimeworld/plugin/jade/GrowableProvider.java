package com.creeping_creeper.slimeworld.plugin.jade;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.entity.Growable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GrowableProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = SlimeWorld.getResource("growable");

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (accessor.getServerData().contains("GrowingTime", 3)) {
            int time = accessor.getServerData().getInt("GrowingTime");
            if (time > 0) {
                tooltip.add(Component.translatable("jade.mobgrowth.time", time));
            }

        }
    }

    @Override
    public void appendServerData(CompoundTag tag, EntityAccessor accessor) {
        if (accessor.getEntity() instanceof Growable growable) {
            int time = growable.getGrowTime();
            if (time > 0) {
                tag.putInt("GrowingTime", time);
            }
        }

    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}
