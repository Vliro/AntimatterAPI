package muramasa.itech.client.model.models;

import muramasa.itech.ITech;
import muramasa.itech.api.enums.MachineFlag;
import muramasa.itech.api.enums.CoverType;
import muramasa.itech.api.machines.Machine;
import muramasa.itech.api.machines.MachineStack;
import muramasa.itech.client.model.bakedmodels.BakedModelBase;
import muramasa.itech.client.model.bakedmodels.BakedModelBaseMulti;
import muramasa.itech.client.model.bakedmodels.BakedModelMachine;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

public class ModelMachine extends ModelBase {

    private static final ModelResourceLocation MACHINE_BASE = new ModelResourceLocation(ITech.MODID + ":machineparts/machinebase");
    private static final ModelResourceLocation MACHINE_BASE_ITEM = new ModelResourceLocation(ITech.MODID + ":machineparts/machinebaseitem");

    private static final HashMap<String, ResourceLocation> overlayTextures = new HashMap<>(), coverTextures = new HashMap<>();

    public static IBakedModel baseBaked;

    static {
        for (Machine type : MachineFlag.BASIC.getTypes()) {
            overlayTextures.put(type.getName(), type.getOverlayTexture());
        }
        for (CoverType coverType : CoverType.values()) {
            coverTextures.put(coverType.getName(), new ResourceLocation(ITech.MODID, "blocks/machines/covers/" + coverType.getName()));
        }
    }

    public ModelMachine() {
        super("ModelMachine", baseTextures.values(), overlayTextures.values(), coverTextures.values());
    }

    @Override
    public IBakedModel bakeModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IModel baseModel = load(MACHINE_BASE), baseModelItem = load(MACHINE_BASE_ITEM);
        baseBaked = baseModel.bake(state, format, bakedTextureGetter);

        HashMap<String, IBakedModel[]> bakedModels = new HashMap<>();
        HashMap<String, IBakedModel> bakedModelsItem = new HashMap<>();
        Collection<MachineStack> machineStacks = MachineFlag.BASIC.getStacks();
        for (MachineStack stack : machineStacks) {
            String tier = stack.getTier().getName(), type = stack.getType().getName();
            IModel overlayModel = load(stack.getType().getOverlayModel());
            bakedModels.put(type + tier, new IBakedModel[] {
                new BakedModelBaseMulti(texAndBake(baseModel, "base", SOUTH, baseTextures.get(tier)), texAndBake(overlayModel, "0", SOUTH, baseTextures.get(tier))),
                new BakedModelBaseMulti(texAndBake(baseModel, "base", NORTH, baseTextures.get(tier)), texAndBake(overlayModel, "0", NORTH, baseTextures.get(tier))),
                new BakedModelBaseMulti(texAndBake(baseModel, "base", EAST, baseTextures.get(tier)), texAndBake(overlayModel, "0", EAST, baseTextures.get(tier))),
                new BakedModelBaseMulti(texAndBake(baseModel, "base", WEST, baseTextures.get(tier)), texAndBake(overlayModel, "0", WEST, baseTextures.get(tier))),
            });
            bakedModelsItem.put(stack.getType().getName() + stack.getTier().getName(), new BakedModelBase(
                texAndBake(baseModelItem, new String[]{"base", "overlay"}, new ResourceLocation[]{baseTextures.get(tier), overlayTextures.get(type)}))
            );
        }

        IBakedModel[][] bakedCovers = new IBakedModel[CoverType.values().length][5];
        for (CoverType coverType : CoverType.values()) {
            if (coverType.getModelLocation() == null) continue;
            IModel coverModel = load(coverType.getModelLocation());
            bakedCovers[coverType.ordinal()] = new IBakedModel[] {
                texAndBake(coverModel, "base", SOUTH, coverTextures.get(coverType.getName())),
                texAndBake(coverModel, "base", EAST, coverTextures.get(coverType.getName())),
                texAndBake(coverModel, "base", WEST, coverTextures.get(coverType.getName())),
                texAndBake(coverModel, "base", DOWN, coverTextures.get(coverType.getName())),
                texAndBake(coverModel, "base", UP, coverTextures.get(coverType.getName())),
            };
        }

        return new BakedModelMachine(bakedModels, bakedModelsItem, bakedCovers);
    }
}
