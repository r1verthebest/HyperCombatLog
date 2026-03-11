package me.r1ver.combat.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CombatManager {

	private final Map<UUID, Long> tagging = new ConcurrentHashMap<>();

	private final long combatTimeMillis = TimeUnit.SECONDS.toMillis(15);

	public void tagPlayer(UUID uuid) {
		tagging.put(uuid, System.currentTimeMillis() + combatTimeMillis);
	}

	public boolean isInCombat(UUID uuid) {
		Long expireTime = tagging.get(uuid);

		if (expireTime == null)
			return false;

		if (System.currentTimeMillis() > expireTime) {
			tagging.remove(uuid);
			return false;
		}

		return true;
	}

	public long getRemainingSeconds(UUID uuid) {
		long remaining = tagging.getOrDefault(uuid, 0L) - System.currentTimeMillis();
		return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(remaining));
	}
}