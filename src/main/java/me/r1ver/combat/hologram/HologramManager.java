package me.r1ver.combat.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager {

	private final Map<UUID, ArmorStand> activeHolograms = new ConcurrentHashMap<>();

	public void createHologram(org.bukkit.entity.Player player) {
		if (activeHolograms.containsKey(player.getUniqueId()))
			return;
		Location loc = player.getLocation().add(0, 2.3, 0);

		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

		as.setVisible(false);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomName("§c§lEM COMBATE");
		as.setCustomNameVisible(true);
		as.setMarker(true);

		activeHolograms.put(player.getUniqueId(), as);
	}

	public void moveHologram(UUID uuid, Location loc) {
		ArmorStand as = activeHolograms.get(uuid);
		if (as != null) {
			as.teleport(loc.add(0, 2.3, 0));
		}
	}

	public void removeHologram(UUID uuid) {
		ArmorStand as = activeHolograms.remove(uuid);
		if (as != null) {
			as.remove();
		}
	}
}