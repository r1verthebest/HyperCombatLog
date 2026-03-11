package me.r1ver.combat;

import lombok.Getter;
import me.r1ver.combat.commands.StatsCommand;
import me.r1ver.combat.database.DatabaseService;
import me.r1ver.combat.database.MySQLPlayerDAO;
import me.r1ver.combat.hologram.HologramManager;
import me.r1ver.combat.listeners.CombatActionListener;
import me.r1ver.combat.listeners.CombatListener;
import me.r1ver.combat.manager.CombatManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

@Getter
public class HyperCombatLog extends JavaPlugin {

	@Getter
	private static HyperCombatLog instance;

	private CombatManager combatManager;
	private DatabaseService dbService;
	private HologramManager hologramManager;
	private PlayerDAO playerDAO;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		this.combatManager = new CombatManager();
		this.hologramManager = new HologramManager();
		if (!setupDatabase()) {
			getLogger().severe("§cFalha ao configurar banco de dados. Desligando...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		registerCommands();
		registerListeners();
		startCombatTask();

		getLogger().info("§a[HyperCombatLog] Plugin ativado com sucesso!");
	}

	@Override
	public void onDisable() {
		if (hologramManager != null) {
			Bukkit.getOnlinePlayers().forEach(p -> hologramManager.removeHologram(p.getUniqueId()));
		}
		if (dbService != null) {
			dbService.close();
		}
	}

	private boolean setupDatabase() {
		this.dbService = new DatabaseService();

		String host = getConfig().getString("database.host");
		int port = getConfig().getInt("database.port");
		String dbName = getConfig().getString("database.database");
		String user = getConfig().getString("database.username");
		String pass = getConfig().getString("database.password");

		dbService.connect(host, port, dbName, user, pass);

		try {
			MySQLPlayerDAO mysqlDAO = new MySQLPlayerDAO(dbService.getConnection());
			mysqlDAO.createTables();
			this.playerDAO = mysqlDAO;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void registerCommands() {
		getCommand("combatstats").setExecutor(new StatsCommand(playerDAO));
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new CombatListener(combatManager, playerDAO, hologramManager),
				this);
		getServer().getPluginManager().registerEvents(new CombatActionListener(combatManager), this);
	}

	private void startCombatTask() {
		getServer().getScheduler().runTaskTimer(this, () -> {
			for (Player p : Bukkit.getOnlinePlayers()) {
				UUID uuid = p.getUniqueId();

				if (combatManager.isInCombat(uuid)) {
					updateCombatVisuals(p);
				} else {
					hologramManager.removeHologram(uuid);
				}
			}
		}, 20L, 20L);
	}

	private void updateCombatVisuals(Player p) {
		int remaining = (int) combatManager.getRemainingSeconds(p.getUniqueId());
		int total = 15;

		String bar = getProgressBar(remaining, total);
		String message = "§e§lCOMBATE §f" + bar + " §6" + remaining + "s";

		p.spigot().sendMessage(TextComponent.fromLegacyText(message));

		hologramManager.createHologram(p);
		hologramManager.moveHologram(p.getUniqueId(), p.getLocation());
	}

	private String getProgressBar(int current, int total) {
		int barCount = 20;
		int greenBars = (int) ((double) current / total * barCount);
		int redBars = Math.max(0, barCount - greenBars);

		StringBuilder bar = new StringBuilder("§7[");
		for (int i = 0; i < greenBars; i++)
			bar.append("§a|");
		for (int i = 0; i < redBars; i++)
			bar.append("§c|");
		bar.append("§7]");

		return bar.toString();
	}
}