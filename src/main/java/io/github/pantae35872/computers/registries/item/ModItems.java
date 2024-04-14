package io.github.pantae35872.computers.registries.item;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.block.ModBlocks;
import io.github.pantae35872.computers.registries.block_entity.custom.ComputerBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> COMPUTER =
            ITEMS.register("computer", () -> new BlockItem(ModBlocks.COMPUTER.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
