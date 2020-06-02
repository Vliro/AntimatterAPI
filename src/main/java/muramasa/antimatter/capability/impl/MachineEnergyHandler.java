package muramasa.antimatter.capability.impl;

import muramasa.antimatter.item.IChargeable;
import muramasa.antimatter.tile.TileEntityMachine;
import net.minecraft.nbt.CompoundNBT;
import tesseract.Tesseract;
import tesseract.api.ITickingController;
import tesseract.util.Dir;

import javax.annotation.Nonnull;

public class MachineEnergyHandler extends EnergyHandler {

    protected TileEntityMachine tile;
    protected ITickingController controller;

    final int LOSS_ITEM = 2;

    public MachineEnergyHandler(TileEntityMachine tile, long energy, long capacity, int voltage_in, int voltage_out, int amperage_in, int amperage_out) {
        super(energy, capacity, voltage_in, voltage_out, amperage_in, amperage_out);
        this.tile = tile;
        Tesseract.ELECTRIC.registerNode(tile.getDimention(), tile.getPos().toLong(), this);
    }

    public MachineEnergyHandler(TileEntityMachine tile) {
        this(tile, 0, tile.getMachineTier().getVoltage() * 64L, tile.getMachineTier().getVoltage(), 0, 1, 0);
    }

    public void onRemove() {
        Tesseract.ELECTRIC.remove(tile.getDimention(), tile.getPos().toLong());
    }

    public void onUpdate() {
        if (controller != null) controller.tick();
        if (canExtract() || canInput()) {
            tile.itemHandler.map(handler -> {
                handler.getChargeableItems().forEach(item -> {
                    if (item.getItem() instanceof IChargeable) {
                        IChargeable ic = ((IChargeable)item.getItem());
                        if (canExtract()) {
                            if (ic.canInput() && ic.getInputVoltage() == this.getInputVoltage() && (ic.insert(item, getOutputVoltage(), true) == getOutputVoltage())) {
                                energy -= (ic.insert(item, getOutputVoltage(), false) + LOSS_ITEM);
                            }
                        }
                    }
                });
                return true;
            });
        }
    }

    /*public void onReset() {
        if (tile.isServerSide()) {
            TesseractAPI.removeElectric(tile.getDimention(), tile.getPos().toLong());
            TesseractAPI.registerElectricNode(tile.getDimention(), tile.getPos().toLong(), this);
        }
    }*/

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean connects(@Nonnull Dir direction) {
        return tile.getFacing().getIndex() != direction.getIndex()/* && tile.getCover(Ref.DIRECTIONS[direction.getIndex()]).isEqual(Data.COVER_EMPTY)*/;
    }

    @Override
    public void reset(ITickingController oldController, ITickingController newController) {
        if (oldController == null || (controller == oldController && newController == null) || controller != oldController)
            controller = newController;
    }

    @Override
    public boolean canOutput(@Nonnull Dir direction) {
        return false;
    }

    /** NBT **/
    // TODO: Finish
    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLong("Energy", energy);
        return tag;
    }

    public void deserialize(CompoundNBT tag) {
        energy = tag.getLong("Energy");
    }
}