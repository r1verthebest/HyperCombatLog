package me.r1ver.combat.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.r1ver.combat.manager.CombatManager;

public class CombatActionListener implements Listener {

	private final CombatManager combatManager;
	
	public CombatActionListener(CombatManager combatManager) {
	    this.combatManager = combatManager;
	}

	@EventHandler
	public void onPearl(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {
			if (combatManager.isInCombat(player.getUniqueId())) {
				event.setCancelled(true);
				player.sendMessage("§c§l[!] §cVocê não pode usar pérolas em combate!");
				player.updateInventory();
			}
		}
	}

}
