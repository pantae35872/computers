package io.github.pantae35872.computers.registries.block_entity;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.block.ModBlocks;
import io.github.pantae35872.computers.registries.block_entity.custom.ComputerBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModBlockEntity {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);

    public static final RegistryObject<BlockEntityType<ComputerBlockEntity>> COMPUTER =
            BLOCK_ENTITIES.register("computer", () ->
                    BlockEntityType.Builder.of(ComputerBlockEntity::new,
                                    ModBlocks.COMPUTER.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
