package muramasa.gtu.api.tree;

import muramasa.gtu.api.GregTechAPI;
import muramasa.gtu.api.registration.IModelOverride;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class BlockLogBase extends BlockLog implements IModelOverride {

    protected ResourceLocation registryName;

    public BlockLogBase(ResourceLocation registryName) {
        this.registryName = registryName;
        setUnlocalizedName(registryName.getResourcePath());
        setRegistryName(registryName);
        setDefaultState(getDefaultState().withProperty(LOG_AXIS, EnumAxis.Y));
        GregTechAPI.register(this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(LOG_AXIS).build();
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta & 12) {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;

        switch (state.getValue(LOG_AXIS)) {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }

        return i;
    }

    @Override
    public void onModelRegistration() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(registryName.getResourceDomain() + ":" + registryName.getResourcePath()));
    }
}