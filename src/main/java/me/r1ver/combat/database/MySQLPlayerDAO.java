package me.r1ver.combat.database;

import lombok.RequiredArgsConstructor;
import me.r1ver.combat.PlayerDAO;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MySQLPlayerDAO implements PlayerDAO {

    private final Connection connection;

    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS combat_stats (" +
                     "uuid VARCHAR(36) PRIMARY KEY, " +
                     "logouts INT DEFAULT 0" +
                     ");";
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void incrementLogouts(UUID uuid) {
        CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO combat_stats (uuid, logouts) VALUES (?, 1) " +
                         "ON DUPLICATE KEY UPDATE logouts = logouts + 1";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, uuid.toString());
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Integer> getLogouts(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT logouts FROM combat_stats WHERE uuid = ?";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, uuid.toString());
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) return rs.getInt("logouts");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}