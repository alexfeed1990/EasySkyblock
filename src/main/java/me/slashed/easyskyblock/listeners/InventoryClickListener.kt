package me.slashed.easyskyblock.listeners

import me.slashed.easyskyblock.EasySkyblock
import me.slashed.easyskyblock.config.Category
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class InventoryClickListener : Listener {
    @EventHandler
    fun inventoryClickEvent(event: InventoryClickEvent) {
        val playerUUID = event.whoClicked.uniqueId
        if(event.inventory == EasySkyblock.guiMap[playerUUID]) {
            val key = NamespacedKey("EasySkyblock", "category_id")
            val container = event.currentItem!!.itemMeta.persistentDataContainer
            val value = container.get(key, PersistentDataType.INTEGER)

            if(container.has(key, PersistentDataType.INTEGER)) {
                EasySkyblock.pubLogger.info(value.toString())

                val path = "shop.categories.$value"
                val categoryConfig = EasySkyblock.pubConfig.getList(path)
                if(categoryConfig == null || categoryConfig !is Map<*, *>) return
                val category = Category.deserialize(categoryConfig as Map<String?, Any?>)

                val inventory = Bukkit.createInventory(event.whoClicked, 27, Component.text("Shop - " + category.name))
                event.whoClicked.openInventory(inventory)

                val TestCategoryButton = ItemStack(Material.COBBLESTONE)

                inventory.setItem(13, TestCategoryButton)
                EasySkyblock.guiMap[playerUUID] = inventory
            }

            event.isCancelled = true;
        }
    }
}