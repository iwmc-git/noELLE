package noelle.features.inventories.paper;

import net.kyori.adventure.text.Component;

import noelle.features.inventories.paper.builder.PagedInventoryBuilder;
import noelle.features.inventories.paper.item.InventoryItem;
import noelle.features.inventories.paper.modifier.InteractionModifier;
import noelle.features.inventories.paper.type.SupportedInventoryType;
import noelle.features.utils.common.text.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PagedInventory extends AbstractInventory {
    private final List<InventoryItem> pageItems = new ArrayList<>();
    private final Map<Integer, InventoryItem> currentPage;

    private int pageSize;
    private int pageNumber = 1;


    public PagedInventory(int rows, int pageSize, Component title, Set<InteractionModifier> interactionModifiers) {
        super(rows, title, interactionModifiers);

        this.pageSize = pageSize;
        this.currentPage = new LinkedHashMap<>(rows * 9);
    }

    public PagedInventory(@NotNull SupportedInventoryType inventoryType, Component title, Set<InteractionModifier> interactionModifiers) {
        super(inventoryType, title, interactionModifiers);
        throw new RuntimeException("Not supported!");
    }

    public static @NotNull PagedInventoryBuilder inventoryBuilder(String title, int pageSize) {
        return new PagedInventoryBuilder(title, pageSize);
    }

    public void addItem(InventoryItem item) {
        pageItems.add(item);
    }

    @Override
    public void addItem(InventoryItem... items) {
        pageItems.addAll(Arrays.asList(items));
    }

    @Override
    public void update() {
        getInventory().clear();
        populate();

        updatePage();
    }

    public void updatePageItem(int slot, ItemStack itemStack) {
        if (!currentPage.containsKey(slot)) {
            return;
        }

        var inventoryItem = currentPage.get(slot);
        inventoryItem.newItemStack(itemStack);

        getInventory().setItem(slot, inventoryItem.itemStack());
    }

    public void updatePageItem(int row, int col, ItemStack itemStack) {
        updateItem(slotFromRowCol(row, col), itemStack);
    }

    public void updatePageItem(int slot, InventoryItem item) {
        if (!currentPage.containsKey(slot)) {
            return;
        }

        // TODO: Хуй знает кудав эту переменную засунуть
        var oldItem = currentPage.get(slot);
        var index = pageItems.indexOf(currentPage.get(slot));

        currentPage.put(slot, item);
        pageItems.set(index, item);

        getInventory().setItem(slot, item.itemStack());
    }

    public void updatePageItem(int row, int col, InventoryItem item) {
        updateItem(slotFromRowCol(row, col), item);
    }

    public void removePageItem(InventoryItem item) {
        pageItems.remove(item);
        updatePage();
    }

    public void removePageItem(ItemStack item) {
        var inventoryItem = pageItems.stream().filter(it -> it.itemStack().equals(item)).findFirst();
        inventoryItem.ifPresent(this::removePageItem);
    }

    @Override
    public void open(@NotNull HumanEntity player) {
        open(player, 1);
    }

    public void open(HumanEntity player, int openPage) {
        if (player.isSleeping()) {
            return;
        }

        if (openPage <= pagesNumber() || openPage > 0) {
            pageNumber = openPage;
        }

        getInventory().clear();
        currentPage.clear();

        populate();

        if (pageSize == 0) {
            pageSize = calculatePageSize();
        }

        populatePage();
        player.openInventory(getInventory());
    }

    @Override
    public AbstractInventory updateTitle(@NotNull String title) {
        updating(true);

        var component = TextUtils.toComponent(title);
        updateInventory(Bukkit.createInventory(this, getInventory().getSize(), component));

        var viewers = getInventory().getViewers();
        viewers.forEach(humanEntity -> open(humanEntity, pageNumber()));

        updating(false);
        return this;
    }

    public Map<Integer, InventoryItem> currentPage() {
        return Collections.unmodifiableMap(currentPage);
    }

    public List< InventoryItem> pageItems() {
        return Collections.unmodifiableList(pageItems);
    }

    public int pageNum() {
        return pageNumber;
    }

    public int nextPageNum() {
        if (pageNumber + 1 > pagesNumber()) {
            return pageNumber;
        }

        return pageNumber + 1;
    }

    public int prevPageNum() {
        if (pageNumber - 1 == 0) {
            return pageNumber;
        }

        return pageNumber - 1;
    }

    public boolean next() {
        if (pageNumber + 1 > pagesNumber()) {
            return false;
        }

        pageNumber++;
        updatePage();
        return true;
    }

    public boolean previous() {
        if (pageNumber - 1 == 0) {
            return false;
        }

        pageNumber--;
        updatePage();
        return true;
    }

    public InventoryItem pageItem(int slot) {
        return currentPage.get(slot);
    }

    private List<InventoryItem> pageNumber(final int givenPage) {
        var page = givenPage - 1;
        var guiPage = new ArrayList<InventoryItem>();

        var max = ((page * pageSize) + pageSize);
        if (max > pageItems.size()) {
            max = pageItems.size();
        }

        for (var i = page * pageSize; i < max; i++) {
            guiPage.add(pageItems.get(i));
        }

        return guiPage;
    }

    public int pagesNumber() {
        return (int) Math.ceil((double) pageItems.size() / pageSize);
    }

    private void populatePage() {
        for (var inventoryItem : pageNumber(pageNumber)) {
            for (var slot = 0; slot < rows() * 9; slot++) {
                if (inventoryItem(slot) != null || getInventory().getItem(slot) != null) {
                    continue;
                }

                currentPage.put(slot, inventoryItem);
                getInventory().setItem(slot, inventoryItem.itemStack());

                break;
            }
        }
    }

    protected Map<Integer, InventoryItem> mutableCurrentPageItems() {
        return currentPage;
    }

    protected void clearPage() {
        for (var entry : currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    public void clearPageItems(boolean update) {
        pageItems.clear();

        if (update) {
            update();
        }
    }

    public void clearPageItems() {
        clearPageItems(false);
    }


    protected int pageSize() {
        return pageSize;
    }

    protected int pageNumber() {
        return pageNumber;
    }

    protected void updatePageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    protected void updatePage() {
        clearPage();
        populatePage();
    }

    protected int calculatePageSize() {
        int counter = 0;

        for (var slot = 0; slot < rows() * 9; slot++) {
            if (getInventory().getItem(slot) == null) {
                counter++;
            }
        }

        return counter;
    }

    public void updatePageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
