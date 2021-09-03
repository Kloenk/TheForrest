package dev.kloenk.forest.entities;

import dev.kloenk.forest.ForestMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;

public abstract class TFPartEntity<T extends Entity> {
    public static final Identifier RENDERER = ForestMod.path("noop");

    protected EntityDimensions realSize;
}
