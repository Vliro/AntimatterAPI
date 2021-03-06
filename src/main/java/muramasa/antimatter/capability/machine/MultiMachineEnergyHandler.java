package muramasa.antimatter.capability.machine;

import muramasa.antimatter.capability.IEnergyHandler;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.tile.multi.TileEntityMultiMachine;
import net.minecraft.nbt.CompoundNBT;
import tesseract.api.gt.IGTNode;
import tesseract.util.Dir;

import java.util.Arrays;

import static muramasa.antimatter.machine.MachineFlag.GENERATOR;

public class MultiMachineEnergyHandler extends MachineEnergyHandler<TileEntityMultiMachine> {

    protected IEnergyHandler[] inputs;//= new IEnergyHandler[0];
    protected IEnergyHandler[] outputs; //= new IEnergyHandler[0];

    protected long cachedCapacity;

    public MultiMachineEnergyHandler(TileEntityMachine tile, CompoundNBT tag, long energy, long capacity, int voltage_in, int voltage_out, int amperage_in, int amperage_out) {
        super((TileEntityMultiMachine)tile, tag, energy, capacity, voltage_in, voltage_out, amperage_in, amperage_out);
    }

    public MultiMachineEnergyHandler(TileEntityMachine tile, CompoundNBT tag) {
        super((TileEntityMultiMachine)tile, tag,0,0, 0, 0,0,0);
    }

    public void onStructureBuild() {
        cacheInputs();
        cacheOutputs();

        //Amps in, amps out etc for this handler does not matter.
        //all handlers should be of same voltage.
        IEnergyHandler handler = anyHandler();
        if (handler != null) {
            this.voltage_in = handler.getInputVoltage();
            this.voltage_out = handler.getOutputVoltage();
        }
        this.cachedCapacity = super.getCapacity() + Arrays.stream(inputs).mapToLong(IGTNode::getCapacity).sum() + Arrays.stream(outputs).mapToLong(IGTNode::getCapacity).sum();
    }

    private void cacheInputs() {
        this.inputs = tile.getComponents("hatch_energy").stream().filter(t -> t.getEnergyHandler().isPresent()).map(t -> t.getEnergyHandler().get()).toArray(IEnergyHandler[]::new);
    }

    private void cacheOutputs() {
        this.outputs = tile.getComponents("hatch_dynamo").stream().filter(t -> t.getEnergyHandler().isPresent()).map(t -> t.getEnergyHandler().get()).toArray(IEnergyHandler[]::new);
    }

    private IEnergyHandler anyHandler() {
        if (inputs != null && inputs.length > 0) {
            return inputs[0];
        }
        if (outputs != null && outputs.length > 0) {
            return outputs[0];
        }
        return null;
    }

    public void invalidate() {
        this.cachedCapacity = super.getCapacity();
        this.inputs = null;
        this.outputs = null;
    }

    @Override
    public long insert(long maxReceive, boolean simulate) {
        if (outputs == null) cacheOutputs();
        long inserted = super.insert(maxReceive, simulate);
        if (inserted == 0 && outputs != null) {
            for (IEnergyHandler handler : outputs) {
                inserted = handler.insert(maxReceive, simulate);
                if (inserted > 0)
                    return inserted;
            }
        }
        return inserted;
    }

    @Override
    public long getEnergy() {
        return super.getEnergy() + Arrays.stream(inputs).mapToLong(IGTNode::getEnergy).sum() + Arrays.stream(outputs).mapToLong(IGTNode::getEnergy).sum();
    }

    @Override
    public long getCapacity() {
        return cachedCapacity;
    }

    @Override
    public boolean canOutput() {
        return false;
    }

    @Override
    public boolean canInput() {
        return false;
    }

    @Override
    public long extract(long maxReceive, boolean simulate) {
        if (inputs == null) cacheInputs();
        long extracted = super.extract(maxReceive, simulate);
        if (extracted == 0 && inputs != null) {
            for (IEnergyHandler handler : inputs) {
                extracted = handler.extract(maxReceive, simulate);
                if (extracted > 0)
                    return extracted;
            }
        }
        return extracted;
    }
}
