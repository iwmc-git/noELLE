package noelle.features.inventories.paper;

import net.kyori.adventure.text.Component;

import noelle.features.inventories.paper.action.InventoryAction;
import noelle.features.inventories.paper.item.InventoryItem;
import noelle.features.inventories.paper.listeners.InteractionListener;
import noelle.features.inventories.paper.listeners.InventoryListener;
import noelle.features.inventories.paper.modifier.InteractionModifier;
import noelle.features.inventories.paper.type.SupportedInventoryType;
import noelle.features.utils.common.text.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractInventory implements InventoryHolder {
    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(AbstractInventory.class);

    static {
        var pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new InventoryListener(), plugin);
        pluginManager.registerEvents(new InteractionListener(), plugin);
    }

    private Inventory mainInventory;
    private Component title;

    private final Map<Integer, InventoryItem> inventoryItems;
    private final Map<Integer, InventoryAction<InventoryClickEvent>> slotActions;

    private final Set<InteractionModifier> interactionModifiers;

    private InventoryAction<InventoryClickEvent> defaultClickAction;
    private InventoryAction<InventoryClickEvent> defaultTopClickAction;
    private InventoryAction<InventoryClickEvent> playerInventoryAction;
    private InventoryAction<InventoryDragEvent> dragAction;
    private InventoryAction<InventoryCloseEvent> closeInventoryAction;
    private InventoryAction<InventoryOpenEvent> openInventoryAction;
    private InventoryAction<InventoryClickEvent> outsideClickAction;

    private boolean updating;

    private boolean runCloseAction = true;
    private boolean runOpenAction = true;

    private int rows;
    private SupportedInventoryType inventoryType;

    public AbstractInventory(int rows, Component title, Set<InteractionModifier> interactionModifiers) {
        var finalRows = rows;

        if (!(rows >= 1 && rows <= 6)) {
            finalRows = 1;
        }

        this.rows = finalRows;
        this.interactionModifiers = safeCopyOf(interactionModifiers);
        this.title = title;
        this.mainInventory = Bukkit.createInventory(this, finalRows * 9, title);
        this.slotActions = new LinkedHashMap<>(finalRows * 9);
        this.inventoryItems = new LinkedHashMap<>(finalRows * 9);
    }

    public AbstractInventory(@NotNull SupportedInventoryType inventoryType, Component title, Set<InteractionModifier> interactionModifiers) {
        this.inventoryType = inventoryType;
        this.interactionModifiers = safeCopyOf(interactionModifiers);
        this.title = title;
        this.mainInventory = Bukkit.createInventory(this, inventoryType.inventoryType(), title);
        this.slotActions = new LinkedHashMap<>(inventoryType.limit());
        this.inventoryItems = new LinkedHashMap<>(inventoryType.limit());
    }

    public void open(@NotNull HumanEntity player) {
        if (player.isSleeping()) {
            return;
        }

        mainInventory.clear();
        populate();

        player.openInventory(mainInventory);
    }

    public void close(HumanEntity player) {
        close(player, true);
    }

    public void close(HumanEntity player, boolean runCloseAction) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.runCloseAction = runCloseAction;
            player.closeInventory();
            this.runCloseAction = true;
        }, 2L);
    }

    public void newItem(int slot, InventoryItem inventoryItem) {
        validateSlot(slot);
        inventoryItems.put(slot, inventoryItem);
    }

    public void newItem(@NotNull List<Integer> slots, InventoryItem inventoryItem) {
        slots.forEach(slot -> newItem(slot, inventoryItem));
    }

    public void newItem(int row, int col, InventoryItem inventoryItem) {
        newItem(slotFromRowCol(row, col), inventoryItem);
    }

    public void addItem(@NotNull final InventoryItem... items) {
        addItem(false, items);
    }

    public void addItem(boolean expandIfFull, InventoryItem... items) {
        var notAddedItems = new ArrayList<InventoryItem>();

        for (var inventoryItem : items) {
            for (var slot = 0; slot < rows * 9; slot++) {
                if (inventoryItems.get(slot) != null) {
                    if (slot == rows * 9 - 1) {
                        notAddedItems.add(inventoryItem);
                    }

                    continue;
                }

                inventoryItems.put(slot, inventoryItem);
                break;
            }
        }

        if (!expandIfFull || this.rows >= 6 || notAddedItems.isEmpty() || (inventoryType != null && inventoryType != SupportedInventoryType.CHEST)) {
            return;
        }

        this.rows++;
        this.mainInventory = Bukkit.createInventory(this, rows * 9, title);

        update();
        addItem(true, notAddedItems.toArray(new InventoryItem[0]));
    }

    public void removeItem(InventoryItem item) {
        var entry = inventoryItems.entrySet()
                .stream()
                .filter(it -> it.getValue().equals(item))
                .findFirst();

        entry.ifPresent(it -> {
            inventoryItems.remove(it.getKey());
            mainInventory.remove(it.getValue().itemStack());
        });
    }

    public void removeItem(ItemStack item) {
        var entry = inventoryItems.entrySet()
                .stream()
                .filter(it -> it.getValue().itemStack().equals(item))
                .findFirst();

        entry.ifPresent(it -> {
            inventoryItems.remove(it.getKey());
            mainInventory.remove(item);
        });
    }

    public void removeItem(final int slot) {
        validateSlot(slot);

        inventoryItems.remove(slot);
        mainInventory.setItem(slot, null);
    }

    public void removeItem(final int row, final int col) {
        removeItem(slotFromRowCol(row, col));
    }

    public void update() {
        mainInventory.clear();
        populate();

        for (var viewer : mainInventory.getViewers()) {
            var player = (Player) viewer;
            player.updateInventory();
        }
    }

    public void populate() {
        for (var entry : inventoryItems.entrySet()) {
            mainInventory.setItem(entry.getKey(), entry.getValue().itemStack());
        }
    }

    public void newDefaultClickAction(InventoryAction<InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    public void newDefaultTopClickAction(InventoryAction<InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    public void newPlayerInventoryAction(InventoryAction<InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    public void newOutsideClickAction(InventoryAction<InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    public void newDragAction(InventoryAction<InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    public void newCloseInventoryAction(InventoryAction<InventoryCloseEvent> closeInventoryAction) {
        this.closeInventoryAction = closeInventoryAction;
    }

    public void newOpenInventoryAction(InventoryAction<InventoryOpenEvent> openInventoryAction) {
        this.openInventoryAction = openInventoryAction;
    }

    public InventoryAction<InventoryClickEvent> slotAction(int slot) {
        return slotActions.get(slot);
    }

    public void addSlotAction(int slot, InventoryAction<InventoryClickEvent> slotAction) {
        validateSlot(slot);

        slotActions.put(slot, slotAction);
    }

    public void addSlotAction(int row, int col, InventoryAction<@NotNull InventoryClickEvent> slotAction) {
        addSlotAction(slotFromRowCol(row, col), slotAction);
    }

    public InventoryItem inventoryItem(int slot) {
        return inventoryItems.get(slot);
    }

    public AbstractInventory disableItemPlace() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    public AbstractInventory disableItemTake() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    public AbstractInventory disableItemSwap() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    public AbstractInventory disableItemDrop() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    public AbstractInventory disableOtherActions() {
        interactionModifiers.add(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    public AbstractInventory disableAllInteractions() {
        interactionModifiers.addAll(InteractionModifier.VALUES);
        return this;
    }

    public AbstractInventory enableItemPlace() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_PLACE);
        return this;
    }

    public AbstractInventory enableItemTake() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_TAKE);
        return this;
    }

    public AbstractInventory enableItemSwap() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_SWAP);
        return this;
    }

    public AbstractInventory enableItemDrop() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_DROP);
        return this;
    }

    public AbstractInventory enableOtherActions() {
        interactionModifiers.remove(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return this;
    }

    public AbstractInventory enableAllInteractions() {
        interactionModifiers.clear();
        return this;
    }

    public boolean canPlaceItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_PLACE);
    }

    public boolean canTakeItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_TAKE);
    }

    public boolean canSwapItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_SWAP);
    }

    public boolean canDropItems() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_ITEM_DROP);
    }

    public boolean allowsOtherActions() {
        return !interactionModifiers.contains(InteractionModifier.PREVENT_OTHER_ACTIONS);
    }

    private EnumSet<InteractionModifier> safeCopyOf(@NotNull Set<InteractionModifier> set) {
        if (set.isEmpty()) {
            return EnumSet.noneOf(InteractionModifier.class);
        } else {
            return EnumSet.copyOf(set);
        }
    }

    public AbstractInventory updateTitle(String title) {
        updating = true;

        var newTitle = TextUtils.toComponent(title);
        var viewers = mainInventory.getViewers();

        this.mainInventory = Bukkit.createInventory(this, mainInventory.getSize(), newTitle);
        viewers.forEach(this::open);

        updating = false;
        this.title = newTitle;

        return this;
    }

    public void updateItem(int slot, ItemStack itemStack) {
        var inventoryItem = inventoryItems.get(slot);

        if (inventoryItem == null) {
            updateItem(slot, new InventoryItem(itemStack));
            return;
        }

        inventoryItem.newItemStack(itemStack);
        updateItem(slot, inventoryItem);
    }

    public void updateItem(int row, int col, ItemStack itemStack) {
        updateItem(slotFromRowCol(row, col), itemStack);
    }

    public void updateItem(int slot, InventoryItem item) {
        inventoryItems.put(slot, item);
        mainInventory.setItem(slot, item.itemStack());
    }

    public void updateItem(int row, int col, InventoryItem item) {
        updateItem(slotFromRowCol(row, col), item);
    }

    private void validateSlot(final int slot) {
        var limit = inventoryType.limit();

        if (inventoryType == SupportedInventoryType.CHEST) {
            if (slot < 0 || slot >= rows * limit) {
                throwInvalidSlot(slot);
            }

            return;
        }

        if (slot < 0 || slot > limit) throwInvalidSlot(slot);
    }

    private void throwInvalidSlot(final int slot) {
        if (inventoryType == SupportedInventoryType.CHEST) {
            throw new RuntimeException("Slot " + slot + " is not valid for the inventory type - " + inventoryType.name() + " and rows - " + rows + "!");
        }

        throw new RuntimeException("Slot " + slot + " is not valid for the inventory type - " + inventoryType.name() + "!");
    }

    protected int slotFromRowCol(int row, int col) {
        return (col + (row - 1) * 9) - 1;
    }

    protected void updateInventory(Inventory inventory) {
        this.mainInventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return mainInventory;
    }

    public InventoryAction<InventoryClickEvent> defaultClickAction() {
        return defaultClickAction;
    }

    public InventoryAction<InventoryClickEvent> defaultTopClickAction() {
        return defaultTopClickAction;
    }

    public InventoryAction<InventoryClickEvent> outsideClickAction() {
        return outsideClickAction;
    }

    public InventoryAction<InventoryClickEvent> playerInventoryAction() {
        return playerInventoryAction;
    }

    public InventoryAction<InventoryCloseEvent> closeInventoryAction() {
        return closeInventoryAction;
    }

    public InventoryAction<InventoryDragEvent> dragAction() {
        return dragAction;
    }

    public InventoryAction<InventoryOpenEvent> openInventoryAction() {
        return openInventoryAction;
    }

    public Map<Integer, InventoryAction<InventoryClickEvent>> slotActions() {
        return slotActions;
    }

    public Set<InteractionModifier> interactionModifiers() {
        return interactionModifiers;
    }

    public Component title() {
        return title;
    }

    public int rows() {
        return rows;
    }

    public Map<Integer, InventoryItem> inventoryItems() {
        return inventoryItems;
    }

    public SupportedInventoryType inventoryType() {
        return inventoryType;
    }

    public boolean updating() {
        return updating;
    }

    public void updating(boolean updating) {
        this.updating = updating;
    }

    public boolean runCloseAction() {
        return runCloseAction;
    }

    public void runCloseAction(boolean runCloseAction) {
        this.runCloseAction = runCloseAction;
    }

    public boolean runOpenAction() {
        return runOpenAction;
    }

    public void runOpenAction(boolean runOpenAction) {
        this.runOpenAction = runOpenAction;
    }
}
