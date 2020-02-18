package com.github.julyss2019.bukkit.plugins.julyguild.listener;

import com.github.julyss2019.bukkit.plugins.julyguild.JulyGuild;
import com.github.julyss2019.bukkit.plugins.julyguild.guild.GuildMember;
import com.github.julyss2019.bukkit.plugins.julyguild.request.Request;
import com.github.julyss2019.bukkit.plugins.julyguild.request.entities.TpAllRequest;
import com.github.julyss2019.bukkit.plugins.julyguild.util.Util;
import com.github.julyss2019.bukkit.plugins.julyguild.config.setting.MainSettings;
import com.github.julyss2019.bukkit.plugins.julyguild.placeholder.PlaceholderContainer;
import com.github.julyss2019.bukkit.plugins.julyguild.placeholder.PlaceholderText;
import com.github.julyss2019.bukkit.plugins.julyguild.player.GuildPlayer;
import com.github.julyss2019.bukkit.plugins.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.message.Title;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpAllListener implements Listener {
    private JulyGuild plugin = JulyGuild.getInstance();
    private GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private Map<UUID, Long> lastSneakMap = new HashMap<>();
    private Map<UUID, Integer> sneakCountMap = new HashMap<>();
    private ConfigurationSection tpAllLangSection = plugin.getLangYaml().getConfigurationSection("TpAll");

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player bukkitPlayer = event.getPlayer();
        UUID uuid = bukkitPlayer.getUniqueId();
        GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer(bukkitPlayer);

        if (!guildPlayer.isInGuild()) {
            return;
        }

        GuildMember guildMember = guildPlayer.getGuild().getMember(guildPlayer);

        for (Request request : guildMember.getReceivedRequests()) {
            if (!(request instanceof TpAllRequest)) {
                continue;
            }

            TpAllRequest tpAllRequest = (TpAllRequest) request;

            if (!MainSettings.getGuildTpAllReceiveWorlds().contains(bukkitPlayer.getWorld().getName())) {
                Util.sendMsg(bukkitPlayer, tpAllLangSection.getString("no_receive_world"));
                return;
            }

            int sneakCount = sneakCountMap.getOrDefault(uuid, 0);

            if (sneakCount >= MainSettings.getGuildTpAllSneakCount()) {
                GuildMember senderGuilderMember = tpAllRequest.getSender();
                Player senderBukkitPlayer = senderGuilderMember.getGuildPlayer().getBukkitPlayer();

                bukkitPlayer.teleport(senderBukkitPlayer.getLocation());

                if (JulyMessage.canUseTitle()) {
                    new Title.Builder().text(tpAllLangSection.getString("teleported.title")).build().send(bukkitPlayer);
                    new Title.Builder().type(Title.Type.SUBTITLE).text(tpAllLangSection.getString("teleported.subtitle")).build().send(bukkitPlayer);
                } else {
                    Util.sendMsg(bukkitPlayer, tpAllLangSection.getString("teleported.msg"));
                }

                Util.sendMsg(senderBukkitPlayer, tpAllLangSection.getString("teleported.sender_msg"), new PlaceholderContainer().add("member", bukkitPlayer.getName()));
                lastSneakMap.remove(uuid);
                sneakCountMap.remove(uuid);
                request.delete();
            } else {
                if (!lastSneakMap.containsKey(uuid) || (System.currentTimeMillis() - lastSneakMap.get(uuid) < MainSettings.getGuildTpAllSneakCountInterval())) {
                    PlaceholderContainer placeholderContainer = new PlaceholderContainer().add("count", MainSettings.getGuildTpAllSneakCount() - sneakCount);

                    if (JulyMessage.canUseTitle()) {
                        new Title.Builder().text(
                                PlaceholderText.replacePlaceholders(tpAllLangSection.getString("sneak_counter.title"), placeholderContainer)).build().send(bukkitPlayer);
                        new Title.Builder().type(Title.Type.SUBTITLE).text(
                                PlaceholderText.replacePlaceholders(tpAllLangSection.getString("sneak_counter.subtitle"), placeholderContainer)).build().send(bukkitPlayer);
                    } else {
                        Util.sendMsg(bukkitPlayer, tpAllLangSection.getString("sneak_counter.msg"), placeholderContainer);
                    }

                    sneakCountMap.put(uuid, sneakCountMap.getOrDefault(uuid, 0) + 1);
                }

                lastSneakMap.put(uuid, System.currentTimeMillis());
            }

            return;
        }
    }
}