package muramasa.antimatter.tile.single;

import muramasa.antimatter.capability.IEnergyHandler;
import muramasa.antimatter.capability.machine.MachineEnergyHandler;
import muramasa.antimatter.machine.types.Machine;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.tile.TileEntityStorage;
import tesseract.util.Dir;

public class TileEntityBatteryBuffer extends TileEntityStorage {

    public TileEntityBatteryBuffer(Machine<?> type) {
        super(type);
        energyHandler.setup((tile, tag) -> new MachineEnergyHandler<TileEntityMachine>(tile, tag, 0L, 0L, tile.getMachineTier().getVoltage(), tile.getMachineTier().getVoltage(), 0, 0) {
            @Override
            public boolean canOutput(Dir direction) {
                return tile.getFacing().getIndex() == direction.getIndex();
            }

            @Override
            public boolean connects(Dir direction) {
                return true;
            }

            @Override
            public boolean canChargeFromItem() {
                return true;
            }

            @Override
            public long getCapacity() {
                return super.getCapacity() + (cachedItems != null ? cachedItems.stream().mapToLong(IEnergyHandler::getCapacity).sum() : 0);
            }

            @Override
            public long getEnergy() {
                return super.getEnergy() + (cachedItems != null ? cachedItems.stream().mapToLong(IEnergyHandler::getEnergy).sum() : 0);
            }
        });
    }
}