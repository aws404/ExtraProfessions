package com.github.aws404.extra_professions.block_entity;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.block.ExtraBlocks;
import com.github.aws404.extra_professions.screen.AnnealerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class AnnealerBlockEntity extends AbstractFurnaceBlockEntity {
    public AnnealerBlockEntity(BlockPos pos, BlockState state) {
        super(ExtraBlocks.ANNEALER_BLOCK_ENTITY, pos, state, ExtraProfessionsMod.ANNEALING_RECIPE_TYPE);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.annealer");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AnnealerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
