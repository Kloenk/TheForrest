package dev.kloenk.forest.util;

import dev.kloenk.forest.mixin.DamageSourceAccessor;
import net.minecraft.entity.damage.DamageSource;

public class TFDamagesources {
    public static final DamageSource LICH_BOLT = ((DamageSourceAccessor)DamageSourceAccessor.of(tfSource("lichBolt")).setProjectile().setUsesMagic()).setBypassesArmorInvoker();
    public static final DamageSource LICH_BOMB = ((DamageSourceAccessor)DamageSourceAccessor.of(tfSource("lichBomb")).setProjectile().setUsesMagic()).setBypassesArmorInvoker();

    public static String tfSource(String name) {
        return "forest." + name;
    }
}
