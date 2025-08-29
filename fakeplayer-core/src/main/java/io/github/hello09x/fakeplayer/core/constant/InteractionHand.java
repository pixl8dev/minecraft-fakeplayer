package io.github.hello09x.fakeplayer.core.constant;

import lombok.AllArgsConstructor;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public enum InteractionHand {

    MAIN_HAND(EquipmentSlot.HAND),

    OFF_HAND(EquipmentSlot.OFF_HAND);

    private final EquipmentSlot slot;

    InteractionHand(EquipmentSlot slot) {
        this.slot = slot;
    }

    public @NotNull EquipmentSlot asSlot() {
        return slot;
    }

}
