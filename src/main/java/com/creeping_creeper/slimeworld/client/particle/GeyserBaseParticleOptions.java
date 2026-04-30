package com.creeping_creeper.slimeworld.client.particle;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

// 1.20.1 不支持 record，改用普通类
public class GeyserBaseParticleOptions implements ParticleOptions {

    // 1.20.1 必须的反序列化器（命令 + 网络）
    public static final Deserializer<GeyserBaseParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public GeyserBaseParticleOptions fromCommand(ParticleType<GeyserBaseParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int waterBlocks = reader.readInt();
            reader.expect(' ');
            float burstImpulseBase = reader.readFloat();
            return new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase);
        }

        @Override
        public GeyserBaseParticleOptions fromNetwork(ParticleType<GeyserBaseParticleOptions> type, FriendlyByteBuf buf) {
            int waterBlocks = buf.readInt();
            float burstImpulseBase = buf.readFloat();
            return new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase);
        }
    };

    public static Codec<GeyserBaseParticleOptions> codec(ParticleType<GeyserBaseParticleOptions> type) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                        Codec.INT.fieldOf("water_blocks").forGetter((obj) -> obj.waterBlocks),
                        Codec.FLOAT.fieldOf("water_blocks").forGetter((obj) -> obj.burstImpulseBase)
                )
                .apply(instance, (waterBlocks, burstImpulseBase) -> new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase)));
    }

    // 成员变量
    private final ParticleType<GeyserBaseParticleOptions> type;
    public final int waterBlocks;
    public final float burstImpulseBase;
    private BlockPos pos;

    // 构造器
    public GeyserBaseParticleOptions(ParticleType<GeyserBaseParticleOptions> type, int waterBlocks, float burstImpulseBase) {
        this.type = type;
        this.waterBlocks = waterBlocks;
        this.burstImpulseBase = burstImpulseBase;
    }

    // 网络写入（服务端 → 客户端）
    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(this.waterBlocks);
        buf.writeFloat(this.burstImpulseBase);
    }

    // 命令字符串
    @Override
    public String writeToString() {
        return getType() + " " + this.waterBlocks + " " + this.burstImpulseBase;
    }

    // 必须实现
    @Override
    public ParticleType<GeyserBaseParticleOptions> getType() {
        return this.type;
    }

    // 可选：位置扩展（原版风格）
    public GeyserBaseParticleOptions setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    public BlockPos getPos() {
        return pos;
    }
}
