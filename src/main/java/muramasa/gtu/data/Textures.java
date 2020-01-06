package muramasa.gtu.data;

import muramasa.antimatter.texture.ITextureHandler;
import muramasa.antimatter.texture.Texture;
import muramasa.antimatter.texture.TextureData;

public class Textures {

    //TODO move some of these to the API somehow

    public static final Texture ERROR = new Texture("block/machine/overlay/invalid/front");

    public static final ITextureHandler BOILER_HANDLER = (m, t) -> new Texture[] {
        new Texture("block/machine/base/" + t.getId()),
        new Texture("block/machine/base/" + t.getId()),
        new Texture("block/machine/base/bricked_" + t.getId()),
        new Texture("block/machine/base/bricked_" + t.getId()),
        new Texture("block/machine/base/bricked_" + t.getId()),
        new Texture("block/machine/base/bricked_" + t.getId()),
    };

    public static final ITextureHandler MULTI_HANDLER = (m, t) -> m.getTiers().size() > 1 ? new Texture[]{new Texture("block/machine/base/" + m.getId() + "_" + t.getId())} : new Texture[]{new Texture("block/machine/base/" + m.getId())};

    public static final Texture PIPE = new Texture("block/pipe/pipe_side");
    public static final Texture WIRE = new Texture("block/pipe/wire_side");
    public static final Texture CABLE = new Texture("block/pipe/cable_side");

    public static final Texture[] PIPE_FACE = new Texture[] {
        new Texture("block/pipe/pipe_vtiny"),
        new Texture("block/pipe/pipe_tiny"),
        new Texture("block/pipe/pipe_small"),
        new Texture("block/pipe/pipe_normal"),
        new Texture("block/pipe/pipe_large"),
        new Texture("block/pipe/pipe_huge")
    };

    public static final Texture[] CABLE_FACE = new Texture[] {
        new Texture("block/pipe/cable_vtiny"),
        new Texture("block/pipe/cable_tiny"),
        new Texture("block/pipe/cable_small"),
        new Texture("block/pipe/cable_normal"),
        new Texture("block/pipe/cable_large"),
        new Texture("block/pipe/cable_huge")
    };

    public static final Texture[] WIRE_FACE = new Texture[] {
        WIRE, WIRE, WIRE, WIRE, WIRE, WIRE
    };

    public static final TextureData[] PIPE_DATA = new TextureData[] {
        new TextureData().base(PIPE).overlay(PIPE_FACE),
        new TextureData().base(WIRE).overlay(WIRE_FACE),
        new TextureData().base(CABLE).overlay(CABLE_FACE)
    };

    public static final Texture[] LARGE_TURBINE = new Texture[] {
        new Texture("block/ct/turbine/large_turbine_0"),
        new Texture("block/ct/turbine/large_turbine_1"),
        new Texture("block/ct/turbine/large_turbine_2"),
        new Texture("block/ct/turbine/large_turbine_3"),
        new Texture("block/ct/turbine/large_turbine_4"),
        new Texture("block/ct/turbine/large_turbine_5"),
        new Texture("block/ct/turbine/large_turbine_6"),
        new Texture("block/ct/turbine/large_turbine_7"),
        new Texture("block/ct/turbine/large_turbine_8")
    };

    public static final Texture[] LARGE_TURBINE_ACTIVE = new Texture[] {
        new Texture("block/ct/turbine/large_turbine_active_0"),
        new Texture("block/ct/turbine/large_turbine_active_1"),
        new Texture("block/ct/turbine/large_turbine_active_2"),
        new Texture("block/ct/turbine/large_turbine_active_3"),
        new Texture("block/ct/turbine/large_turbine_active_4"),
        new Texture("block/ct/turbine/large_turbine_active_5"),
        new Texture("block/ct/turbine/large_turbine_active_6"),
        new Texture("block/ct/turbine/large_turbine_active_7"),
        new Texture("block/ct/turbine/large_turbine_active_8")
    };

    public static final Texture[] FUSION_1_CT = new Texture[] {
        new Texture("block/ct/fusion/fusion_1_0"),
        new Texture("block/ct/fusion/fusion_1_1"),
        new Texture("block/ct/fusion/fusion_1_2"),
        new Texture("block/ct/fusion/fusion_1_3"),
        new Texture("block/ct/fusion/fusion_1_4"),
        new Texture("block/ct/fusion/fusion_1_5"),
        new Texture("block/ct/fusion/fusion_1_6"),
        new Texture("block/ct/fusion/fusion_1_7"),
        new Texture("block/ct/fusion/fusion_1_8"),
        new Texture("block/ct/fusion/fusion_1_9"),
        new Texture("block/ct/fusion/fusion_1_10"),
        new Texture("block/ct/fusion/fusion_1_11"),
        new Texture("block/ct/fusion/fusion_1_12")
    };

    public static final Texture[] FUSION_2_CT = new Texture[] {
        new Texture("block/ct/fusion/fusion_2_0"),
        new Texture("block/ct/fusion/fusion_2_1"),
        new Texture("block/ct/fusion/fusion_2_2"),
        new Texture("block/ct/fusion/fusion_2_3"),
        new Texture("block/ct/fusion/fusion_2_4"),
        new Texture("block/ct/fusion/fusion_2_5"),
        new Texture("block/ct/fusion/fusion_2_6"),
        new Texture("block/ct/fusion/fusion_2_7"),
        new Texture("block/ct/fusion/fusion_2_8"),
        new Texture("block/ct/fusion/fusion_2_9"),
        new Texture("block/ct/fusion/fusion_2_10"),
        new Texture("block/ct/fusion/fusion_2_11"),
        new Texture("block/ct/fusion/fusion_2_12")
    };

    public static final Texture[] FUSION_3_CT = new Texture[] {
        new Texture("block/ct/fusion/fusion_3_0"),
        new Texture("block/ct/fusion/fusion_3_1"),
        new Texture("block/ct/fusion/fusion_3_2"),
        new Texture("block/ct/fusion/fusion_3_3"),
        new Texture("block/ct/fusion/fusion_3_4"),
        new Texture("block/ct/fusion/fusion_3_5"),
        new Texture("block/ct/fusion/fusion_3_6"),
        new Texture("block/ct/fusion/fusion_3_7"),
        new Texture("block/ct/fusion/fusion_3_8"),
        new Texture("block/ct/fusion/fusion_3_9"),
        new Texture("block/ct/fusion/fusion_3_10"),
        new Texture("block/ct/fusion/fusion_3_11"),
        new Texture("block/ct/fusion/fusion_3_12")
    };
}