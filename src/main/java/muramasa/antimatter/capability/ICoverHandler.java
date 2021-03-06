package muramasa.antimatter.capability;

import muramasa.antimatter.cover.Cover;
import muramasa.antimatter.cover.CoverInstance;
import muramasa.antimatter.tool.AntimatterToolType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface ICoverHandler<T extends TileEntity> {

    /** Getters/Setters **/
    boolean set(Direction side, Cover cover);

    CoverInstance<T> get(Direction side);

    CoverInstance<?>[] getAll();

    Direction getTileFacing();

    T getTile();
    //Returns a lambda that, given a direction returns the given Cover.
    default Function<Direction, CoverInstance> getCoverFunction() {
        return this::get;
    }

    /** Events **/
    void onRemove();

    void onUpdate();

    //If the player uses a cover in hand -> place cover if none exists.. Otherwises interact with the cover, if present.
    boolean onInteract(PlayerEntity player, Hand hand, Direction side, @Nullable AntimatterToolType type);

    /** Helpers **/
    boolean placeCover(PlayerEntity player, Direction side, ItemStack stack, Cover cover);

    boolean removeCover(PlayerEntity player, Direction side);

    boolean hasCover(Direction side, Cover cover);

    boolean isValid(Direction side, Cover replacement);
}
