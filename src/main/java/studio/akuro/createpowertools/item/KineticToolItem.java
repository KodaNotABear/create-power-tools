package studio.akuro.createpowertools.item;

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

import java.util.ArrayList;
import java.util.List;

public class KineticToolItem extends Item {

    private static final int AIR_PER_BLOCK = 1;

    public KineticToolItem(Tier tier, TagKey<Block> tagKey, float speed, Properties properties) {
        super(properties.component(DataComponents.TOOL, buildTool(tier, tagKey, speed)));
    }

    private static Tool buildTool(Tier tier, TagKey<Block> tagKey, float speed) {
        List<Tool.Rule> rules = new ArrayList<>();

        rules.add(Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()));
        rules.add(Tool.Rule.minesAndDrops(tagKey, speed));

        return new Tool(rules, 1.0f, 0);
    }


    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity player) {



        return super.mineBlock(stack, level, state, pos, player);
    }
}
