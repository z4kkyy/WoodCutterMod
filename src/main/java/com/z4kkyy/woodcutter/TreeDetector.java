package com.z4kkyy.woodcutter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class TreeDetector {

    public static Set<BlockPos> detectTree(Level world, BlockPos startPos) {
        // startPos = pos of the block broken first
        BlockState startState = world.getBlockState(startPos);

        if (!startState.is(BlockTags.LOGS)) {
            return new HashSet<>();
        }

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> checkedPositions = new HashSet<>();
        Queue<BlockPos> toCheck = new LinkedList<>();

        toCheck.add(startPos);
        checkedPositions.add(startPos);

        // Breadth-first search
        while (!toCheck.isEmpty()) {
            BlockPos currentPos = toCheck.poll();
            BlockState currentState = world.getBlockState(currentPos);

            if (currentState.is(BlockTags.LOGS)) {
                logPositions.add(currentPos);

                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x == 0 && y == 0 && z == 0) continue;

                            BlockPos newPos = currentPos.offset(x, y, z);
                            if (!checkedPositions.contains(newPos)) {
                                checkedPositions.add(newPos);
                                toCheck.add(newPos);
                            }
                        }
                    }
                }
            }
        }

        return logPositions;
    }
}
