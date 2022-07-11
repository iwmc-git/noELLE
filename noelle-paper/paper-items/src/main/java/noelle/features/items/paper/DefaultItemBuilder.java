package noelle.features.items.paper;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

public class DefaultItemBuilder extends AbstractItemBuilder<DefaultItemBuilder> {

    protected DefaultItemBuilder(ItemStack stack) {
        super(stack);

        try {
            Class.forName("de.tr7zw.nbtapi.NBTItem");
        } catch (Exception exception) {
            throw new RuntimeException("Plugin NBTAPI not loaded!");
        }
    }

    public static @NotNull DefaultItemBuilder makeItem(ItemStack stack) {
        return new DefaultItemBuilder(stack);
    }

    public static @NotNull DefaultItemBuilder makeItem(Material material) {
        return new DefaultItemBuilder(new ItemStack(material));
    }
}
