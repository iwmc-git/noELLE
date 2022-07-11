package noelle.features.inventories.paper.builder;

import noelle.features.inventories.paper.PagedInventory;

import org.jetbrains.annotations.NotNull;

public class PagedInventoryBuilder extends AbstractInventoryBuilder<PagedInventory, PagedInventoryBuilder> {
    private final int pageSize;

    public PagedInventoryBuilder(String title, int pageSize) {
        super(title);

        this.pageSize = pageSize;
    }

    public static @NotNull PagedInventoryBuilder inventoryBuilder(String title, int pageSize) {
        return new PagedInventoryBuilder(title, pageSize);
    }

    @Override
    public PagedInventory buildInventory() {
        var inventory = new PagedInventory(rows(), pageSize, title(), interactionModifiers());

        var consumer = consumer();
        if (consumer != null) consumer.accept(inventory);

        return inventory;
    }
}
