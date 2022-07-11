package noelle.features.inventories.paper.listeners;

import noelle.features.inventories.paper.AbstractInventory;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class InteractionListener implements Listener {

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            var condition = (!inventory.canPlaceItems() && placeItemEvent(event))
                    || (!inventory.canTakeItems() && takeItemEvent(event))
                    || (!inventory.canSwapItems() && swapItemEvent(event))
                    || (!inventory.canDropItems() && dropItemEvent(event))
                    || (!inventory.allowsOtherActions() && otherEvent(event));

            if (condition) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        var holder = event.getInventory().getHolder();

        if (holder instanceof AbstractInventory inventory) {
            if (inventory.canPlaceItems() || !draggingOnInventory(event)) {
                return;
            }

            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    private boolean takeItemEvent(@NotNull InventoryClickEvent event) {
        var inventory = event.getInventory();
        var clickedInventory = event.getClickedInventory();
        var action = event.getAction();

        if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.PLAYER) {
            return false;
        }

        return action == InventoryAction.MOVE_TO_OTHER_INVENTORY || isTakeAction(action);
    }

    private boolean placeItemEvent(@NotNull InventoryClickEvent event) {
        var inventory = event.getInventory();
        var clickedInventory = event.getClickedInventory();
        var action = event.getAction();

        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY
                && clickedInventory != null
                && clickedInventory.getType() == InventoryType.PLAYER
                && inventory.getType() != clickedInventory.getType()) {
            return true;
        }

        return isPlaceAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    private boolean swapItemEvent(@NotNull InventoryClickEvent event) {
        var inventory = event.getInventory();
        var clickedInventory = event.getClickedInventory();
        var action = event.getAction();

        return isSwapAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    private boolean dropItemEvent(@NotNull InventoryClickEvent event) {
        var inventory = event.getInventory();
        var clickedInventory = event.getClickedInventory();
        var action = event.getAction();

        return isDropAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    private boolean otherEvent(@NotNull InventoryClickEvent event) {
        var inventory = event.getInventory();
        var clickedInventory = event.getClickedInventory();
        var action = event.getAction();

        return isOtherAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    private boolean draggingOnInventory(@NotNull InventoryDragEvent event) {
        var topSlots = event.getView().getTopInventory().getSize();
        return event.getRawSlots().stream().anyMatch(slot -> slot < topSlots);
    }

    private boolean isTakeAction(InventoryAction action) {
        return ITEM_TAKE_ACTIONS.contains(action);
    }

    private boolean isPlaceAction(InventoryAction action) {
        return ITEM_PLACE_ACTIONS.contains(action);
    }

    private boolean isSwapAction(InventoryAction action) {
        return ITEM_SWAP_ACTIONS.contains(action);
    }

    private boolean isDropAction(InventoryAction action) {
        return ITEM_DROP_ACTIONS.contains(action);
    }

    private boolean isOtherAction(InventoryAction action) {
        return action == InventoryAction.CLONE_STACK || action == InventoryAction.UNKNOWN;
    }

    private static final Set<InventoryAction> ITEM_TAKE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ALL,
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.MOVE_TO_OTHER_INVENTORY
    ));

    private static final Set<InventoryAction> ITEM_PLACE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ALL
    ));

    private static final Set<InventoryAction> ITEM_SWAP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.HOTBAR_MOVE_AND_READD
    ));

    private static final Set<InventoryAction> ITEM_DROP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ALL_CURSOR
    ));
}
