package com.pashacat2015.haha_mod.init;

import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Собственная вкладка креатива мода «Hoogie Woogie».
 */
public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATEVE_TABS_MORE =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, cat_mod.MODID);

    /** Вкладка со всеми предметами и блоками мода */
    public static final RegistryObject<CreativeModeTab> TAB = CREATEVE_TABS_MORE.register("pashacat_craft", () -> CreativeModeTab
            .builder()
            .icon(() -> itemMain.XYGUGREENHEART.get().getDefaultInstance())
            .title(Component.nullToEmpty("Pashacat craft"))
            .displayItems((parameters, output) -> {
                output.accept(itemMain.XYGUGREENHEART.get());
                output.accept(itemMain.XYGUREDNHEART.get());
                output.accept(BlockMod.OMAN_BLOCK_ORE.get());
                output.accept(BlockMod.COOKING_TABLE_BLOCK.get());
                output.accept(itemMain.CORA.get());
                output.accept(itemMain.OM.get());
                output.accept(itemMain.SKEWER.get());
                output.accept(itemMain.BARBEQUI.get());
                output.accept(itemMain.PIZZA.get());
                output.accept(itemMain.SANDWITH.get());
                output.accept(itemMain.CHEESE.get());
                output.accept(itemMain.FRENCHFRICE.get());
                output.accept(BlockMod.FRITEUSE_BLOCK.get());
                output.accept(itemMain.BUTTER.get());
            }).build()
    );


    /** Вкладка со всеми предметами и блоками мода */
    public static final RegistryObject<CreativeModeTab> TAB_FUNCTIONAL_BLOCK = CREATEVE_TABS_MORE.register("pashacat_craft_functional_block", () -> CreativeModeTab
            .builder()
            .icon(() -> itemMain.SKEWER.get().getDefaultInstance())
            .title(Component.nullToEmpty("Pashacat craft functional block"))
            .displayItems((parameters, output) -> {
                output.accept(BlockMod.COOKING_TABLE_BLOCK.get());
                output.accept(BlockMod.FRITEUSE_BLOCK.get());
            }).build()
    );


    public static final RegistryObject<CreativeModeTab> TAB_FOOD = CREATEVE_TABS_MORE.register("pashacat_craft_food", () -> CreativeModeTab
            .builder()
            .icon(() -> itemMain.SANDWITH.get().getDefaultInstance())
            .title(Component.nullToEmpty("Pashacat craft food"))
            .displayItems((parameters, output) -> {
                output.accept(itemMain.XYGUREDNHEART.get());
                output.accept(itemMain.OM.get());
                output.accept(itemMain.BARBEQUI.get());
                output.accept(itemMain.PIZZA.get());
                output.accept(itemMain.SANDWITH.get());
                output.accept(itemMain.CHEESE.get());
                output.accept(itemMain.FRENCHFRICE.get());
                output.accept(itemMain.BUTTER.get());
            }).build()
    );

    public static void register(IEventBus bus) {
        CREATEVE_TABS_MORE.register(bus);
    }
}
