package me.slashed.easyskyblock.config

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.util.BoundingBox

@SerializableAs("Category")
class Category (
    val name: String,
    val description: String,
    val block: Material,
    val listings: List<Listing>
) : ConfigurationSerializable, Cloneable {
    override fun serialize(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map.put("name", this.name)
        map.put("description", this.description)
        map.put("block", this.block)
        map.put("listings", this.listings)
        return map
    }
    companion object {
        fun deserialize(args: Map<String?, Any?>): Category {
            var name = "";
            var description = "";
            var block = Material.GRAY_STAINED_GLASS_PANE
            var listings: List<Listing> = listOf<Listing>()

            if (args.containsKey("name")) {
                name = (args["name"] as String?)!!.toString()
            }
            if (args.containsKey("description")) {
                description = (args["description"] as String?)!!.toString()
            }
            if (args.containsKey("block")) {
                block = Material.getMaterial((args["block"] as String?)!!)!!
            }
            if (args.containsKey("listings")) {
                listings = (args["listings"] as List<Map<String?, Any?>>?)!!.map { Listing.deserialize(it) }
            }
            return Category(name, description, block, listings)
        }
    }
}