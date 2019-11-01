package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGUI;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGUIItem;
import com.github.julyss2019.mcsp.julyguild.config.MainConfig;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.CacheGuildManager;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.OwnedIcon;
import com.github.julyss2019.mcsp.julyguild.placeholder.Placeholder;
import com.github.julyss2019.mcsp.julyguild.placeholder.PlaceholderText;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatListener;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 主GUI
 * @version 1.0.0
 */
public class MainGUI extends BasePageableGUI {
    private Inventory inventory;
    private List<Guild> guilds = new ArrayList<>();

    private final JulyGuild plugin = JulyGuild.getInstance();
    private final ConfigurationSection thisGUISection = plugin.getGuiYamlConfig().getConfigurationSection("MainGUI");
    private final ConfigurationSection thisLangSection = plugin.getLangYamlConfig().getConfigurationSection("MainGUI");
    private final CacheGuildManager cacheGuildManager = plugin.getCacheGuildManager();

    public MainGUI(GuildPlayer guildPlayer) {
        super(GUIType.MAIN, guildPlayer);

        guilds.addAll(cacheGuildManager.getSortedGuilds());
        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        ConfigGUI.Builder guiBuilder = (ConfigGUI.Builder) new ConfigGUI.Builder()
                .title(PlaceholderText.replacePlaceholders(thisGUISection.getString("title"), new Placeholder.Builder().add("%PAGE%", String.valueOf(getCurrentPage() + 1)).build()))
                .row(6)
                .colored()
                .listener(new InventoryListener() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        int index = event.getSlot() + getCurrentPage() * 43;

                        if (index < guilds.size()) {
                            Guild guild = guilds.get(index);
                            GUI newGUI = new GuildInfoGUI(guildPlayer, guild, getCurrentPage());

                            close();
                            guildPlayer.setUsingGUI(newGUI);
                            newGUI.open();
                        }
                    }
                });


        // 如果大于1页则提供翻页按钮
        if (getTotalPage() > 1) {
            guiBuilder
                    .item(4, 7, CommonItem.PREVIOUS_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasPrecious()) {
                                close();
                                previousPage();
                                open();
                            }
                        }
                    })
                    .item(4, 8, CommonItem.NEXT_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasNext()) {
                                close();
                                nextPage();
                                open();
                            }
                        }
                    });
        }

        if (guildPlayer.isInGuild()) {
            guiBuilder.item(ConfigGUIItem.getIndexItem(thisGUISection.getConfigurationSection("items.my_guild"), new Placeholder.Builder().add("%PLAYER%", playerName).build()), new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    close();
                    new GuildMineGUI(guildPlayer).open();
                }
            });
        } else {
            guiBuilder.item(ConfigGUIItem.getIndexItem(thisGUISection.getConfigurationSection("items.create_guild")), new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    close();
                    Util.sendColoredMessage(bukkitPlayer, thisLangSection.getString("create.input"));
                    JulyChatFilter.registerChatFilter(bukkitPlayer, new ChatListener() {
                        @Override
                        public void onChat(AsyncPlayerChatEvent event) {
                            event.setCancelled(true);
                            JulyChatFilter.unregisterChatFilter(bukkitPlayer);

                            String guildName = ChatColor.translateAlternateColorCodes('&', event.getMessage());

                            if (!guildName.matches(MainConfig.getCreateNameRegex())) {
                                Util.sendColoredMessage(bukkitPlayer, thisLangSection.getString("create.regex_not_match"));
                                return;
                            }

                            if (guildName.contains("§") && !bukkitPlayer.hasPermission("JulyGuild.create.colored")) {
                                Util.sendColoredMessage(bukkitPlayer, thisLangSection.getString("create.no_colored_name_permission"));
                                return;
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    close();
                                    new GuildCreateGUI(guildPlayer, guildName).open();
                                    JulyChatFilter.unregisterChatFilter(bukkitPlayer);
                                }
                            }.runTaskLater(plugin, 1L);

                            event.setCancelled(true);
                        }
                    });
                }
            });
        }

        int guildSize = guilds.size();
        int itemCounter = page * 43;
        int loopCount = guildSize - itemCounter < 43 ? guildSize - itemCounter : 43;

        // 公会图标
        for (int i = 0; i < loopCount; i++) {
            Guild guild = guilds.get(itemCounter++);
            OwnedIcon ownedIcon = guild.getIcon();
            ItemBuilder itemBuilder = new ItemBuilder()
                    .material(ownedIcon.getMaterial())
                    .data(ownedIcon.getData())
                    .insertLore(0, ownedIcon.getFirstLore())
                    .displayName(PlaceholderText.replacePlaceholders(thisGUISection.getString("items._guild.display_name"), bukkitPlayer))
                    .lores(PlaceholderText.replacePlaceholders(thisGUISection.getStringList("items._guild.lores"), bukkitPlayer))
            ;

            guiBuilder.item(i, itemBuilder.build());
        }

        this.inventory = guiBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int guildSize = guilds.size();

        return guildSize == 0 ? 1 : guildSize % 43 == 0 ? guildSize / 43 : guildSize / 43 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}