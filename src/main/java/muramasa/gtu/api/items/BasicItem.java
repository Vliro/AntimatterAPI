package muramasa.gtu.api.items;

import muramasa.gtu.Configs;
import muramasa.gtu.Ref;
import muramasa.gtu.api.GregTechAPI;
import muramasa.gtu.api.blocks.BlockCoil;
import muramasa.gtu.api.blocks.BlockStone;
import muramasa.gtu.api.blocks.BlockStorage;
import muramasa.gtu.api.data.Machines;
import muramasa.gtu.api.machines.MachineFlag;
import muramasa.gtu.api.materials.MaterialType;
import muramasa.gtu.api.ore.BlockOre;
import muramasa.gtu.api.registration.IGregTechObject;
import muramasa.gtu.api.registration.IModelProvider;
import muramasa.gtu.api.texture.Texture;
import muramasa.gtu.api.util.Utils;
import muramasa.gtu.common.Data;
import muramasa.gtu.data.providers.GregTechItemModelProvider;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicItem extends Item implements IGregTechObject, IModelProvider {

    protected String id, tooltip = "";
    protected boolean enabled = true;
    protected Set<ItemTag> tags = new HashSet<>();

    public BasicItem(String id, Item.Properties properties) {
        super(properties);
        this.id = id;
        setRegistryName(getId());
        GregTechAPI.register(BasicItem.class, this);
    }

    public BasicItem(String id) {
        this(id, new Item.Properties().group(Ref.TAB_ITEMS));
    }

    public BasicItem(String id, String tooltip, Item.Properties properties) {
        this(id, properties);
        this.tooltip = tooltip;
    }

    public BasicItem(String id, String tooltip) {
        this(id, tooltip, new Item.Properties());
    }

    public BasicItem tags(ItemTag... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTooltip() {
        return tooltip;
    }

    public boolean isEnabled() {
        return enabled || Configs.DATA.ENABLE_ALL_MATERIAL_ITEMS;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("item." + getId());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new StringTextComponent(this.tooltip));
        if (Data.DebugScanner.equals(this)) {
            tooltip.add(new StringTextComponent("Blocks: " + GregTechAPI.all(Block.class).size()));
            tooltip.add(new StringTextComponent("Machines: " + Machines.getTypes(MachineFlag.BASIC, MachineFlag.MULTI, MachineFlag.HATCH).size()));
            tooltip.add(new StringTextComponent("Pipes: " + /*GregTechAPI.all(BlockPipe.class).size()*/ "TODO"));
            tooltip.add(new StringTextComponent("Casings: " + /*GregTechAPI.all(BlockCasing.class).size()*/ "TODO"));
            tooltip.add(new StringTextComponent("Coils: " + GregTechAPI.all(BlockCoil.class).size()));
            tooltip.add(new StringTextComponent("Storage: " + GregTechAPI.all(BlockStorage.class).size()));
            tooltip.add(new StringTextComponent("Ores: " + GregTechAPI.all(BlockOre.class).size()));
            tooltip.add(new StringTextComponent("Stones: " + GregTechAPI.all(BlockStone.class).size()));
            tooltip.add(new StringTextComponent("Data:"));
            tooltip.add(new StringTextComponent("Ore Materials: " + MaterialType.ORE.all().size()));
            tooltip.add(new StringTextComponent("Small Ore Materials: " + MaterialType.ORE_SMALL.all().size()));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }

//    @Override
//    public EnumActionResult onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
//        ItemStack stack = player.getHeldItem(hand);
//        TileEntity tile = Utils.getTile(world, pos);
//        if (tile != null) {
//            if (Data.DebugScanner.isEqual(stack)) {
//                if (tile instanceof TileEntityMachine) {
//                    if (tile instanceof TileEntityMultiMachine) {
//                        if (!world.isRemote) {
//                            if (!((TileEntityMultiMachine) tile).isStructureValid()) {
//                                ((TileEntityMultiMachine) tile).checkStructure();
//                            }
//                        }
//                        ((TileEntityMultiMachine) tile).checkRecipe();
//                    } else if (tile instanceof TileEntityHatch) {
////                        MachineFluidHandler handler = ((TileEntityHatch) tile).getFluidHandler();
////                        if (handler != null) {
////                            System.out.println(handler.toString());
////                        }
//                    } /*else if (tile instanceof TileEntityItemFluidMachine) {
//                        MachineFluidHandler fluidHandler = ((TileEntityItemFluidMachine) tile).getFluidHandler();
//                        for (FluidStack fluid : fluidHandler.getInputs()) {
//                            System.out.println(fluid.getLocalizedName() + " - " + fluid.amount);
//                        }
//                        tile.markDirty();
//                    }*/
//                } else if (tile instanceof TileEntityPipe) {
//                    player.sendMessage(new StringTextComponent("C: " + ((TileEntityPipe) tile).getConnections() + (((TileEntityPipe) tile).getConnections() > 63 ? " (Culled)" : " (Non Culled)")));
//                } else if (tile instanceof TileEntityMaterial) {
//                    if (!world.isRemote) {
//                        TileEntityMaterial ore = (TileEntityMaterial) tile;
//                        player.sendMessage(new StringTextComponent(ore.getMaterial().getId()));
//                    }
//                }
//            }
//        } else {
//            if (Data.DebugScanner.isEqual(stack)) {
//                BlockState state = world.getBlockState(pos);
//                if (state.getBlock() instanceof BlockTurbineCasing) {
//                    BlockState casingState = state.getBlock().getExtendedState(state, world, pos);
//                    if (casingState instanceof IExtendedBlockState) {
//                        IExtendedBlockState exState = (IExtendedBlockState) casingState;
//                        try {
//                            int[] ct = exState.getValue(BlockTurbineCasing.CONFIG);
//                            player.sendMessage(new StringTextComponent("ct: " + Arrays.toString(ct)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else if (state.getBlock() instanceof BlockDynamic) {
//                    BlockState ctState = state.getBlock().getExtendedState(state, world, pos);
//                    if (ctState instanceof IExtendedBlockState) {
//                        IExtendedBlockState exState = (IExtendedBlockState) ctState;
//                        try {
//                            int[] ct = exState.getValue(BlockDynamic.CONFIG);
//                            player.sendMessage(new StringTextComponent("ct: " + Arrays.toString(ct)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                //if (!world.isRemote) {
//                    //Data.RUBBER_SAPLING.generateTree(world, pos, Ref.RNG);
//                    //RecipeMap.dumpHashCollisions();
//                //}
//            }
//        }
//        return EnumActionResult.FAIL; //TODO FAIL?
//    }

//    public ItemType required(String... mods) {
//        for (int i = 0; i < mods.length; i++) {
//            if (!Utils.isModLoaded(mods[i])) {
//                enabled = false;
//                break;
//            }
//        }
//        return this;
//    }
//
//    public ItemType optional(String... mods) {
//        enabled = false;
//        for (int i = 0; i < mods.length; i++) {
//            if (Utils.isModLoaded(mods[i])) {
//                enabled = true;
//                break;
//            }
//        }
//        return this;
//    }

    public boolean isEqual(ItemStack stack) {
        return stack.getItem() == this;
    }

    public static boolean doesShowExtendedHighlight(ItemStack stack) {
        return GregTechAPI.getCoverFromCatalyst(stack) != null;
    }

    public ItemStack get(int count) {
        //TODO replace consumeTag with flag system
        if (count == 0) return Utils.addNoConsumeTag(new ItemStack(this, 1));
        return new ItemStack(this, count);
    }

    public Texture getTexture() {
        return new Texture("item/standard/" + id);
    }

    @Override
    public ItemStack asItemStack() {
        return get(1);
    }

    @Override
    public void onItemModelBuild(GregTechItemModelProvider provider) {
        provider.single(this, getTexture());
    }
}
