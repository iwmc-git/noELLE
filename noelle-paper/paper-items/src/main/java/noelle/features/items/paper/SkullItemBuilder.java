package noelle.features.items.paper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullItemBuilder extends AbstractItemBuilder<SkullItemBuilder> {
    private static final Field PROFILE_FIELD;

    static {
        try {
            var skullMeta = (SkullMeta) new ItemStack(Material.PLAYER_HEAD).getItemMeta();

            var field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);

            PROFILE_FIELD = field;
        } catch (NoSuchFieldException fieldException) {
            throw new RuntimeException(fieldException);
        }
    }

    protected SkullItemBuilder(ItemStack stack) {
        super(stack);

        if (stack.getType() != Material.PLAYER_HEAD) {
            throw new RuntimeException("Skull item builder requires only PLAYER_HEAD material!");
        }
    }

    public static @NotNull SkullItemBuilder makeItem() {
        return new SkullItemBuilder(new ItemStack(Material.PLAYER_HEAD));
    }

    public SkullItemBuilder texture(String texture, UUID profileId) {
        if (PROFILE_FIELD == null) {
            return this;
        }

        var skullMeta = (SkullMeta) meta();
        var profile = new GameProfile(profileId, null);

        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            PROFILE_FIELD.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }

        updateMeta(skullMeta);
        return this;
    }
}
