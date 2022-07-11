package noelle.features.inventories.paper.item;

import de.tr7zw.nbtapi.NBTItem;

import noelle.features.inventories.paper.action.InventoryAction;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryItem {
    private final UUID uuid = UUID.randomUUID();
    private final NBTItem nbtItem;

    private InventoryAction<InventoryClickEvent> action;
    private ItemStack itemStack;

    public InventoryItem(ItemStack stack) {
        this.itemStack = stack;
        this.nbtItem = new NBTItem(stack);

        var compound = nbtItem.addCompound("noelle-inventory");
        compound.setString("noelle-inventory", uuid.toString());
    }

    public NBTItem nbtItem() {
        return nbtItem;
    }

    public InventoryAction<InventoryClickEvent> action() {
        return action;
    }

    public ItemStack itemStack() {
        return itemStack;
    }

    public InventoryItem newAction(InventoryAction<InventoryClickEvent> action) {
        this.action = action;
        return this;
    }

    public void newItemStack(ItemStack itemStack) {
        var item = new NBTItem(itemStack);
        var compound = item.addCompound("noelle-inventory");

        compound.setString("noelle-inventory", uuid.toString());

        this.itemStack = item.getItem();
    }

    public UUID uuid() {
        return uuid;
    }
}
