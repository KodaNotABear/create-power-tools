package studio.akuro.createpowertools.item;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import studio.akuro.createpowertools.CreatePowerTools;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.content.equipment.armor.BacktankItem.BAR_COLOR;

public class KineticToolItem extends Item {

    @SafeVarargs
    public KineticToolItem(Tier tier, float speed, Properties properties, TagKey<Block>... mineable) {
        super(properties.component(DataComponents.TOOL, buildTool(tier, speed, mineable)));
    }

    @SafeVarargs
    private static Tool buildTool(Tier tier, float speed, TagKey<Block>... mineable) {
        List<Tool.Rule> rules = new ArrayList<>();
        rules.add(Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()));

        for (TagKey<Block> tag : mineable) {
            rules.add(Tool.Rule.minesAndDrops(tag, speed));
        }

        return new Tool(rules, 1.0f, 0);
    }


    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity player) {

        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0f ) {
            drainAir(stack, player);
        }

        return super.mineBlock(stack, level, state, pos, player);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return BacktankUtil.getBarWidth(stack, CreatePowerTools.BLOCKS_PER_TANK.get());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    private static void drainAir(ItemStack stack, LivingEntity player) {
        int tankSize = BacktankUtil.maxAirWithoutEnchants();
        int blocksPerAir = Math.max(1, CreatePowerTools.BLOCKS_PER_TANK.get() / tankSize);

        int counter = stack.getOrDefault(CreatePowerTools.BLOCKS_MINED.get(), 0) + 1;

        if (counter >= blocksPerAir) {
            counter = 0;
            BacktankUtil.canAbsorbDamage(player, tankSize);
        }

        stack.set(CreatePowerTools.BLOCKS_MINED.get(), counter);
    }

}
