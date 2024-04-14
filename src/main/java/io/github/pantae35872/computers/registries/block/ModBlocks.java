package io.github.pantae35872.computers.registries.block;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.block.custom.ComputerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, Main.MOD_ID);

    public static final RegistryObject<Block> COMPUTER = BLOCKS.register("computer", ComputerBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
