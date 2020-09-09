package muramasa.antimatter;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import muramasa.antimatter.cover.CoverInstance;
import muramasa.antimatter.dynamic.ModelConfig;
import muramasa.antimatter.machine.types.Machine;
import muramasa.antimatter.pipe.PipeSize;
import muramasa.antimatter.tile.TileEntityMachine;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelProperty;

public class AntimatterProperties {

    /** Block Dynamic Properties **/
    public static final ModelProperty<ModelConfig> DYNAMIC_CONFIG = new ModelProperty<>();

    /** Block Machine Properties **/
    public static final ModelProperty<Machine<?>> MACHINE_TYPE = new ModelProperty<>();
    public static final ModelProperty<Direction> MACHINE_FACING = new ModelProperty<>();
    public static final ModelProperty<Object2ObjectMap<Direction, CoverInstance<TileEntityMachine>>> MACHINE_COVER = new ModelProperty<>();

    /** Block Pipe Properties **/
    //public static PropertyBool PIPE_INSULATED = PropertyBool.create("insulated");
    //public static PropertyBool PIPE_RESTRICTIVE = PropertyBool.create("restrictive");
    public static final ModelProperty<PipeSize> PIPE_SIZE = new ModelProperty<>();
    public static final ModelProperty<Byte> PIPE_CONNECTIONS = new ModelProperty<>();
}
