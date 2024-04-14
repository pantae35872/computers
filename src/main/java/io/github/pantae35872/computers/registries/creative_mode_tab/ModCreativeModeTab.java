package io.github.pantae35872.computers.registries.creative_mode_tab;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB, Main.MOD_ID);
    public static final RegistryObject<CreativeModeTab> COMPUTER = CREATIVE_MODE_TAB.register(
            "computer", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.COMPUTER.get()))
                    .title(Component.literal("Computer")).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
