package muramasa.antimatter.gui.screen;

import muramasa.antimatter.Antimatter;
import muramasa.antimatter.gui.ButtonBody;
import muramasa.antimatter.gui.container.ContainerMachine;
import muramasa.antimatter.gui.event.GuiEvent;
import muramasa.antimatter.gui.widget.SwitchWidjet;
import muramasa.antimatter.machine.MachineFlag;
import muramasa.antimatter.network.packets.GuiEventPacket;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenSteamMachine<T extends ContainerMachine> extends ScreenMachine<T> {

    private final static ButtonBody FLUID = new ButtonBody("fluid_eject", 8, 63, 169, -44,16, 16);
    private final static ButtonBody ITEM = new ButtonBody("item_eject", 26, 63, 151, -26,16, 16);

    public ScreenSteamMachine(T container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawTooltipInArea(container.getTile().getMachineState().getDisplayName(), mouseX, mouseY, (xSize / 2) - 5, 45, 10, 8);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        drawProgress(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        if (container.getTile().has(MachineFlag.ITEM)) {
            addButton(new SwitchWidjet(gui, guiLeft + 26, guiTop + 63, 16, 16, ITEM, (b, s) -> {
                Antimatter.NETWORK.sendToServer(new GuiEventPacket(GuiEvent.ITEM_EJECT, container.getTile().getPos(), s ? 1 : 0));
            }));
        }
        if (container.getTile().has(MachineFlag.FLUID)) {
            addButton(new SwitchWidjet(gui, guiLeft + 8, guiTop + 63, 16, 16, FLUID, (b, s) -> {
                Antimatter.NETWORK.sendToServer(new GuiEventPacket(GuiEvent.FLUID_EJECT, container.getTile().getPos(), s ? 1 : 0));
            }));
        }
    }
}
