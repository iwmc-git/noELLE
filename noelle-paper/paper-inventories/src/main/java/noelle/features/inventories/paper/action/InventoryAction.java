package noelle.features.inventories.paper.action;

import org.bukkit.event.Event;

@FunctionalInterface
public interface InventoryAction<E extends Event> {
    void execute(E event);
}
