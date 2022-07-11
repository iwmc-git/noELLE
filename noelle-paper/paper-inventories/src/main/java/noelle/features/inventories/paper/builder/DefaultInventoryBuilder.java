package noelle.features.inventories.paper.builder;

import noelle.features.inventories.paper.DefaultInventory;
import noelle.features.inventories.paper.type.SupportedInventoryType;

import org.jetbrains.annotations.NotNull;

public class DefaultInventoryBuilder extends AbstractInventoryBuilder<DefaultInventory, DefaultInventoryBuilder> {
    private final SupportedInventoryType inventoryType;

    public DefaultInventoryBuilder(String title, SupportedInventoryType inventoryType) {
        super(title);

        this.inventoryType = inventoryType;
    }

    public static @NotNull DefaultInventoryBuilder inventoryBuilder(String title, SupportedInventoryType inventoryType) {
        return new DefaultInventoryBuilder(title, inventoryType);
    }

    public static @NotNull DefaultInventoryBuilder inventoryBuilder(String title) {
        return new DefaultInventoryBuilder(title, SupportedInventoryType.CHEST);
    }

    @Override
    public DefaultInventory buildInventory() {
        var componentTitle = title();
        var rows = rows();
        var modifiers = interactionModifiers();

        var inventory = inventoryType == SupportedInventoryType.CHEST
                ? new DefaultInventory(rows, componentTitle, modifiers)
                : new DefaultInventory(inventoryType, componentTitle, modifiers);

        var consumer = consumer();
        if (consumer != null) {
            consumer.accept(inventory);
        }

        return inventory;
    }
}
