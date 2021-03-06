package mekanism.common.registration.impl;

import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.INamedEntry;
import mekanism.common.registration.WrappedDeferredRegister;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeDeferredRegister extends WrappedDeferredRegister<ContainerType<?>> {

    public ContainerTypeDeferredRegister(String modid) {
        super(modid, ForgeRegistries.CONTAINERS);
    }

    public <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<MekanismTileContainer<TILE>> register(INamedEntry nameProvider, Class<TILE> tileClass) {
        return register(nameProvider.getInternalRegistryName(), tileClass);
    }

    public <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<MekanismTileContainer<TILE>> register(String name, Class<TILE> tileClass) {
        //Temporarily generate this using null as we replace it with a proper value before we actually use this so it is fine
        ContainerTypeRegistryObject<MekanismTileContainer<TILE>> registryObject = new ContainerTypeRegistryObject<>(null);
        IContainerFactory<MekanismTileContainer<TILE>> factory = (id, inv, buf) ->
              new MekanismTileContainer<>(registryObject, id, inv, MekanismTileContainer.getTileFromBuf(buf, tileClass));
        return register(name, () -> IForgeContainerType.create(factory), registryObject::setRegistryObject);
    }

    public <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<EmptyTileContainer<TILE>> registerEmpty(INamedEntry nameProvider, Class<TILE> tileClass) {
        return registerEmpty(nameProvider.getInternalRegistryName(), tileClass);
    }

    public <TILE extends TileEntityMekanism> ContainerTypeRegistryObject<EmptyTileContainer<TILE>> registerEmpty(String name, Class<TILE> tileClass) {
        //Temporarily generate this using null as we replace it with a proper value before we actually use this so it is fine
        ContainerTypeRegistryObject<EmptyTileContainer<TILE>> registryObject = new ContainerTypeRegistryObject<>(null);
        IContainerFactory<EmptyTileContainer<TILE>> factory = (id, inv, buf) ->
              new EmptyTileContainer<>(registryObject, id, inv, MekanismTileContainer.getTileFromBuf(buf, tileClass));
        return register(name, () -> IForgeContainerType.create(factory), registryObject::setRegistryObject);
    }

    public <CONTAINER extends Container> ContainerTypeRegistryObject<CONTAINER> register(INamedEntry nameProvider, IContainerFactory<CONTAINER> factory) {
        return register(nameProvider.getInternalRegistryName(), factory);
    }

    public <CONTAINER extends Container> ContainerTypeRegistryObject<CONTAINER> register(String name, IContainerFactory<CONTAINER> factory) {
        return register(name, () -> IForgeContainerType.create(factory), ContainerTypeRegistryObject::new);
    }
}