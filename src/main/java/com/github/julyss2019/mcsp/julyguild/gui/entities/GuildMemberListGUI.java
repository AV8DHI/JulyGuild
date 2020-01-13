package com.github.julyss2019.mcsp.julyguild.gui.entities;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.gui.IndexConfigGUI;
import com.github.julyss2019.mcsp.julyguild.config.gui.item.GUIItemManager;
import com.github.julyss2019.mcsp.julyguild.gui.BasePlayerPageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.GuildOwner;
import com.github.julyss2019.mcsp.julyguild.guild.Permission;
import com.github.julyss2019.mcsp.julyguild.placeholder.Placeholder;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GuildMemberListGUI extends BasePlayerPageableGUI {
    private enum ViewerType {PLAYER, MANAGER}
    private static final List<Permission> managerPermissions = Arrays.asList(Permission.MEMBER_KICK, Permission.MANAGE_PERMISSION);
    private final JulyGuild plugin = JulyGuild.getInstance();
    private final ViewerType viewerType;
    private final Guild guild;
    private final Player bukkitPlayer = getBukkitPlayer();
    private ConfigurationSection thisGUISection;
    private List<Integer> positions;
    private int positionCount;
    private List<GuildMember> members;
    private int memberCount;

    public GuildMemberListGUI(@Nullable GUI lastGUI, Guild guild, GuildMember guildMember) {
        this(lastGUI, guild, guildMember.getGuildPlayer());
    }

    public GuildMemberListGUI(@Nullable GUI lastGUI, Guild guild, GuildPlayer guildPlayer) {
        super(lastGUI, GUIType.MEMBER, guildPlayer);

        this.guild = guild;

        GuildMember member = guild.getMember(guildPlayer);

        out:
        if (member == null) {
            viewerType = ViewerType.PLAYER;
        } else {
            for (Permission permission : managerPermissions) {
                if (member.hasPermission(permission)) {
                    viewerType = ViewerType.MANAGER;
                    break out;
                }
            }

            viewerType = ViewerType.PLAYER;
        }

        this.thisGUISection = plugin.getGUIYaml("GuildMemberListGUI").getConfigurationSection(viewerType.name().toLowerCase());
        this.positions = Util.getIndexes(thisGUISection.getString( "items.member.indexes")); // 得到所有可供公会设置的位置
        this.positionCount = positions.size();

        update();

        if (getTotalPage() > 0) {
            setCurrentPage(0);
        }
    }

    @Override
    public void update() {
        this.members = guild.getMembers();
        this.members.sort(Comparator.comparingLong(GuildMember::getJoinTime));
        this.memberCount = members.size();

        setTotalPage(memberCount % positionCount == 0 ? memberCount / positionCount : memberCount / positionCount + 1);
    }

    @Override
    public Inventory createInventory() {
        Map<Integer, GuildMember> indexMap = new HashMap<>();
        IndexConfigGUI.Builder guiBuilder = new IndexConfigGUI.Builder()
                .fromConfig(thisGUISection, bukkitPlayer)
                .pageItems(thisGUISection.getConfigurationSection("items.page_items"), this, bukkitPlayer, guild)
                .item(GUIItemManager.getIndexItem(thisGUISection.getConfigurationSection("items.back"), bukkitPlayer, guild), new ItemListener() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        back();
                    }
                });

        if (viewerType == ViewerType.MANAGER) {
            guiBuilder.listener(new InventoryListener() {
                        @Override
                        public void onClick(InventoryClickEvent event) {
                            int slot = event.getSlot();

                            if (indexMap.containsKey(slot)) {
                                GuildMember guildMember = indexMap.get(slot);

                                if (!guildMember.isValid()) {
                                    reopen();
                                    return;
                                }

                                close();

                                new GuildMemberManageGUI(GuildMemberListGUI.this, GuildMemberListGUI.this.guild.getMember(guildPlayer), guildMember).open();
                            }
                        }
                    });
        }

        int itemCounter = getCurrentPage() * positions.size();
        int loopCount = memberCount - itemCounter < memberCount ? memberCount - itemCounter : memberCount;

        for (int i = 0; i < loopCount; i++) {
            GuildMember guildMember = members.get(itemCounter++);
            ItemBuilder itemBuilder = GUIItemManager.getItemBuilder(thisGUISection.getConfigurationSection("items").getConfigurationSection("member")
                    , guildMember, new Placeholder.Builder().addGuildMemberPlaceholders(guildMember));

            // 管理模式
            if (viewerType == ViewerType.MANAGER) {
                indexMap.put(positions.get(i), guildMember);
            }

            guiBuilder.item(positions.get(i), itemBuilder.build());
        }

        return guiBuilder.build();
    }

    @Override
    public boolean canUse() {
        return guild.isValid();
    }
}
