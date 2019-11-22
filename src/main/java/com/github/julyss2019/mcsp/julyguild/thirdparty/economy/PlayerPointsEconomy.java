package com.github.julyss2019.mcsp.julyguild.thirdparty.economy;

import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsEconomy {
    private PlayerPointsAPI playerPointsAPI;

    public PlayerPointsEconomy(PlayerPointsAPI playerPointsAPI) {
        this.playerPointsAPI = playerPointsAPI;
    }


    public boolean has(Player player, int amount) {
        return getBalance(player) >= amount;
    }

    public void withdraw(Player player, int amount) {
        playerPointsAPI.take(player.getUniqueId(), amount);
    }

    public void deposit(Player player, int amount) {
        playerPointsAPI.give(player.getUniqueId(), amount);
    }

    public int getBalance(Player player) {
        return playerPointsAPI.look(player.getUniqueId());
    }
}
