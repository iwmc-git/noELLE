package noelle.features.inventories.paper.type;

import org.bukkit.event.inventory.InventoryType;

/**
 * Supported inventories
 */
public enum SupportedInventoryType {
    CHEST(InventoryType.CHEST, 9),
    WORKBENCH(InventoryType.WORKBENCH, 9),
    HOPPER(InventoryType.HOPPER, 5),
    DISPENSER(InventoryType.DISPENSER, 8),
    BREWING(InventoryType.BREWING, 4);

    private final InventoryType inventoryType;
    private final int limit;

    SupportedInventoryType(InventoryType inventoryType, final int limit) {
        this.inventoryType = inventoryType;
        this.limit = limit;
    }

    public InventoryType inventoryType() {
        return inventoryType;
    }

    public int limit() {
        return limit;
    }
}
