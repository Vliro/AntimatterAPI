package muramasa.antimatter.registration;

import muramasa.antimatter.Ref;
import muramasa.antimatter.cover.Cover;
import muramasa.antimatter.datagen.builder.AntimatterBlockModelBuilder;
import muramasa.antimatter.datagen.providers.AntimatterBlockStateProvider;
import muramasa.antimatter.datagen.providers.AntimatterItemModelProvider;
import muramasa.antimatter.machine.Tier;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeBlock;

import java.util.Arrays;

import static com.google.common.collect.ImmutableMap.of;

public interface IModelProvider {

    default void onItemModelBuild(IItemProvider item, AntimatterItemModelProvider prov) {
        if (item instanceof IForgeBlock) prov.blockItem(item);
        else if (item instanceof ITextureProvider) prov.tex(item, ((ITextureProvider) item).getTextures());
        else prov.getBuilder(item);
    }

    default void onBlockModelBuild(Block block, AntimatterBlockStateProvider prov) {
        if (block instanceof ITextureProvider) prov.state(block, ((ITextureProvider) block).getTextures());
    }

    default void onCoverModelBuild(Cover cover, AntimatterBlockStateProvider prov) {
        if (cover != null) {
            ResourceLocation base = cover.getModel();
            for (Direction dir : Ref.DIRS) {
                AntimatterBlockModelBuilder builder = prov.getBuilder(new ResourceLocation(base.getNamespace(), base.getPath()));//.model(cover.getModel().toString(), m -> m.put("cover", cover.getTextures()[0]));
                builder.model(String.valueOf(cover.getModel()), m -> m.put("cover", cover.getTextures()[0]).put("base", Tier.LV.getBaseTexture()));
               // builder.config(dir.getIndex(), (b,l) -> l.add(b.of(cover.getModel()).tex(of("cover", cover.getTextures()[0])).rot(dir)));
                builder.toSpecial();
            }
        }
    }
}
