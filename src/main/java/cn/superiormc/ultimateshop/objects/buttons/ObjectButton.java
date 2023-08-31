package cn.superiormc.ultimateshop.objects.buttons;

import cn.superiormc.ultimateshop.objects.items.ObjectAction;
import cn.superiormc.ultimateshop.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ObjectButton extends AbstractButton {

    public ObjectButton(ConfigurationSection config) {
        super(config);
        this.type = ButtonType.COMMON;
    }

    @Override
    public void clickEvent(ClickType type, Player player) {
        ObjectAction tempVal1 = new ObjectAction(config.getStringList("actions"));
        tempVal1.doAction(player, 1);
    }

    @Override
    public ItemStack getDisplayItem(Player player) {
        ConfigurationSection tempVal1 = config.getConfigurationSection("display-item");
        if (tempVal1 == null) {
            return new ItemStack(Material.BEDROCK);
        }
        return ItemUtil.buildItemStack(tempVal1);
    }
}