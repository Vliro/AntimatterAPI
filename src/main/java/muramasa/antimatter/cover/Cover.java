package muramasa.antimatter.cover;

import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.Data;
import muramasa.antimatter.Ref;
import muramasa.antimatter.gui.GuiData;
import muramasa.antimatter.gui.container.AntimatterContainer;
import muramasa.antimatter.machine.event.IMachineEvent;
import muramasa.antimatter.registration.IAntimatterObject;
import muramasa.antimatter.registration.IModelProvider;
import muramasa.antimatter.registration.ITextureProvider;
import muramasa.antimatter.texture.Texture;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.tool.AntimatterToolType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

import static muramasa.antimatter.Data.COVERNONE;
import static muramasa.antimatter.Data.COVEROUTPUT;

//The base Cover class. All cover classes extend from this.
public abstract class Cover implements IAntimatterObject, ITextureProvider, IModelProvider {

    protected GuiData gui;
    @Nullable
    private Item item;

    public Cover() {
        this.gui = new GuiData(this, Data.COVER_MENU_HANDLER);
        gui.setEnablePlayerSlots(true);
    }

    protected String getRenderId() {
        return getId();
    }

    public void setGui(GuiData setGui) {
        this.gui = setGui;
    }

    public GuiData getGui() {
        return gui;
    }

    public abstract String getId();
    public ItemStack getDroppedStack() {
        return item == null ? ItemStack.EMPTY : new ItemStack(getItem(), 1);
    }

    /**
     * Fires once per Side. Return defines whether or not to consume the interaction.
     **/
    public boolean onInteract(CoverInstance<?> instance, PlayerEntity player, Hand hand, Direction side, @Nullable AntimatterToolType type) {
        //Do not consume behaviour per default.
        return false;
    }

    public void onPlace(CoverInstance<?> instance, Direction side) {

    }

    public void onRemove(CoverInstance<?> instance, Direction side) {

    }

    //Called on update of the world.
    public void onUpdate(CoverInstance<?> instance, Direction side) {

    }

    public void onMachineEvent(CoverInstance<?> instance, TileEntityMachine tile, IMachineEvent event) {
        //NOOP
    }

    public boolean openGui(CoverInstance<?> instance, PlayerEntity player, Direction side) {
        NetworkHooks.openGui((ServerPlayerEntity) player, instance, packetBuffer -> {
            packetBuffer.writeBlockPos(instance.getTile().getPos());
            packetBuffer.writeInt(side.getIndex());
        });
        player.playSound(Ref.WRENCH, SoundCategory.BLOCKS, 1.0f, 2.0f);
        return true;
    }

    public AntimatterContainer getContainer() {
        return null;
    }

    public boolean hasGui() {
        return getGui() != null && getGui().getMenuHandler() != null;
    }

    public List<BakedQuad> onRender(IBakedModel baked, List<BakedQuad> quads, int side) {
        return quads;
    }

    public boolean isEqual(Cover cover) {
        return getId().equals(cover.getId());
    }

    public boolean isEmpty() {
        return getId().equals(COVERNONE.getId());
    }

    public Texture[] getTextures() {
        return new Texture[]{new Texture("gti", "block/machine/cover/" + getRenderId())};
    }

    public ResourceLocation getModel() {
        return new ResourceLocation((this == COVEROUTPUT || this == COVERNONE) ? "antimatter" : "gti" + ":block/machine/cover/" + getRenderId());
    }

    //The default cover model
    public static ResourceLocation getBasicModel() {
        return new ResourceLocation("antimatter" + ":block/cover/basic");
    }

    // TODO: refactor this if/when covers will be singletons
    public void onRegister() {
        String id = getId();
        if (AntimatterAPI.get(Cover.class, id) == null)
            AntimatterAPI.register(Cover.class, this);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void deserialize(CompoundNBT nbt) {

    }

    public void serialize(CompoundNBT nbt) {
        //Write to the NBT at root level
    }

    public boolean hasTexture() {
        return true;
    }
}
