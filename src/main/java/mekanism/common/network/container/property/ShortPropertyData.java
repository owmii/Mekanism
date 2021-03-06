package mekanism.common.network.container.property;

import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.network.container.PacketUpdateContainerShort;
import net.minecraft.network.PacketBuffer;

public class ShortPropertyData extends PropertyData {

    private final short value;

    public ShortPropertyData(short property, short value) {
        super(PropertyType.SHORT, property);
        this.value = value;
    }

    @Override
    public PacketUpdateContainerShort getSinglePacket(short windowId) {
        return new PacketUpdateContainerShort(windowId, getProperty(), value);
    }

    @Override
    public void handleWindowProperty(MekanismContainer container) {
        container.handleWindowProperty(getProperty(), value);
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {
        super.writeToPacket(buffer);
        buffer.writeShort(value);
    }
}