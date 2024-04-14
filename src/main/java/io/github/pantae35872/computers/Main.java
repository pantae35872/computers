package io.github.pantae35872.computers;

import com.mojang.logging.LogUtils;
import io.github.pantae35872.computers.networking.ModNetwork;
import io.github.pantae35872.computers.registries.block.ModBlocks;
import io.github.pantae35872.computers.registries.block_entity.ModBlockEntity;
import io.github.pantae35872.computers.registries.block_entity.custom.ComputerBlockEntity;
import io.github.pantae35872.computers.registries.creative_mode_tab.ModCreativeModeTab;
import io.github.pantae35872.computers.registries.item.ModItems;
import io.github.pantae35872.computers.registries.menutype.ModMenuTypes;
import io.github.pantae35872.computers.registries.menutype.custom.ComputerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "computers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ModCreativeModeTab.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntity.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.register();
    }


    private void addCreative(@NotNull BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == ModCreativeModeTab.COMPUTER.get()) {
            event.accept(ModItems.COMPUTER);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.COMPUTER.get(), ComputerScreen::new);
        }
    }
}
