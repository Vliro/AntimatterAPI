package muramasa.itech.client.model;

import muramasa.itech.ITech;
import muramasa.itech.client.model.models.ModelBase;
import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.HashMap;

public class ModelLoader implements ICustomModelLoader {

    private static HashMap<String, ModelBase> modelLookup = new HashMap<>();

    private IResourceManager resourceManager;

    public static void register(Block block, ModelBase model) {
        modelLookup.put(block.getRegistryName().getResourcePath(), model);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public boolean accepts(ResourceLocation modelLoc) {
        return modelLoc.getResourceDomain().equals(ITech.MODID) && modelLookup.containsKey(modelLoc.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLoc) throws Exception {
        ModelBase model = modelLookup.get(modelLoc.getResourcePath());
        return model != null ? model : ModelLoaderRegistry.getMissingModel();
    }
}
