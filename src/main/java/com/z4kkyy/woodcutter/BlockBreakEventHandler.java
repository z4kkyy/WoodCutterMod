package com.z4kkyy.woodcutter;

import java.util.Set;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.level.BlockEvent;


public class BlockBreakEventHandler {

    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        // LOGGER.info("BlockEvent.BreakEvent Detected at: " + pos.toString() + " by player: " + player.getName().getString());

        if (!state.is(BlockTags.LOGS)) {
            return;
        }

        ItemStack heldItem = player.getMainHandItem();
        if (!heldItem.canPerformAction(ToolActions.AXE_DIG)) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel)) {
            return;
        }

        ServerLevel world = (ServerLevel) event.getLevel();

        Set<BlockPos> logs = TreeDetector.detectTree(world, pos);

        if (logs.isEmpty() || logs.size() <= 1) {
            return;
        }

        LOGGER.info("Detected tree break event at: " + pos.toString() + " with size: " + logs.size());
        logs.remove(pos);

        ItemStack axeItem = heldItem.copy();

        for (BlockPos logPos : logs) {
            BlockState logState = world.getBlockState(logPos);
            BlockEntity blockEntity = world.getBlockEntity(logPos);

            // LOGGER.info("Dropping resources for block at: " + logPos.toString() + " with state: " + logState.toString());
            Block.dropResources(logState, world, logPos, blockEntity, player, axeItem);
            world.setBlock(logPos, Blocks.AIR.defaultBlockState(), 3);

            int currentDamage = heldItem.getDamageValue();
            int maxDamage = heldItem.getMaxDamage();
            heldItem.setDamageValue(currentDamage + 1);

            // Check if the axe broke
            if (currentDamage + 1 >= maxDamage) {
                heldItem.shrink(1);
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ITEM_BREAK, SoundSource.PLAYERS,
                                1.0F, 1.0F);
                LOGGER.info("Axe broke during tree cutting, stopping process");
                break;
            }
        }
    }
}
