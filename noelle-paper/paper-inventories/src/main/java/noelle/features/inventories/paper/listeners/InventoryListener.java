package noelle.features.inventories.paper.listeners;

import noelle.features.inventories.paper.AbstractInventory;
import noelle.features.inventories.paper.PagedInventory;
import noelle.features.inventories.paper.item.InventoryItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

public class InventoryListener implements Listener {

    @EventHandler
    public void onGuiClick(@NotNull InventoryClickEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            var outsideClickAction = inventory.outsideClickAction();
            if (outsideClickAction != null && event.getClickedInventory() == null) {
                outsideClickAction.execute(event);
                return;
            }

            if (event.getClickedInventory() == null) {
                return;
            }

            var defaultTopClick = inventory.defaultTopClickAction();
            if (defaultTopClick != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
                defaultTopClick.execute(event);
            }

            var playerInventoryClick = inventory.playerInventoryAction();
            if (playerInventoryClick != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
                playerInventoryClick.execute(event);
            }

            var defaultClick = inventory.defaultClickAction();
            if (defaultClick != null) {
                defaultClick.execute(event);
            }

            var slotAction = inventory.slotAction(event.getSlot());
            if (slotAction != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
                slotAction.execute(event);
            }

            InventoryItem inventoryItem;
            if (inventory instanceof PagedInventory pagedInventory) {
                inventoryItem = pagedInventory.inventoryItem(event.getSlot());
                if (inventoryItem == null) {
                    inventoryItem = pagedInventory.pageItem(event.getSlot());
                }
            } else {
                inventoryItem = inventory.inventoryItem(event.getSlot());
            }

            if (!isInventoryItem(event.getCurrentItem(), inventoryItem)) {
                return;
            }

            var itemAction = inventoryItem.action();
            if (itemAction != null) {
                itemAction.execute(event);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            var dragAction = inventory.dragAction();
            if (dragAction != null) dragAction.execute(event);
        }
    }

    @EventHandler
    public void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            var closeAction = inventory.closeInventoryAction();
            if (closeAction != null && !inventory.updating() && inventory.runCloseAction()) {
                closeAction.execute(event);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            var openAction = inventory.openInventoryAction();
            if (openAction != null && !inventory.updating()) {
                openAction.execute(event);
            }
        }
    }

    private boolean isInventoryItem(ItemStack currentItem, InventoryItem inventoryItem) {
        if (currentItem == null || inventoryItem == null) {
            return false;
        }

        var nbtString = inventoryItem.nbtItem().getCompound("noelle-inventory").getString("noelle-inventory");
        if (nbtString == null) {
            return false;
        }

        return nbtString.equals(inventoryItem.uuid().toString());
    }
}
