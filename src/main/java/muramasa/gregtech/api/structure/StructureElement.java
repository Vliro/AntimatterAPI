package muramasa.gregtech.api.structure;

import muramasa.gregtech.api.capability.GTCapabilities;
import muramasa.gregtech.api.capability.IComponent;
import muramasa.gregtech.api.util.Utils;
import muramasa.gregtech.api.util.int3;
import muramasa.gregtech.common.tileentities.base.multi.TileEntityMultiMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;

import java.util.HashMap;

public class StructureElement {

    private static HashMap<String, StructureElement> elementLookup = new HashMap<>();

    private String elementName;
    private String[] elementIds;
    private boolean addToList;

    public StructureElement(IStringSerializable elementName) {
        this(elementName.getName(), true, elementName);
    }

    public StructureElement(String elementName, IStringSerializable... elementIds) {
        this(elementName, true, elementIds);
    }

    public StructureElement(String elementName, boolean addToList, IStringSerializable... elementIds) {
        this.elementName = elementName;
        this.addToList = addToList;
        this.elementIds = new String[elementIds.length];
        for (int i = 0; i < elementIds.length; i++) {
            this.elementIds[i] = elementIds[i].getName();
            elementLookup.put(elementIds[i].getName(), this);
        }
        elementLookup.put(elementName, this);
    }

    public String getName() {
        return elementName;
    }

    public boolean shouldAddToList() {
        return addToList;
    }

    public boolean evaluate(TileEntityMultiMachine machine, int3 pos, StructureResult result) {
        TileEntity tile = Utils.getTile(machine.getWorld(), pos.asBlockPos());
        if (tile != null && tile.hasCapability(GTCapabilities.COMPONENT, null)) {
            IComponent component = tile.getCapability(GTCapabilities.COMPONENT, null);
            for (int i = 0; i < elementIds.length; i++) {
                if (elementIds[i].equals(component.getId())) {
                    result.addComponent(elementName, component);
                    return true;
                }
            }
            result.withError("Expected: '" + elementName + "' Found: '" + component.getId() + "' @" + pos);
            return false;
        }
        result.withError("No valid component found @" + pos);
        return false;
    }

    public static StructureElement get(String name) {
        return elementLookup.get(name);
    }
}