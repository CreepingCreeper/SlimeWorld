package com.creeping_creeper.slimeworld.init.world;

//import com.google.common.collect.ImmutableList;
//import com.mojang.datafixers.util.Either;
//import com.mojang.datafixers.util.Pair;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.core.Holder;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.level.biome.*;
//
//import java.util.List;
//import java.util.function.Function;
//
//// 继承原版！
//public class CustomMultiNoiseBiomeSource extends MultiNoiseBiomeSource {
//    // ==============================================
//    // 【原版主世界同款写法】 ↓↓↓ 核心在这里！
//    // ==============================================
//    private static final SourceProvider OVERWORLD_PRESET = // 直接调用 OverworldBiomeBuilder，和原版主世界完全一样！
//            CustomMultiNoiseBiomeSource::generateOverworldBiomes;
//
//    // ==============================================
//    // 原版同款函数式接口 SourceProvider
//    // ==============================================
//    @FunctionalInterface
//    public interface SourceProvider {
//        <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> function);
//    }
//
//    // ==============================================
//    // 原版同款：调用 OverworldBiomeBuilder
//    // ==============================================
//    private static <T> Climate.ParameterList<T> generateOverworldBiomes(Function<ResourceKey<Biome>, T> valueGetter) {
//        ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> builder =
//                ImmutableList.builder();
//
//        // ✅ 这里直接用原版 OverworldBiomeBuilder 生成完整主世界群系！
//        new OverworldBiomeBuilder().addBiomes(consumer ->
//                builder.add(consumer.mapSecond(valueGetter))
//        );
//
//        return new Climate.ParameterList<>(builder.build());
//    }
//
//    // ==============================================
//    // CODEC 序列化
//    // ==============================================
//    public static final Codec<CustomMultiNoiseBiomeSource> CODEC = RecordCodecBuilder.create(inst -> inst.group(
//            Climate.ParameterList.codec(Biome.CODEC.fieldOf("biome"))
//                    .fieldOf("biomes")
//                    .forGetter(CustomMultiNoiseBiomeSource::getParameters)
//    ).apply(inst, CustomMultiNoiseBiomeSource::new));
//
//    // ==============================================
//    // 内部存储的参数表
//    // ==============================================
//    private final Climate.ParameterList<Holder<Biome>> parameters;
//
//    // ==============================================
//    // 构造方法：直接使用 OVERWORLD_PRESET
//    // ==============================================
//    public CustomMultiNoiseBiomeSource(Function<ResourceKey<Biome>, Holder<Biome>> biomeGetter) {
//        super(Either.left(OVERWORLD_PRESET.apply(biomeGetter)));
//        this.parameters = OVERWORLD_PRESET.apply(biomeGetter);
//    }
//
////    public CustomMultiNoiseBiomeSource(Climate.ParameterList<Holder<Biome>> parameters) {
////        super(Either.left(parameters));
////        this.parameters = parameters;
////    }
//
//    // ==============================================
//    // 内部获取参数
//    // ==============================================
//    public Climate.ParameterList<Holder<Biome>> getParameters() {
//        return this.parameters;
//    }
//
//    // ==============================================
//    // 必须重写
//    // ==============================================
//    @Override
//    protected Codec<? extends BiomeSource> codec() {
//        return CODEC;
//    }
//}
