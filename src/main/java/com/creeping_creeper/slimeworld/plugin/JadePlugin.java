package com.creeping_creeper.slimeworld.plugin;

import com.creeping_creeper.slimeworld.init.block.DryingRackBlock;
import com.creeping_creeper.slimeworld.init.block.entity.DryingRackBlockEntity;
import com.creeping_creeper.slimeworld.init.entity.Growable;
import com.creeping_creeper.slimeworld.init.entity.SulfurCubeEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.OriginSlimeEntity;
import com.creeping_creeper.slimeworld.plugin.jade.DryingRackProvider;
import com.creeping_creeper.slimeworld.plugin.jade.GrowableProvider;
import net.minecraft.world.entity.monster.Slime;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(DryingRackProvider.INSTANCE, DryingRackBlock.class);
        registration.registerEntityComponent(GrowableProvider.INSTANCE, OriginSlimeEntity.class);
        registration.registerEntityComponent(GrowableProvider.INSTANCE, SulfurCubeEntity.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(DryingRackProvider.INSTANCE, DryingRackBlockEntity.class);
        registration.registerEntityDataProvider(GrowableProvider.INSTANCE, OriginSlimeEntity.class);
        registration.registerEntityDataProvider(GrowableProvider.INSTANCE, SulfurCubeEntity.class);

    }

}
