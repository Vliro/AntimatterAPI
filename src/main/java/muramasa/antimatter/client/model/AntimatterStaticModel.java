package muramasa.antimatter.client.model;

import com.mojang.datafixers.util.Pair;
import muramasa.antimatter.client.ModelUtils;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class AntimatterStaticModel extends AntimatterModel {

    boolean resolvedTextures = false;
    boolean baked = false;

    public AntimatterStaticModel(IUnbakedModel model, int... rotations) {
        this.model = model;
        this.rotations = rotations;
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> getter, Set<Pair<String, String>> errors) {
        if (model != null && !resolvedTextures) {
            resolvedTextures = true;
            return model.getTextures(getter, errors);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public IBakedModel bakeModel(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> getter, IModelTransform transform, ItemOverrideList overrides, ResourceLocation loc) {
        if (baked) {
            return  ModelUtils.getMissingModel().bakeModel(bakery, getter, transform, loc);
        } else {
            baked = true;
            return super.bakeModel(owner,bakery,getter,transform,overrides,loc);
        }
    }
}
