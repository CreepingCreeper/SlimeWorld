package com.creeping_creeper.slimeworld.plugin;

import com.creeping_creeper.slimeworld.init.block.DryingRackBlock;
import com.creeping_creeper.slimeworld.init.block.entity.DryingRackBlockEntity;
import com.creeping_creeper.slimeworld.plugin.jade.DryingRackProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(DryingRackProvider.INSTANCE, DryingRackBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(DryingRackProvider.INSTANCE, DryingRackBlockEntity.class);
    }

}
