package me.slashed.easyskyblock.config

import me.slashed.easyskyblock.EasySkyblock
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class Page(plugin: EasySkyblock) : InventoryHolder {
    private var inventory: Inventory? = null

    private var pageListings = mutableMapOf<Int, List<Listing>>()
    private var invPages = mutableMapOf<Int, Inventory>()
    private var pages = 0

    fun MyInventory(plugin: EasySkyblock) {
        inventory = plugin.server.createInventory(this, 27)
    }

    fun addListings(listings: List<Listing>) {
        var currentList = mutableListOf<Listing>()
        var iterator = 0
        var doubleIterator = 1
        for(li in listings) {
            if(iterator >= listings.size) break
            // currentList.add(listings[iterator * doubleIterator])
            currentList.add(li)
            if(iterator == 13) {
                pageListings[doubleIterator] = currentList
                currentList = mutableListOf<Listing>()

                iterator = 0
                doubleIterator++
                continue
            }
            iterator++
        }

        updateInv(EasySkyblock())
    }

    // god this was absolute pain to write
    private fun updateInv(plugin: EasySkyblock) {
        for(i in 1..pageListings.size) {
            val currentListings = pageListings[i]
            var currentInv = invPages[i]
            currentInv = plugin.server.createInventory(this, 27)

            if(pageListings.size > 2 && i == 1) {
                var nextArrow = ItemStack(Material.ARROW)
                var nextArrowMeta = nextArrow.itemMeta
                var backButton = ItemStack(Material.BARRIER)
                var backButtonMeta = backButton.itemMeta

                /*
                    1: other page
                    2: next page
                    3: back button (back to categories)
                 */
                nextArrowMeta.displayName(Component.text("Next page"))
                nextArrowMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 2)

                backButtonMeta.displayName(Component.text("Go back"))
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 3)
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page"), PersistentDataType.INTEGER, i)

                nextArrow.itemMeta = nextArrowMeta
                backButton.itemMeta = backButtonMeta
                currentInv.setItem(27, nextArrow)
                currentInv.setItem(15, backButton)
            }
            else if (pageListings.size > 3 && i != 1 && i != pageListings.size) {
                var nextArrow = ItemStack(Material.ARROW)
                var nextArrowMeta = nextArrow.itemMeta
                var backArrow = ItemStack(Material.ARROW)
                var backArrowMeta = nextArrow.itemMeta
                var backButton = ItemStack(Material.BARRIER)
                var backButtonMeta = backButton.itemMeta

                /*
                    1: other page
                    2: next page
                    3: back button (back to categories)
                 */
                nextArrowMeta.displayName(Component.text("Next page"))
                nextArrowMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 2)

                backArrowMeta.displayName(Component.text("Other page"))
                backArrowMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 1)

                backButtonMeta.displayName(Component.text("Go back"))
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 3)
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page"), PersistentDataType.INTEGER, i)

                nextArrow.itemMeta = nextArrowMeta
                backArrow.itemMeta = backArrowMeta
                backButton.itemMeta = backButtonMeta

                currentInv.setItem(27, nextArrow)
                currentInv.setItem(15, backArrow)
                currentInv.setItem(16, backButton)
            }
            else if (pageListings.size == 1) {
                var backButton = ItemStack(Material.BARRIER)
                var backButtonMeta = backButton.itemMeta

                /*
                    1: other page
                    2: next page
                    3: back button (back to categories)
                 */
                backButtonMeta.displayName(Component.text("Go back"))
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page_button"), PersistentDataType.INTEGER, 3)
                backButtonMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "page"), PersistentDataType.INTEGER, i)

                backButton.itemMeta = backButtonMeta
                currentInv.setItem(15, backButton)
            }

            for(j in 1..14) {
                val listing = currentListings!![j]
                var itemStack = ItemStack(listing.item)
                var itemMeta = itemStack.itemMeta

                itemMeta.displayName(Component.text(listing.price.toString() + "$ - TEST"))
                itemMeta.persistentDataContainer.set(NamespacedKey("EasySkyblock", "listing_id"), PersistentDataType.INTEGER, j)

                itemStack.itemMeta = itemMeta
                currentInv.setItem(j, itemStack)
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        val item = event.currentItem!!
        val itemPDC = item.itemMeta.persistentDataContainer
        val key = NamespacedKey("EasySkyblock", "page_button")
        val pageKey = NamespacedKey("EasySkyblock", "page")
        if (inventory.holder !is Page) return

        /*
            1: other page
            2: next page
            3: back button (back to categories)
         */
        if(item.type == Material.ARROW && itemPDC.get(key, PersistentDataType.INTEGER) == 1) {
            val page = itemPDC.get(pageKey, PersistentDataType.INTEGER)
            event.whoClicked.openInventory(invPages[page!! - 1]!!)
        }
        else if (item.type == Material.ARROW && itemPDC.get(key, PersistentDataType.INTEGER) == 2) {
            val page = itemPDC.get(pageKey, PersistentDataType.INTEGER)
            event.whoClicked.openInventory(invPages[page!! + 1]!!)
        }
        else if (item.type == Material.BARRIER && itemPDC.get(key, PersistentDataType.INTEGER) == 3) {
            val inv = Bukkit.createInventory(event.whoClicked, 27, Component.text("Shop - Categories"))
            event.whoClicked.openInventory(inv)

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

                inv.setItem(13 - (length / 2) + currentPos++, categoryButton)
            }
            EasySkyblock.guiMap[event.whoClicked.uniqueId] = inv
        }
    }

    override fun getInventory(): Inventory? {
        return inventory
    }
}


