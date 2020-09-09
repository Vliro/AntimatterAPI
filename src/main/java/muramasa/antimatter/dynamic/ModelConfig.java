package muramasa.antimatter.dynamic;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import muramasa.antimatter.AntimatterProperties;
import muramasa.antimatter.cover.CoverInstance;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class ModelConfig {

    protected int[] config = BlockDynamic.DEFAULT_CONFIG;
    protected int modelIndex = -1;

    public ModelConfig() {

    }

    public ModelConfig set(int[] config) {
        this.config = config;
        return this;
    }

    public int[] getConfig() {
        return config;
    }

    public boolean hasModelIndex() {
        return modelIndex != -1;
    }

    public void setModelIndex(int index) {
        modelIndex = index;
    }

    public int getModelIndex() {
        return modelIndex;
    }
    public static ImmutableMap<Integer,Direction> DIR_MAP = ImmutableMap.<Integer,Direction>builder().put(0,Direction.UP).put(1,Direction.DOWN).put(2,Direction.NORTH).put(3,Direction.SOUTH).put(4,Direction.WEST).put(5,Direction.EAST).build();

    public List<BakedQuad> getQuads(List<BakedQuad> quads, Int2ObjectOpenHashMap<IBakedModel[]> bakedConfigs, BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        IBakedModel[] baked;
        Map<Direction, CoverInstance<TileEntityMachine>> instances = (data.hasProperty(AntimatterProperties.MACHINE_COVER) ? data.getData(AntimatterProperties.MACHINE_COVER) : null);

        for (int i = 0; i < config.length; i++) {
            if (config[i] == 0 && instances != null && instances.size() == 6) {
                Direction rotated = Utils.rotateFacing(DIR_MAP.get(i), state.get(HORIZONTAL_FACING));
                CoverInstance<?> inst = instances.get(rotated);
                baked = bakedConfigs.get(rotated.getIndex()*10000 + inst.getId().hashCode());
                if (baked != null) addBaked(quads, baked, state, side, rand, data);
            } else {
                baked = bakedConfigs.get(config[i]);
                if (baked != null) {
                    addBaked(quads, baked, state, side, rand, data);
                    if (i == 0) setModelIndex(config[i]);
                }
            }
        }
        return quads;
    }

    public void addBaked(List<BakedQuad> quads, IBakedModel[] baked, BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        for (int j = 0; j < baked.length; j++) {
            quads.addAll(baked[j].getQuads(state, side, rand, data));
        }
    }

    public boolean isInvalid() {
        return config == null || config.length == 0 || config[0] == -1;
    }
}
