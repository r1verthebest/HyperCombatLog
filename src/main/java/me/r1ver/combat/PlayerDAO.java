package me.r1ver.combat;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerDAO {
    void incrementLogouts(UUID uuid);
    CompletableFuture<Integer> getLogouts(UUID uuid);
}