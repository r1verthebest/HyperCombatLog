package me.r1ver.combat.listeners;

import lombok.RequiredArgsConstructor;
import me.r1ver.combat.PlayerDAO;
import me.r1ver.combat.hologram.HologramManager;
import me.r1ver.combat.manager.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CombatListener implements Listener {

	private final CombatManager combatManager;
	private final PlayerDAO playerDAO;
	private final HologramManager hologramManager;

	private final List<String> allowedCommands = Arrays.asList("/tell", "/r", "/report", "/help");

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombat(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
			return;
		}

		Player victim = (Player) event.getEntity();
		Player attacker = (Player) event.getDamager();

		handleTag(victim);
		handleTag(attacker);
	}

	private void handleTag(Player player) {
		if (!combatManager.isInCombat(player.getUniqueId())) {
			player.sendMessage("§c§l[!] §cVocê entrou em combate! Não deslogue.");
			hologramManager.createHologram(player);
		}
		combatManager.tagPlayer(player.getUniqueId());
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (!combatManager.isInCombat(player.getUniqueId()))
			return;

		String cmd = event.getMessage().split(" ")[0].toLowerCase();

		if (!allowedCommands.contains(cmd)) {
			event.setCancelled(true);
			player.sendMessage("§c§l[!] §cEste comando está bloqueado em combate!");
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (combatManager.isInCombat(player.getUniqueId())) {
			player.setHealth(0);
			playerDAO.incrementLogouts(player.getUniqueId());
			hologramManager.removeHologram(player.getUniqueId());

			Bukkit.broadcastMessage("§8[§4§lX§8] §e" + player.getName() + " §cdeslogou em combate e foi punido!");
		}
	}
}