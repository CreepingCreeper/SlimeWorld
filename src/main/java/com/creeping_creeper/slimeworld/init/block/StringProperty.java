package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StringProperty extends Property<String> {
    protected StringProperty(String name, String fluid) {
        super(name, String.class);
    }

    @Override
    public Collection<String> getPossibleValues() {
        return List.of();
    }

    @Override
    public String getName(String string) {
        return string;
    }

    public static StringProperty create(String name, String fluid, String string) {
        return new StringProperty(name, fluid);
    }

    @Override
    public Optional<String> getValue(String string) {
        return string.describeConstable();
    }
}
