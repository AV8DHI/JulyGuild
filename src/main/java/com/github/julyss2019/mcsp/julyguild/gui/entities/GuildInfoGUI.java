package com.github.julyss2019.mcsp.julyguild.gui.entities;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.gui.IndexConfigGUI;
import com.github.julyss2019.mcsp.julyguild.config.gui.item.GUIItemManager;
import com.github.julyss2019.mcsp.julyguild.gui.*;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.Permission;
import com.github.julyss2019.mcsp.julyguild.placeholder.PlaceholderContainer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.request.entities.JoinRequest;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;


/**
 * 查看公会成员，申请加入公会
 */
public class GuildInfoGUI extends BasePlayerGUI {
    private final Player bukkitPlayer;
    private final Guild guild;
    private final JulyGuild plugin = JulyGuild.getInstance();
    private final ConfigurationSection thisGUISection = plugin.getGUIYaml("GuildInfoGUI");
    private final ConfigurationSection thisLangSection = plugin.getLangYaml().getConfigurationSection("GuildInfoGUI");

    public GuildInfoGUI(@Nullable GUI lastGUI, @NotNull GuildPlayer guildPlayer, @NotNull Guild guild) {
        super(lastGUI, GUIType.INFO, guildPlayer);

        this.bukkitPlayer = guildPlayer.getBukkitPlayer();
        this.guild = guild;
    }

    @Override
    public Inventory createInventory() {
        IndexConfigGUI.Builder guiBuilder = new IndexConfigGUI.Builder()
                .fromConfig(thisGUISection, bukkitPlayer, new PlaceholderContainer().addGuildPlaceholders(guild))
                .item(GUIItemManager.getIndexItem(thisGUISection.getConfigurationSection("items.request_join"), bukkitPlayer, new PlaceholderContainer().addGuildPlaceholders(guild)), new ItemListener() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        close();

                        if (guild.getMemberCount() >= guild.getMaxMemberCount()) {
                            Util.sendMsg(bukkitPlayer, thisLangSection.getString("request_join.guild_full"));
                            return;
                        }

                        for (JoinRequest joinRequest : guildPlayer.getSentRequests().stream().filter(request -> request instanceof JoinRequest).map(request -> (JoinRequest) request).collect(Collectors.toList())) {
                            if (joinRequest.getReceiver().equals(guild)) {
                                Util.sendMsg(bukkitPlayer, thisLangSection.getString("request_join.already_have"));
                                return;
                            }
                        }

                        new JoinRequest(guildPlayer, guild).send();

                        guild.getMembers().stream().filter(guildMember -> guildMember.hasPermission(Permission.PLAYER_JOIN_CHECK)).forEach(guildMember -> {
                            Util.sendMsg(bukkitPlayer, "request_join.received");
                        });

                        Util.sendMsg(bukkitPlayer, thisLangSection.getString("request_join.success"));
                    }
                }).item(GUIItemManager.getIndexItem(thisGUISection.getConfigurationSection("items.members"), bukkitPlayer, new PlaceholderContainer().addGuildPlaceholders(guild)), new ItemListener() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        close();
                        new GuildMemberListGUI(GuildInfoGUI.this, guild, guildPlayer).open();
                    }
                }).item(GUIItemManager.getIndexItem(thisGUISection.getConfigurationSection("items.back"), bukkitPlayer, new PlaceholderContainer().addGuildPlaceholders(guild)), new ItemListener() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        if (canBack()) {
                            back();
                        }
                    }
                });

        return guiBuilder.build();
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public boolean canUse() {
        return guild.isValid();
    }
}
