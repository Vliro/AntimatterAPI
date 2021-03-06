package muramasa.antimatter.capability.energy;

import muramasa.antimatter.Ref;
import muramasa.antimatter.capability.AntimatterCaps;
import muramasa.antimatter.capability.EnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * ItemEnergyHandler represents the Antimatter Energy capability implementation for items.
 * It wraps an item and provides the ability to charge it & remove it, depending on if the item supports it.
 */
public class ItemEnergyHandler extends EnergyHandler implements ICapabilitySerializable<CompoundNBT> {
    protected ItemStack stack;

    public ItemEnergyHandler(ItemStack stack, long energy, long capacity, int voltage_in, int voltage_out, int amperage_in, int amperage_out) {
        super(energy, capacity, voltage_in, voltage_out, amperage_in, amperage_out);
        this.stack = stack;
    }

    //Override the following methods to use getTagEnergy instead
    @Override
    public boolean canInput() {
        return voltage_in > 0 /*&& getTagEnergy() != capacity*/;
    }

    @Override
    public boolean canOutput() {
        return !canModeBlock() && voltage_out > 0 /*&& getTagEnergy() >= voltage_out*/;
    }

    /**
     * Returns whether the item can block output.
     * @return false as default non-blocking, true on block discharge.
     */
    private boolean canModeBlock() {
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.getBoolean(Ref.KEY_ITEM_DISCHARGE_MODE);
    }

    @Override
    public long getEnergy() {
        return getTagEnergy();
    }

    private void setTagEnergy(long energy) {
        this.energy = energy;
        setStackEnergy(stack, energy);
    }

    private long getTagEnergy() {
        return getEnergyFromStack(stack);
    }

    /**
     * Sets the energy value of an item.
     * @param stack the stack to set the energy of.
     * @param energy the fix energy value.
     * @return energy parameter
     */
    public static long setStackEnergy(ItemStack stack, long energy) {
        stack.getOrCreateTag().putLong(Ref.KEY_ITEM_ENERGY, energy);
        return energy;
    }

    public static long getEnergyFromStack(ItemStack stack) {
        return stack.getOrCreateTag().getLong(Ref.KEY_ITEM_ENERGY);
    }

    @Override
    public long insert(long maxReceive, boolean simulate) {
        long energy = super.insert(maxReceive, simulate);
        if (!simulate) {
            setStackEnergy(stack, this.energy);
        }
        return energy;
    }

    @Override
    public long extract(long maxExtract, boolean simulate) {
        long energy = super.extract(maxExtract, simulate);
        if (!simulate) {
            setStackEnergy(stack, this.energy);
        }
        return energy;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AntimatterCaps.ENERGY_HANDLER_CAPABILITY ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong(Ref.KEY_ITEM_ENERGY, this.energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getLong(Ref.KEY_ITEM_ENERGY);
    }

}
