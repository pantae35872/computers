package io.github.pantae35872.computers.registries.menutype;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.networking.PendingPacket;
import io.github.pantae35872.computers.networking.packet.ComputerBlockPosS2CPacket;
import io.github.pantae35872.computers.registries.menutype.custom.ComputerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES,
            Main.MOD_ID
    );

    public static final RegistryObject<MenuType<ComputerMenu>> COMPUTER = registerMenuType("computer",
            ComputerMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }
    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
