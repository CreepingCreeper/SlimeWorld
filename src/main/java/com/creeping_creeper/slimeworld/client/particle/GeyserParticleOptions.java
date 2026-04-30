package com.creeping_creeper.slimeworld.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.serialization.Codec;

// 1.20.1 不能用 record，必须用普通类
public class GeyserParticleOptions implements ParticleOptions {

    // 1.20.1 必须的反序列化器（命令 + 网络）
    public static final Deserializer<GeyserParticleOptions> DESERIALIZER = new Deserializer<>() {
        // 命令解析 /particle 指令用
        @Override
        public GeyserParticleOptions fromCommand(ParticleType<GeyserParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int waterBlocks = reader.readInt(); // 读取整数
            return new GeyserParticleOptions(type, waterBlocks);
        }

        // 网络发包读取（客户端接收）
        @Override
        public GeyserParticleOptions fromNetwork(ParticleType<GeyserParticleOptions> type, FriendlyByteBuf buf) {
            int waterBlocks = buf.readInt();
            return new GeyserParticleOptions(type, waterBlocks);
        }
    };

    // 1.20.1 数据包/持久化 codec

    public static Codec<GeyserParticleOptions> codec(ParticleType<GeyserParticleOptions> type) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                        Codec.INT.fieldOf("water_blocks").forGetter((obj) -> obj.waterBlocks))
                .apply(instance, (waterBlocks) -> new GeyserParticleOptions(type, waterBlocks)));
    }

    // 成员变量
    private final ParticleType<GeyserParticleOptions> type;
    public final int waterBlocks;
    @Getter
    private BlockPos pos; // 可选，原版常用扩展字段

    // 构造器
    public GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, int waterBlocks) {
        this.type = type;
        this.waterBlocks = waterBlocks;
    }

    // 1.20.1 必须：写入网络数据（服务端→客户端）
    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(this.waterBlocks);
    }

    // 1.20.1 必须：转字符串（/particle 指令显示用）
    @Override
    public String writeToString() {
        return getType() + " " + this.waterBlocks;
    }

    // 必须实现
    @Override
    public ParticleType<GeyserParticleOptions> getType() {
        return this.type;
    }

    // 可选：位置扩展（和原版保持一致）
    public GeyserParticleOptions setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }
}
