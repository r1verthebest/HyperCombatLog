package me.r1ver.combat.commands;

import lombok.RequiredArgsConstructor;
import me.r1ver.combat.PlayerDAO;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class StatsCommand implements CommandExecutor {

	private final PlayerDAO playerDAO;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cApenas jogadores podem ver estatísticas de combate.");
			return true;
		}

		Player player = (Player) sender;
		player.sendMessage("§e[!] Consultando seus registros no banco de dados...");
		playerDAO.getLogouts(player.getUniqueId()).thenAccept(logouts -> {
			player.sendMessage(" ");
			player.sendMessage(" §6§lESTATÍSTICAS DE COMBATE");
			player.sendMessage(" ");
			player.sendMessage(" §fCombat Logouts: §c" + logouts);
			player.sendMessage(" §7(Vezes que você deslogou em combate)");
			player.sendMessage(" ");
		});

		return true;
	}
}