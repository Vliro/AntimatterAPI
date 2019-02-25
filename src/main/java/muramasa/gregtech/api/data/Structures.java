package muramasa.gregtech.api.data;

import muramasa.gregtech.api.enums.Coil;
import muramasa.gregtech.api.structure.StructureBuilder;
import muramasa.gregtech.api.structure.StructureElement;
import muramasa.gregtech.api.structure.Structure;
import muramasa.gregtech.api.structure.StructureResult;
import muramasa.gregtech.api.util.int3;
import muramasa.gregtech.common.tileentities.base.multi.TileEntityMultiMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import static muramasa.gregtech.api.data.Machines.*;
import static muramasa.gregtech.api.enums.Casing.*;
import static muramasa.gregtech.api.enums.Coil.FUSION;

public class Structures {

    /** Air Type Elements **/
    public static StructureElement X = new StructureElement("x", false); //Used to skip positions for non-cubic structures
    public static StructureElement AIR = new StructureElement("air") { //Air Block Check
        @Override
        public boolean evaluate(TileEntityMultiMachine machine, int3 pos, StructureResult result) {
            IBlockState state = machine.getWorld().getBlockState(pos.asBlockPos());
            return state.getBlock().isAir(state, machine.getWorld(), pos.asBlockPos());
        }
    };

    /** Charcoal Pit Elements **/
    public static StructureElement CP_LOG_OR_ELSE = new StructureElement("logorelse") {
        @Override
        public boolean evaluate(TileEntityMultiMachine machine, int3 pos, StructureResult result) {
            IBlockState state = machine.getWorld().getBlockState(pos.asBlockPos());
            String tool = state.getBlock().getHarvestTool(state);
            return state.getBlock().getMaterial(state) == Material.WOOD && (tool != null && tool.equals("axe"));
        }
    };

    /** Primitive/Bronze Furnace Elements **/
    public static StructureElement BF_AIR_OR_LAVA = new StructureElement("airorlava") {
        @Override
        public boolean evaluate(TileEntityMultiMachine machine, int3 pos, StructureResult result) {
            IBlockState state = machine.getWorld().getBlockState(pos.asBlockPos());
            return AIR.evaluate(machine, pos, result) || state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA;
        }
    };

    /** Electric Blast Furnace Elements **/
    public static StructureElement HATCH_OR_CASING_EBF = new StructureElement("hatchorcasingebf", HEAT_PROOF, HATCH_ITEM_INPUT, HATCH_ITEM_OUTPUT);
    public static StructureElement ANY_COIL_EBF = new StructureElement("anycoilebf", Coil.getAll().toArray(new Coil[0]));

    /** Vacuum Freezer Elements **/
    public static StructureElement VF_HATCH_OR_CASING = new StructureElement("hatchorcasingvf", FROST_PROOF, HATCH_ITEM_INPUT, HATCH_ITEM_OUTPUT, HATCH_ENERGY);

    /** Structure Patterns **/
    public static Structure STRUCTURE_PRIMITIVE_BLAST_FURNAVE = StructureBuilder.start()
        .of("CCC", "CCC", "CCC").of("CCC", "CBM", "CCC").of("CCC", "CBC", "CCC").of("CCC", "CAC", "CCC")
        .at("C", FIRE_BRICK).at("B", BF_AIR_OR_LAVA).at("M", PRIMITIVE_BLAST_FURNACE).build()
        .offset(2, -1).exact(PRIMITIVE_BLAST_FURNACE, 1).min(FIRE_BRICK, 32);

    public static Structure STRUCTURE_BRONZE_BLAST_FURNACE = StructureBuilder.start()
        .of("CCC", "CCC", "CCC").of("CCC", "CBM", "CCC").of("CCC", "CBC", "CCC").of("CCC", "CAC", "CCC")
        .at("C", BRONZE_PLATED_BRICK).at("B", BF_AIR_OR_LAVA).at("M", BRONZE_BLAST_FURNACE).build()
        .offset(2, -1).exact(BRONZE_BLAST_FURNACE, 1).min(BRONZE_PLATED_BRICK, 32);

    public static Structure STRUCTURE_BLAST_FURNACE = StructureBuilder.start()
        .of("CCC", "CCM", "CCC").of("BBB", "BAB", "BBB").of(1).of("CCC", "CCC", "CCC")
        .at("C", HATCH_OR_CASING_EBF).at("B", ANY_COIL_EBF).at("M", ELECTRIC_BLAST_FURNACE).build()
        .offset(2, 0).exact(ELECTRIC_BLAST_FURNACE, 1).min(HEAT_PROOF, 12).min(HATCH_ITEM_INPUT, 1).min(HATCH_ITEM_OUTPUT, 1);

    public static Structure STRUCTURE_VACUUM_FREEZER = StructureBuilder.start()
        .of("CCC", "CCC", "CCC").of("CCC", "CAM", "CCC").of(0)
        .at("C", VF_HATCH_OR_CASING).at("M", VACUUM_FREEZER).build()
        .offset(2, -1).exact(VACUUM_FREEZER, 1).min(FROST_PROOF, 22).min(HATCH_ITEM_INPUT, 1).min(HATCH_ITEM_OUTPUT, 1).min(HATCH_ENERGY, 1);

    public static Structure PATTERN_FUSION_REACTOR = StructureBuilder.start()
        .of(
            "XXXXXXXXXXXXXXX",
            "XXXXXXOOOXXXXXX",
            "XXXXOOXXXOOXXXX",
            "XXXOXXXXXXXOXXX",
            "XXOXXXXXXXXXOXX",
            "XXOXXXXXXXXXOXX",
            "XOXXXXXXXXXXXOX",
            "XOXXXXXXXXXXXOX",
            "XOXXXXXXXXXXXOX",
            "XXOXXXXXXXXXOXX",
            "XXOXXXXXXXXXOXX",
            "XXXOXXXXXXXOXXX",
            "XXXXOOXXXOOXXXX",
            "XXXXXXOOOXXXXXX",
            "XXXXXXXXXXXXXXX"
        )
        .of(
            "XXXXXXOOOXXXXXX",
            "XXXXOOCCCOOXXXX",
            "XXXOCCOOOCCOXXX",
            "XXOCOOXXXOOCOXX",
            "XOCOXXXXXXXOCOX",
            "XOCOXXXXXXXOCOX",
            "OCOXXXXXXXXXOCO",
            "OCMXXXXXXXXXOCO",
            "OCOXXXXXXXXXOCO",
            "XOCOXXXXXXXOCOX",
            "XOCOXXXXXXXOCOX",
            "XXOCOOXXXOOCOXX",
            "XXXOCCOOOCCOXXX",
            "XXXXOOCCCOOXXXX",
            "XXXXXXOOOXXXXXX"
        )
        .of(0)
        .at("O", FUSION_3).at("C", FUSION).at("M", FUSION_REACTOR_1).build()
        .offset(2, -1);
}