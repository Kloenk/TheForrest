package dev.kloenk.forest.util;

import dev.kloenk.forest.mixin.ClientAdvancementManagerAccessor;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class PlayerHelper {
    public static boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player, Identifier... requiredAdvancements) {
        for (Identifier advancement : requiredAdvancements) {
            if (player.world.isClient()) {
                if (player instanceof ClientPlayerEntity) {
                    ClientAdvancementManager manager = ((ClientPlayerEntity) player).networkHandler.getAdvancementHandler();
                    Advancement adv = manager.getManager().get(advancement);
                    if (adv == null)
                        return false;
                    AdvancementProgress progress = ((ClientAdvancementManagerAccessor) manager).getAdvancementProgresses().get(adv);
                    return progress != null && progress.isDone();
                }
                return false;
            } else {
                if (player instanceof ServerPlayerEntity) {
                    ServerWorld world = ((ServerPlayerEntity) player).getServerWorld();
                    Advancement adv = world.getServer().getAdvancementLoader().get(advancement);
                    return adv != null && ((ServerPlayerEntity) player).getAdvancementTracker().getProgress(adv).isDone();
                }
                return false;
            }
        }
        return true;
    }
}
