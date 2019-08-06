package muramasa.gtu.api.capability.impl;

import muramasa.gtu.api.cover.Cover;
import muramasa.gtu.api.util.Utils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RotatableCoverHandler extends CoverHandler {

    public RotatableCoverHandler(TileEntity tile, Cover... covers) {
        super(tile, covers);
    }

    @Override
    public void update() {
        for (int i = 0; i < covers.length; i++) {
            if (covers[i].isEmpty()) continue;
            covers[i].onUpdate(getTile(), Utils.rotateFacingAlt(EnumFacing.VALUES[i], getTileFacing()));
        }
    }

    @Override
    public boolean set(EnumFacing side, Cover cover) {
        return super.set(Utils.rotateFacing(side, getTileFacing()), cover);
    }

    @Override
    public Cover get(EnumFacing side) {
        return super.get(Utils.rotateFacing(side, getTileFacing()));
    }
}
