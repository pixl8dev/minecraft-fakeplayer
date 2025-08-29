package io.github.hello09x.fakeplayer.core.repository.model;

import io.github.hello09x.fakeplayer.api.spi.ActionSetting;
import io.github.hello09x.fakeplayer.api.spi.ActionType;
import io.github.hello09x.fakeplayer.core.command.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author tanyaofei
 * @since 2024/8/13
 **/
public enum Feature implements Translatable, Singletons {

    /**
     * 是否具有碰撞箱
     */
    collidable(
            "fakeplayer.config.collidable",
            List.of(Permission.config),
            List.of("true", "false"),
            "true",
            faker -> String.valueOf(faker.isCollidable()),
            (faker, value) -> faker.setCollidable(Boolean.parseBoolean(value))
    ),

    /**
     * 是否无敌
     */
    invulnerable(
            "fakeplayer.config.invulnerable",
            List.of(Permission.config),
            List.of("true", "false"),
            "false",
            faker -> String.valueOf(faker.isInvulnerable()),
            (faker, value) -> faker.setInvulnerable(Boolean.parseBoolean(value))
    ),

    /**
     * 金刚狼模式 -> 超强的再生能力
     */
    wolverine(
            "fakeplayer.config.wolverine",
            List.of(Permission.config),
            List.of("true", "false"),
            "false",
            fake -> String.valueOf(Optional.ofNullable(fake.getPotionEffect(PotionEffectType.REGENERATION)).map(PotionEffect::isInfinite).orElse(false)),
            (faker, value) -> {
                if (Boolean.parseBoolean(value)) {
                    faker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 4, true, true));
                } else {
                    faker.removePotionEffect(PotionEffectType.REGENERATION);
                }
            }
    ),

    /**
     * 是否自动看向实体
     */
    look_at_entity(
            "fakeplayer.config.look_at_entity",
            List.of(Permission.config, Permission.look),
            List.of("true", "false"),
            "false",
            faker -> String.valueOf(actionManager.get().hasActiveAction(faker, ActionType.LOOK_AT_NEAREST_ENTITY)),
            (faker, value) -> {
                if (Boolean.parseBoolean(value)) {
                    actionManager.get().setAction(faker, ActionType.LOOK_AT_NEAREST_ENTITY, ActionSetting.continuous());
                } else {
                    actionManager.get().setAction(faker, ActionType.LOOK_AT_NEAREST_ENTITY, ActionSetting.stop());
                }
            }
    ),

    /**
     * 是否能够拾取物品
     */
    pickup_items(
            "fakeplayer.config.pickup_items",
            List.of(Permission.config),
            List.of("true", "false"),
            "true",
            faker -> String.valueOf(faker.getCanPickupItems()),
            (faker, value) -> faker.setCanPickupItems(Boolean.parseBoolean(value))
    ),

    /**
     * 是否使用皮肤
     */
    skin(
            "fakeplayer.config.skin",
            List.of(Permission.config),
            List.of("true", "false"),
            "true",
            null,
            null
    ),

    /**
     * 是否自动补货
     */
    replenish(
            "fakeplayer.config.replenish",
            List.of(Permission.config, Permission.replenish),
            List.of("true", "false"),
            "false",
            faker -> String.valueOf(replenishManager.get().isReplenish(faker)),
            (faker, value) -> replenishManager.get().setReplenish(faker, Boolean.parseBoolean(value))
    ),

    /**
     * 是否自动钓鱼
     */
    autofish(
            "fakeplayer.config.autofish",
            List.of(Permission.config, Permission.autofish),
            List.of("true", "false"),
            "false",
            faker -> String.valueOf(autofishManager.get().isAutofish(faker)),
            (faker, value) -> autofishManager.get().setAutofish(faker, Boolean.parseBoolean(value))
    ),
    ;

    private final String translationKey;
    private final List<String> permissions;
    private final List<String> options;
    private final String defaultOption;
    private final Function<Player, String> detector;
    private final BiConsumer<Player, String> modifier;

    Feature(
        String translationKey,
        List<String> permissions,
        List<String> options,
        String defaultOption,
        Function<Player, String> detector,
        BiConsumer<Player, String> modifier
    ) {
        this.translationKey = translationKey;
        this.permissions = permissions;
        this.options = options;
        this.defaultOption = defaultOption;
        this.detector = detector;
        this.modifier = modifier;
    }

    @Override
    public @NotNull String translationKey() {
        return this.translationKey;
    }

    public @NotNull List<String> getPermissions() {
        return this.permissions;
    }

    public @NotNull List<String> getOptions() {
        return this.options;
    }

    public @NotNull String getDefaultOption() {
        return this.defaultOption;
    }



    public boolean hasDetector() {
        return this.detector != null;
    }
    
    public @Nullable Function<Player, String> getDetector() {
        return this.detector;
    }

    public boolean hasModifier() {
        return this.modifier != null;
    }
    
    public @Nullable BiConsumer<Player, String> getModifier() {
        return this.modifier;
    }

    public boolean testPermissions(@NotNull CommandSender sender) {
        if (this.permissions.isEmpty()) {
            return true;
        }

        for (var permission : this.permissions) {
            if (!sender.hasPermission(permission)) {
                return false;
            }
        }

        return true;
    }


}
