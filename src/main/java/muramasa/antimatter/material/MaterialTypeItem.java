package muramasa.antimatter.material;

import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.recipe.TagInput;
import muramasa.antimatter.util.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;

public class MaterialTypeItem<T> extends MaterialType<T> {

    public MaterialTypeItem(String id, int layers, boolean visible, int unitValue) {
        super(id, layers, visible, unitValue);
        AntimatterAPI.register(MaterialTypeItem.class, id, this);
    }

    public boolean allowItemGen(Material material) {
        return allowGen(material) && !blockType && AntimatterAPI.getReplacement(this, material) == null;
    }

    public Item get(Material material) {
        Item replacement = AntimatterAPI.getReplacement(this, material);
        if (replacement == null) {
            if (!allowItemGen(material)) Utils.onInvalidData(String.join("", "GET ERROR - DOES NOT GENERATE: T(", id, ") M(", material.getId(), ")"));
            else return AntimatterAPI.get(MaterialItem.class, id + "_" + material.getId());
        }
        return replacement;
    }

    public ItemStack get(Material material, int count) {
        if (count < 1) Utils.onInvalidData(String.join("", "GET ERROR - MAT STACK EMPTY: T(", id, ") M(", material.getId(), ")"));
        return new ItemStack(get(material), count);
    }

    public Tag<Item> getMaterialTag(Material m) {
        return Utils.getForgeItemTag(String.join("", Utils.getConventionalMaterialType(this), "/", m.getId()));
    }

    public TagInput getMaterialTag(Material m, int count) {
        return new TagInput(getMaterialTag(m),count);
    }
}
