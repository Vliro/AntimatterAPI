package muramasa.gtu.api.util;

import muramasa.gtu.Ref;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

public class Utils {

    private static DecimalFormat DECIMAL_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    private static DecimalFormatSymbols DECIMAL_SYMBOLS = DECIMAL_FORMAT.getDecimalFormatSymbols();

    static {
        DECIMAL_SYMBOLS.setGroupingSeparator(' ');
    }

    public static String getString(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem().getRegistryName() == null) {
            throw new InvalidParameterException("Cannot get recipe string for a empty item or if the registry name is null");
        }
        return stack.getItem().getRegistryName().toString();
    }

    public static String getString(FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null) {
            throw new InvalidParameterException("Cannot get recipe string for a null fluid or fluidstack");
        }
        return fluid.getUnlocalizedName();
    }

    public static String getString(ItemStack stack, FluidStack fluid) {
        return getString(stack) + getString(fluid);
    }

    /** Returns true of A is not empty, has the same Item and damage is equal to B **/
    public static boolean equals(ItemStack a, ItemStack b) {
        return a.isItemEqual(b);
    }

    /** Returns true of A has the same Fluid as B **/
    public static boolean equals(FluidStack a, FluidStack b) {
        return a.isFluidEqual(b);
    }

    /** Returns true if A equals() B and A amount >= B amount **/
    public static boolean contains(ItemStack a, ItemStack b) {
        return equals(a, b) && a.getCount() >= b.getCount();
    }

    /** Returns true if A equals() B and A amount >= B amount **/
    public static boolean contains(FluidStack a, FluidStack b) {
        return a.containsFluid(b);
    }

    /** Returns the index of an item in a list, or -1 if not found **/
    public static int contains(List<ItemStack> list, ItemStack item) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (Utils.equals(list.get(i), item)) return i;
        }
        return -1;
    }

    /** Returns the index of a fluid in a list, or -1 if not found **/
    public static int contains(List<FluidStack> list, FluidStack fluid) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (Utils.equals(list.get(i), fluid)) return i;
        }
        return -1;
    }

    /** Merges B into A, ignoring maxStackSize **/
    public static List<ItemStack> mergeItems(List<ItemStack> a, List<ItemStack> b) {
        int position, size = b.size();
        for (int i = 0; i < size; i++) {
            if (b.get(i).isEmpty()) continue;
            position = contains(a, b.get(i));
            if (position == -1) a.add(b.get(i));
            else a.get(position).grow(b.get(i).getCount());
        }
        return a;
    }

    /** Merges two Lists of FluidStacks, ignoring max amount **/
    public static List<FluidStack> mergeFluids(List<FluidStack> a, List<FluidStack> b) {
        int position, size = b.size();
        for (int i = 0; i < size; i++) {
            if (b.get(i) == null) continue;
            position = contains(a, b.get(i));
            if (position == -1) a.add(b.get(i));
            else a.get(position).amount += b.get(i).amount;
        }
        return a;
    }

    public static ItemStack ca(int amount, ItemStack toCopy) {
        ItemStack stack = toCopy.copy();
        stack.setCount(amount);
        return stack;
    }

    public static FluidStack ca(int amount, FluidStack toCopy) {
        FluidStack stack = toCopy.copy();
        stack.amount = amount;
        return stack;
    }

    public static ItemStack mul(int amount, ItemStack stack) {
        return ca(stack.getCount() * amount, stack);
    }

    public static FluidStack mul(int amount, FluidStack stack) {
        return ca(stack.amount * amount, stack);
    }

    public static boolean hasChanceTag(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(Ref.KEY_STACK_CHANCE);
    }

    public static boolean hasNoConsumeTag(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(Ref.KEY_STACK_NO_CONSUME);
    }

    public static int getChanceTag(ItemStack stack) {
        return stack.getTagCompound().getInteger(Ref.KEY_STACK_CHANCE);
    }

    public static boolean getNoConsumeTag(ItemStack stack) {
        return stack.getTagCompound().getBoolean(Ref.KEY_STACK_NO_CONSUME);
    }

    public static ItemStack addChanceTag(ItemStack stack, int chance) {
        validateNBT(stack).getTagCompound().setInteger(Ref.KEY_STACK_CHANCE, chance);
         return stack;
    }

    public static ItemStack addNoConsumeTag(ItemStack stack) {
        validateNBT(stack).getTagCompound().setBoolean(Ref.KEY_STACK_NO_CONSUME, true);
        return stack;
    }

    public static ItemStack validateNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack;
    }

    public static boolean areItemsValid(ItemStack... items) {
        if (items == null || items.length == 0) return false;
        for (int i = 0; i < items.length; i++) {
            if (items[i].isEmpty()) return false;
        }
        return true;
    }

    public static boolean areItemsValid(ItemStack[]... itemArrays) {
        for (int i = 0; i < itemArrays.length; i++) {
            if (!areItemsValid(itemArrays[i])) return false;
        }
        return true;
    }

    public static boolean areFluidsValid(FluidStack... fluids) {
        if (fluids == null || fluids.length == 0) return false;
        for (int i = 0; i < fluids.length; i++) {
            if (fluids[i] == null) return false;
        }
        return true;
    }

    public static boolean areFluidsValid(FluidStack[]... fluidArrays) {
        for (int i = 0; i < fluidArrays.length; i++) {
            if (!areFluidsValid(fluidArrays[i])) return false;
        }
        return true;
    }

    public static boolean doItemsMatchAndSizeValid(ItemStack[] a, ItemStack[] b) {
        int matchCount = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (contains(b[j], a[i])) {
                    matchCount++;
                    break;
                }
            }
        }
        return matchCount >= a.length;
    }

    public static boolean doFluidsMatchAndSizeValid(FluidStack[] a, FluidStack[] b) {
        int matchCount = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (contains(b[j], a[i])) {
                    matchCount++;
                    break;
                }
            }
        }
        return matchCount >= a.length;
    }

    @Nullable
    public static Fluid getFluidById(int id) {
        for (Map.Entry<Fluid, Integer> entry : FluidRegistry.getRegisteredFluidIDs().entrySet()) {
            if (entry.getValue() == id) return entry.getKey();
        }
        return null;
    }

    public static int getIdByFluid(Fluid fluid) {
        return FluidRegistry.getRegisteredFluidIDs().get(fluid);
    }

    public static String formatNumber(long number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static void spawnItems(World world, BlockPos pos, @Nullable EnumFacing side, ItemStack stack) {
        if (side != null) pos.offset(side);
        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    /** Safe version of world.getTileEntity **/
    @Nullable
    public static TileEntity getTile(IBlockAccess blockAccess, BlockPos pos) {
        if (pos == null || blockAccess == null) return null;
        if (blockAccess instanceof ChunkCache) {
            return ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        } else {
            return blockAccess.getTileEntity(pos);
        }
    }

    public static World getClientWorld() {
        return Ref.MC.world;
    }

    /** Gets the Server version of the client world **/
    public static World getServerWorld() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(getClientWorld().provider.getDimension());
    }

    /** Syncs NBT between Client & Server **/
    public static void markTileForNBTSync(TileEntity tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 3);
    }

    /** Sends block update to clients **/
    public static void markTileForRenderUpdate(TileEntity tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 2);
    }

    public static EnumFacing rotateFacing(EnumFacing toRotate, EnumFacing rotateBy) {
        if (toRotate.getAxis() == EnumFacing.Axis.Y || rotateBy.getAxis() == EnumFacing.Axis.Y) return toRotate;
        /** S-W-N-E **/
        if (rotateBy.getHorizontalIndex() < EnumFacing.NORTH.getHorizontalIndex()) {
            //Rotate CCW
            int dif = rotateBy.getHorizontalIndex() - EnumFacing.NORTH.getHorizontalIndex();
//            System.out.println("Difccw: " + dif);
            for (int i = 0; i < Math.abs(dif); i++) {
                toRotate = toRotate.rotateYCCW();
            }
        } else {
            //Rotate CW
            int dif = EnumFacing.NORTH.getHorizontalIndex() - rotateBy.getHorizontalIndex();
//            System.out.println("Difcw: " + dif);
            for (int i = 0; i < Math.abs(dif); i++) {
                toRotate = toRotate.rotateY();
            }
        }
//        System.out.println("to: " + toRotate + " - by: " + rotateBy + " - res: " + toRotate);
        return rotateBy == EnumFacing.EAST || rotateBy == EnumFacing.WEST ? toRotate.getOpposite() : toRotate;
    }

    //TODO replace with doRaytrace in block?
    //TODO optimize...
    public static EnumFacing getInteractSide(EnumFacing side, float x, float y, float z) {
        EnumFacing backSide = side.getOpposite();
        switch (side.getIndex()) {
            case 0:
            case 1:
                if (x < 0.25) {
                    if (z < 0.25) return backSide;
                    if (z > 0.75) return backSide;
                    return EnumFacing.WEST;
                }
                if (x > 0.75) {
                    if (z < 0.25) return backSide;
                    if (z > 0.75) return backSide;
                    return EnumFacing.EAST;
                }
                if (z < 0.25) return EnumFacing.NORTH;
                if (z > 0.75) return EnumFacing.SOUTH;
                return side;
            case 2:
            case 3:
                if (x < 0.25) {
                    if (y < 0.25) return backSide;
                    if (y > 0.75) return backSide;
                    return EnumFacing.WEST;
                }
                if (x > 0.75) {
                    if (y < 0.25) return backSide;
                    if (y > 0.75) return backSide;
                    return EnumFacing.EAST;
                }
                if (y < 0.25) return EnumFacing.DOWN;
                if (y > 0.75) return EnumFacing.UP;
                return side;
            case 4:
            case 5:
                if (z < 0.25) {
                    if (y < 0.25) return backSide;
                    if (y > 0.75) return backSide;
                    return EnumFacing.NORTH;
                }
                if (z > 0.75) {
                    if (y < 0.25) return backSide;
                    if (y > 0.75) return backSide;
                    return EnumFacing.SOUTH;
                }
                if (y < 0.25) return EnumFacing.DOWN;
                if (y > 0.75) return EnumFacing.UP;
                return side;
        }
        return side;
    }

    public static Set<BlockPos> getCubicPosArea(int3 area, EnumFacing side, BlockPos origin, EntityPlayer player, boolean excludeAir) {
        int xRadius, yRadius, zRadius;
        BlockPos center;

        if (side == null) {
            center = origin;
            xRadius = area.x;
            yRadius = area.y;
            zRadius = area.z;
        } else {
            center = origin.offset(side.getOpposite(), area.z);
            if (side.getAxis() == EnumFacing.Axis.Y) {
                xRadius = player.getHorizontalFacing().getAxis() == EnumFacing.Axis.X ? area.y : area.x;
                yRadius = area.z;
                zRadius = player.getHorizontalFacing().getAxis() == EnumFacing.Axis.Z ? area.y : area.x;
            } else {
                xRadius = player.getHorizontalFacing().getAxis() == EnumFacing.Axis.X ? area.z : area.x;
                yRadius = area.y;
                zRadius = player.getHorizontalFacing().getAxis() == EnumFacing.Axis.Z ? area.z : area.x;
            }
        }

        Set<BlockPos> set = new HashSet<>();
        IBlockState state;
        for (int x = center.getX() - xRadius; x <= center.getX() + xRadius; x++) {
            for (int y = center.getY() - yRadius; y <= center.getY() + yRadius; y++) {
                for (int z = center.getZ() - zRadius; z <= center.getZ() + zRadius; z++) {
                    BlockPos harvestPos = new BlockPos(x, y, z);
                    if (harvestPos.equals(origin)) continue;
                    if (excludeAir) {
                        state = player.world.getBlockState(harvestPos);
                        if (state.getBlock().isAir(state, player.world, harvestPos)) continue;
                    }
                    set.add(new BlockPos(x, y, z));
                }
            }
        }
        return set;
    }

    //Credit: from Tinkers' Construct
    public static void breakBlock(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player) {
        Block block = state.getBlock();
        if (player.capabilities.isCreativeMode) {
            block.onBlockHarvested(world, pos, state, player);
            if (block.removedByPlayer(state, world, pos, player, false)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            if (!world.isRemote) {
                ((EntityPlayerMP)player).connection.sendPacket(new SPacketBlockChange(world, pos));
            }
            return;
        }
        stack.onBlockDestroyed(world, state, pos, player);

        if (!world.isRemote) { // server sided handling
            int xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
            if (xp == -1) return;//event cancelled


            // serverside we reproduce ItemInWorldManager.tryHarvestBlock

            // ItemInWorldManager.removeBlock
            block.onBlockHarvested(world, pos, state, player);

            if (block.removedByPlayer(state, world, pos, player, true)){
                block.onBlockDestroyedByPlayer(world, pos, state);
                block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                block.dropXpOnBlockBreak(world, pos, xp);
            }

            EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
            mpPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));
        } else { // client sided handling
            // PlayerControllerMP.onPlayerDestroyBlock
            world.playBroadcastSound(2001, pos, Block.getStateId(state));
            if (block.removedByPlayer(state, world, pos, player, true)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            stack.onBlockDestroyed(world, state, pos, player);

            Ref.MC.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Ref.MC.objectMouseOver.sideHit));
        }
    }
}