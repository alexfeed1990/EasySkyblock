package me.slashed.easyskyblock.commands

import me.slashed.easyskyblock.EasySkyblock
import me.slashed.easyskyblock.config.Category
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ShopCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(sender is Player) {
            val inventory = Bukkit.createInventory(sender, 27, Component.text("Shop - Categories"))
            sender.openInventory(inventory)

            val categoryList = EasySkyblock.pubConfig.getList("shop.categories")
            val length = categoryList!!.size
            var currentPos = 0;
            for(category in categoryList) {
                if(category == null || category !is Map<*, *>) continue
                val cat = Category.deserialize(category as Map<String?, Any?>)
                val categoryButton = ItemStack(cat.block)
                val categoryButtonMeta = categoryButton.itemMeta

                categoryButtonMeta.displayName(Component.text(cat.name))
                categoryButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "category_id"), PersistentDataType.INTEGER, currentPos)

                val lore = java.util.ArrayList<Component>()
                lore.add(Component.text(cat.description))
                categoryButtonMeta.lore(lore)

                categoryButton.itemMeta = categoryButtonMeta

                inventory.setItem(13 - (length / 2) + currentPos++, categoryButton)
            }
            EasySkyblock.guiMap[sender.uniqueId] = inventory
        }
        return false
    }
}