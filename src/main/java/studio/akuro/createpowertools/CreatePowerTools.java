package studio.akuro.createpowertools;

import com.mojang.serialization.Codec;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper.Palette;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import studio.akuro.createpowertools.item.KineticToolItem;

import java.util.List;

import static net.neoforged.neoforge.common.util.AttributeUtil.BASE_ATTACK_DAMAGE_ID;
import static net.neoforged.neoforge.common.util.AttributeUtil.BASE_ATTACK_SPEED_ID;

@Mod(CreatePowerTools.MODID)
public class CreatePowerTools {

    public static final String MODID = "create_power_tools";

    private static final float NO_AIR_SPEED = 1.0f;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue BLOCKS_PER_TANK = BUILDER.comment("Number of blocks mineable with one full backtank.").defineInRange("blocksPerTank", 2700, 900, 9000);
    public static final ModConfigSpec SPEC = BUILDER.build();

    //Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BLOCKS_MINED = COMPONENTS.registerComponentType("blocks_mined", builder -> builder.persistent(Codec.INT));

    //Items
    private static final ItemAttributeModifiers MODIFIERS = ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, 1.6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
            .build();
    public static final DeferredItem<KineticToolItem> KINETIC_MULTITOOL = ITEMS.register("kinetic_multitool", () -> new KineticToolItem(Tiers.NETHERITE, 12.0f, new Item.Properties().stacksTo((1)).attributes(MODIFIERS),
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.MINEABLE_WITH_HOE));

    public CreatePowerTools(IEventBus eventBus, ModContainer modContainer) {
        ITEMS.register(eventBus);
        COMPONENTS.register(eventBus);
        modContainer.registerConfig(ModConfig.Type.SERVER, SPEC);

        eventBus.addListener(this::addCreativeTabContents);
        eventBus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.addListener(this::onBreakSpeed);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        Item item = KINETIC_MULTITOOL.get();
        TooltipModifier.REGISTRY.register(item, new ItemDescription.Modifier(item, Palette.STANDARD_CREATE));
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
