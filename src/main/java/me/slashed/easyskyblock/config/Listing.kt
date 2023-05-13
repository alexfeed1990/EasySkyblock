package me.slashed.easyskyblock.config

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

@SerializableAs("Listing")
class Listing (
    val price: Double,
    val sellable: Boolean,
    val item: Material
) : ConfigurationSerializable, Cloneable {
    override fun serialize(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap<String, Any>()
        map.put("price", this.price)
        map.put("bulk", this.sellable)
        map.put("item", this.item)
        return map
    }
    companion object {
        fun deserialize(args: Map<String?, Any?>): Listing {
            var price: Double = 0.0;
            var sellable = true;
            var item = Material.GRAY_STAINED_GLASS_PANE

            if (args.containsKey("price")) {
                price = (args["price"] as Number?)!!.toDouble()
            }
            if (args.containsKey("bulk")) {
                sellable = (args["bulk"] as Boolean?)!!
            }
            if (args.containsKey("block")) {
                item = Material.getMaterial((args["item"] as String?)!!)!!
            }
            return Listing(price, sellable, item)
        }
    }
}