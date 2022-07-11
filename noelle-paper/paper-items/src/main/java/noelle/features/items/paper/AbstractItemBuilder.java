package noelle.features.items.paper;

import de.tr7zw.nbtapi.NBTItem;

import noelle.features.utils.common.text.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class AbstractItemBuilder<I extends AbstractItemBuilder<I>> {
    public static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS
    );

    private ItemStack stack;
    private ItemMeta meta;

    private final NBTItem nbtItem;

    protected AbstractItemBuilder(@NotNull ItemStack stack) {
        this.stack = stack;
        this.meta = stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(stack.getType());

        this.nbtItem = new NBTItem(stack);
    }

    protected ItemMeta meta() {
        return meta;
    }

    protected void updateMeta(ItemMeta meta) {
        this.meta = meta;
    }

    public I name(String name) {
        if (meta != null) {
            var componentName = TextUtils.toComponent(name);
            meta.displayName(componentName);
        }

        return (I) this;
    }

    public I amount(int amount) {
        stack.setAmount(amount);
        return (I) this;
    }

    public I lore(String... lores) {
        return lore(Arrays.asList(lores));
    }

    public I lore(List<String> lores) {
        if (meta != null) {
            var componentList = lores.stream().map(TextUtils::toComponent).collect(Collectors.toList());
            meta.lore(componentList);
        }

        return (I) this;
    }

    public I enchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return (I) this;
    }

    public I enchant(Enchantment enchantment, int level) {
        return enchant(enchantment, level, true);
    }

    public I enchant(Enchantment enchantment) {
        return enchant(enchantment, 1, true);
    }

    public I enchant(@NotNull Map<Enchantment, Integer> enchantments, boolean ignoreLevelRestriction) {
        enchantments.forEach((enchantment, level) -> enchant(enchantment, level, ignoreLevelRestriction));
        return (I) this;
    }

    public I enchant(@NotNull Map<Enchantment, Integer> enchantments) {
        return enchant(enchantments, true);
    }

    public I flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return (I) this;
    }

    public I unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return (I) this;
    }

    public I glow(boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            return (I) this;
        }

        for (var enchantment : meta.getEnchants().keySet()) {
            meta.removeEnchant(enchantment);
        }

        return (I) this;
    }

    public I model(int modelData) {
        meta.setCustomModelData(modelData);
        return (I) this;
    }

    public I color(Color color) {
        if (LEATHER_ARMOR.contains(stack.getType())) {
            var leatherArmorMeta = (LeatherArmorMeta) meta;

            leatherArmorMeta.setColor(color);

            updateMeta(leatherArmorMeta);
        }

        return (I) this;
    }

    public I addNbtValue(String compoundName, String key, String value) {
        stack.setItemMeta(meta);

        var nbtCompound = nbtItem.addCompound(compoundName);
        nbtCompound.setString(key, value);

        stack = nbtItem.getItem();
        return (I) this;
    }

    public I addNbtValue(String compoundName, String key, Boolean value) {
        stack.setItemMeta(meta);

        var nbtCompound = nbtItem.addCompound(compoundName);
        nbtCompound.setBoolean(key, value);

        stack = nbtItem.getItem();
        return (I) this;
    }

    public I removeNbtValue(String compoundName, String key) {
        stack.setItemMeta(meta);

        var nbtCompound = nbtItem.getCompound(compoundName);
        nbtCompound.removeKey(key);

        stack = nbtItem.getItem();
        return (I) this;
    }

    public ItemStack buildItem() {
        stack.setItemMeta(meta);
        return stack;
    }
}
