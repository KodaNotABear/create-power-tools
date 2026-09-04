package studio.akuro.createpowertools;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.akuro.createpowertools.item.KineticToolItem;

@Mod(CreatePowerTools.MODID)
public class CreatePowerTools {

    public static final String MODID = "create_power_tools";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    //Items
    public static final DeferredItem<KineticToolItem> KINETIC_PICKAXE = ITEMS.register("kinetic_pickaxe", () -> new KineticToolItem(Tiers.NETHERITE, BlockTags.MINEABLE_WITH_PICKAXE, 12.0f, new Item.Properties().stacksTo(1)));

    public CreatePowerTools(IEventBus eventBus, ModContainer modContainer) {
        ITEMS.register(eventBus);

        eventBus.addListener(this::addCreativeTabContents);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(new ItemStack(Items.NETHERITE_PICKAXE), KINETIC_PICKAXE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }


}
