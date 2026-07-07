package com.creeping_creeper.slimeworld.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecated")
public class GeyserParticleOptions implements ParticleOptions {

     public static final Deserializer<GeyserParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public @NotNull GeyserParticleOptions fromCommand(@NotNull ParticleType<GeyserParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int waterBlocks = reader.readInt();
            return new GeyserParticleOptions(type, waterBlocks);
        }

        @Override
        public @NotNull GeyserParticleOptions fromNetwork(@NotNull ParticleType<GeyserParticleOptions> type, FriendlyByteBuf buf) {
            int waterBlocks = buf.readInt();
            return new GeyserParticleOptions(type, waterBlocks);
        }
    };

    public static Codec<GeyserParticleOptions> codec(ParticleType<GeyserParticleOptions> type) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                        Codec.INT.fieldOf("water_blocks").forGetter((obj) -> obj.waterBlocks))
                .apply(instance, (waterBlocks) -> new GeyserParticleOptions(type, waterBlocks)));
    }

    private final ParticleType<GeyserParticleOptions> type;
    public final int waterBlocks;
    private BlockPos pos;

    public GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, int waterBlocks) {
        this.type = type;
        this.waterBlocks = waterBlocks;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(this.waterBlocks);
    }

    @Override
    public @NotNull String writeToString() {
        return getType() + " " + this.waterBlocks;
    }

    // 必须实现
    @Override
    public @NotNull ParticleType<GeyserParticleOptions> getType() {
        return this.type;
    }

    public GeyserParticleOptions setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }
}
