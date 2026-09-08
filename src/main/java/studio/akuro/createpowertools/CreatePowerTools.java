package studio.akuro.createpowertools;

import com.mojang.serialization.Codec;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.akuro.createpowertools.item.KineticToolItem;

import java.util.List;

@Mod(CreatePowerTools.MODID)
public class CreatePowerTools {

    public static final String MODID = "create_power_tools";

    private static final float NO_AIR_SPEED = 1.0f;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    //Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BLOCKS_MINED = COMPONENTS.registerComponentType("blocks_mined", builder -> builder.persistent(Codec.INT));

    //Items
    public static final DeferredItem<KineticToolItem> KINETIC_MULTITOOL = ITEMS.register("kinetic_multitool", () -> new KineticToolItem(Tiers.NETHERITE, 12.0f, new Item.Properties().stacksTo((1)),
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.MINEABLE_WITH_HOE));

    public CreatePowerTools(IEventBus eventBus, ModContainer modContainer) {
        ITEMS.register(eventBus);
        COMPONENTS.register(eventBus);

        eventBus.addListener(this::addCreativeTabContents);
        NeoForge.EVENT_BUS.addListener(this::onBreakSpeed);
    }

    private void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!(event.getEntity().getMainHandItem().getItem() instanceof KineticToolItem)) return;

        List<ItemStack> list = BacktankUtil.getAllWithAir(event.getEntity());

        if (!list.isEmpty()) return;

        event.setNewSpeed(Math.min(event.getNewSpeed(), NO_AIR_SPEED));
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(new ItemStack(Items.NETHERITE_PICKAXE), KINETIC_MULTITOOL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }


}
