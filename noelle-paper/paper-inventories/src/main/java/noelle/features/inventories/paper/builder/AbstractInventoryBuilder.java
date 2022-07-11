package noelle.features.inventories.paper.builder;

import net.kyori.adventure.text.Component;

import noelle.features.inventories.paper.AbstractInventory;
import noelle.features.inventories.paper.modifier.InteractionModifier;
import noelle.features.utils.common.text.TextUtils;

import java.util.EnumSet;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class AbstractInventoryBuilder<I extends AbstractInventory, B extends AbstractInventoryBuilder<I, B>> {
    private final EnumSet<InteractionModifier> interactionModifiers = EnumSet.noneOf(InteractionModifier.class);
    private final Component title;

    private int rows;
    private Consumer<I> consumer;

    public AbstractInventoryBuilder(String title) {
        this.title = TextUtils.toComponent(title);
    }

    public B rows(int rows) {
        this.rows = rows;
        return (B) this;
    }

    public B apply(Consumer<I> consumer) {
        this.consumer = consumer;
        return (B) this;
    }

    public B disableItemPlace() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_PLACE);
        return (B) this;
    }

    public B disableItemTake() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_TAKE);
        return (B) this;
    }

    public B disableItemSwap() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_SWAP);
        return (B) this;
    }

    public B disableItemDrop() {
        interactionModifiers.add(InteractionModifier.PREVENT_ITEM_DROP);
        return (B) this;
    }

    public B disableOtherActions() {
        interactionModifiers.add(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return (B) this;
    }

    public B enableItemPlace() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_PLACE);
        return (B) this;
    }

    public B enableItemTake() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_TAKE);
        return (B) this;
    }

    public B enableItemSwap() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_SWAP);
        return (B) this;
    }

    public B enableItemDrop() {
        interactionModifiers.remove(InteractionModifier.PREVENT_ITEM_DROP);
        return (B) this;
    }

    public B enableOtherActions() {
        interactionModifiers.remove(InteractionModifier.PREVENT_OTHER_ACTIONS);
        return (B) this;
    }

    public B disableAllInteractions() {
        interactionModifiers.addAll(InteractionModifier.VALUES);
        return (B) this;
    }

    public B enableAllInteractions() {
        interactionModifiers.clear();
        return (B) this;
    }

    public int rows() {
        return rows;
    }

    public Component title() {
        return title;
    }

    public Consumer<I> consumer() {
        return consumer;
    }

    public EnumSet<InteractionModifier> interactionModifiers() {
        return interactionModifiers;
    }

    public abstract I buildInventory();
}
