package muramasa.antimatter.capability.impl;
;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import muramasa.antimatter.machine.event.ContentEvent;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import tesseract.util.Dir;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class FluidTankWrapper implements IFluidHandler {

    private FluidTank[] tanks;
    private boolean dirty = false;
    private Map<Dir, ObjectSet<?>> filter = new EnumMap<>(Dir.class);

    public FluidTankWrapper(TileEntityMachine machine, int count, int capacity, ContentEvent event) {
        tanks = new FluidTank[count];
        for (int i = 0; i < count; i++) {
            tanks[i] = new FluidTank(capacity) {
                @Override
                protected void onContentsChanged() {
                    dirty = true;
                    machine.onMachineEvent(event);
                }
            };
        }

        for (Dir direction : Dir.VALUES) {
            filter.put(direction, new ObjectLinkedOpenHashSet<>(count));
        }
    }

    @Nonnull
    public FluidTank getTank(int tank) {
        return tanks[tank];
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tanks[tank].getFluid();
    }

    public void setFluidToTank(int tank, FluidStack stack) {
        tanks[tank].setFluid(stack);
    }

    @Nonnull
    public CompoundNBT writeToNBT(int tank, CompoundNBT nbt) {
        tanks[tank].writeToNBT(nbt);
        return nbt;
    }

    @Override
    public int getTankCapacity(int tank) {
        return tanks[tank].getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return tanks[tank].isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int tank = getFirstValidTank(resource.getFluid());
        return tank != -1 ? getTank(tank).fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidTank tank = findFluidInTanks(resource);
        return tank != null ? tank.drain(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    @Deprecated
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidTank tank = getFirstValidTank();
        return tank != null ? tank.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isFluidAvailable(@Nonnull Object fluid, Dir direction) {
        Set<?> filtered = filter.get(direction);
        return filtered.isEmpty() || filtered.contains(fluid);
    }

    // Fast way to find available tank for fluid
    public int getFirstValidTank(@Nonnull Object fluid) {
        int tank = -1;
        for (int i = 0; i < getTanks(); i++) {
            FluidStack stack = getFluidInTank(i);
            if (stack.isEmpty()) {
                tank = i;
            } else {
                if (stack.getFluid().equals(fluid) && getTankCapacity(i) > stack.getAmount()) {
                    return i;
                }
            }
        }
        return tank;
    }

    public int getAvailableTank(@Nonnull Dir direction) {
        Set<?> set = filter.get(direction);
        if (set.isEmpty()) {
            for (int i = 0; i < getTanks(); i++) {
                FluidStack stack = getFluidInTank(i);
                if (!stack.isEmpty()) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < getTanks(); i++) {
                FluidStack stack = getFluidInTank(i);
                if (!stack.isEmpty() && set.contains(stack.getFluid())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Nullable
    private FluidTank findFluidInTanks(FluidStack fluid) {
        for (FluidTank tank : tanks) {
            if (!tank.isEmpty() && Utils.equals(tank.getFluid(), fluid)) {
                return tank;
            }
        }
        return null;
    }

    @Nullable
    private FluidTank getFirstValidTank() {
        for (FluidTank tank : tanks) {
            if (!tank.isEmpty()) {
                return tank;
            }
        }
        return null;
    }

    @Nullable
    private FluidTank getFirstEmptyTank() {
        for (FluidTank tank : tanks) {
            if (!tank.isEmpty()) {
                return tank;
            }
        }
        return null;
    }
}
