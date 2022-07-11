package noelle.features.inventories.paper;

import net.kyori.adventure.text.Component;

import noelle.features.inventories.paper.builder.DefaultInventoryBuilder;
import noelle.features.inventories.paper.modifier.InteractionModifier;
import noelle.features.inventories.paper.type.SupportedInventoryType;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DefaultInventory extends AbstractInventory {

    public DefaultInventory(int rows, Component title, Set<InteractionModifier> interactionModifiers) {
        super(rows, title, interactionModifiers);
    }

    public DefaultInventory(@NotNull SupportedInventoryType inventoryType, Component title, Set<InteractionModifier> interactionModifiers) {
        super(inventoryType, title, interactionModifiers);
    }

    @Contract("_, _ -> new")
    public static @NotNull DefaultInventoryBuilder newBuilder(String title, SupportedInventoryType inventoryType) {
        return DefaultInventoryBuilder.inventoryBuilder(title, inventoryType);
    }

    @Contract("_ -> new")
    public static @NotNull DefaultInventoryBuilder newBuilder(String title) {
        return DefaultInventoryBuilder.inventoryBuilder(title);
    }
}
