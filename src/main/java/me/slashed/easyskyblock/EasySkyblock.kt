package me.slashed.easyskyblock

import me.slashed.easyskyblock.commands.ShopCommand
import me.slashed.easyskyblock.listeners.InventoryClickListener
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import org.yaml.snakeyaml.Yaml
import java.util.*

class EasySkyblock : JavaPlugin() {
    var LOGGER = logger;
    private var config = getConfig()

    companion object {
        val guiMap: MutableMap<UUID, Inventory> = mutableMapOf()
        lateinit var pubConfig: FileConfiguration;
        lateinit var pubLogger: java.util.logging.Logger;
    }

    override fun onEnable() {
        // Plugin startup login
        config.options().copyDefaults(true)

        saveConfig()

        pubConfig = config
        pubLogger = logger

        logger.info("Easy Skyblock loaded!")

        // Yea this is how you get the size of the array.
        val testList = config.getList("shop.items") as MutableList<*>
        logger.info(testList.size.toString())

        logger.info("Config loaded!")
        registerCommands()
        registerListeners()
    }

    private fun setShopConfig() {

    }

    private fun registerCommands() {
        getCommand("shop")?.setExecutor(ShopCommand())
        logger.info("Registered commands.")
    }

    private fun registerListeners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(), this)
        logger.info("Registered Listeners.")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}