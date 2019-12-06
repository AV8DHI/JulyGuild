package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.request.player.PlayerRequest;
import com.github.julyss2019.mcsp.julyguild.request.player.PlayerRequestType;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class GuildPlayer {
    private final UUID uuid;
    private File file;
    private YamlConfiguration yml;
    private UUID guildUuid;
    private String name;
    private GUI usingGUI;
    private Map<String, PlayerRequest> requestMap = new HashMap<>();

    GuildPlayer(UUID uuid) {
        this.uuid = uuid;

        load();
    }

    /**
     * 初始化
     * @return
     */
    public GuildPlayer load() {
        this.file = new File(JulyGuild.getInstance().getDataFolder(), "players" + File.separator + getUuid() + ".yml");
        this.yml = YamlConfiguration.loadConfiguration(file);
        this.guildUuid = UUID.fromString(yml.getString("guild"));
        this.name = Optional
                .ofNullable(Bukkit.getOfflinePlayer(getUuid()))
                .map(OfflinePlayer::getName)
                .orElse(uuid.toString());
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isUsingGUI() {
        return usingGUI != null;
    }

    public boolean isUsingGUI(GUIType... types) {
        return Optional.ofNullable(usingGUI).filter(gui -> {
            for (GUIType type : types) {
                if (usingGUI.getType() == type) {
                    return true;
                }
            }

            return false;
        }).isPresent();
    }

    /**
     * 得到当前使用的GUI
     * @return
     */
    public GUI getUsingGUI() {
        return usingGUI;
    }

    /**
     * 关闭GUI
     */
    public void closeGUI() {
        if (!isOnline()) {
            throw new RuntimeException("离线状态下不能更新GUI");
        }

        setUsingGUI(null);
        getBukkitPlayer().closeInventory();
    }

    /**
     * 设置当前使用的GUI
     * @param usingGUI
     */
    public void setUsingGUI(GUI usingGUI) {
        if (!isOnline() && usingGUI != null) {
            throw new IllegalStateException("离线状态下不能设置GUI");
        }

        this.usingGUI = usingGUI;
    }

    /**
     * 添加请求，不存储到文件系统
     */
    public void addRequest(PlayerRequest request) {
        requestMap.put(request.getUUID().toString(), request);
    }

    /**
     * 得到请求
     * @return
     */
    public Collection<PlayerRequest> getRequests() {
        return requestMap.values();
    }

    /**
     * 删除请求
     * @param uuid
     */
    public void removeRequest(String uuid) {
        requestMap.remove(uuid);
    }

    /**
     * 是否有请求
     * @param uuid
     * @return
     */
    public boolean hasRequest(String uuid) {
        return requestMap.containsKey(uuid);
    }

    /**
     * 是否有请求
     * @param type
     * @return
     */
    public boolean hasRequest(PlayerRequestType type) {
        for (PlayerRequest request : getRequests()) {
            if (request.getType() == type) {
                return true;
            }
        }

        return false;
    }

    public PlayerRequest getOnlyOneRequest(PlayerRequestType type) {
        for (PlayerRequest request : getRequests()) {
            if (request.getType() == type) {
                return request;
            }
        }

        return null;
    }

    /**
     * 更新GUI
     * @param guiTypes
     */
    public void updateGUI(GUIType... guiTypes) {
        if (!isOnline()) {
            throw new IllegalStateException("离线状态下不能更新GUI");
        }

        if (usingGUI != null) {
            for (GUIType guiType : guiTypes) {
                if (usingGUI.getType() == guiType) {
                    usingGUI.reopen();
                }
            }

            GUI lastGUI = usingGUI;

            // 遍历所有 lastGUI
            while ((lastGUI = lastGUI.getLastGUI()) != null) {
                for (GUIType guiType : guiTypes) {
                    if (usingGUI.getType() == guiType) {
                        usingGUI.reopen();
                    }
                }
            }
        }
    }

    public Guild getGuild() {
        return Optional
                .ofNullable(guildUuid)
                .map(uuid1 -> JulyGuild.getInstance().getGuildManager().getGuild(guildUuid))
                .filter(guild -> guild.isMember(this))
                .orElseThrow(() -> new RuntimeException("该玩家在指向的公会不是成员"));
    }

    /**
     * 指向公会
     * @param newGuild
     */
    public void pointGuild(Guild newGuild) {
        yml.set("guild", newGuild == null ? null : newGuild.getUuid().toString());
        this.guildUuid = newGuild == null ? null : newGuild.getUuid();
        save();
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isOnline() {
        Player tmp = getBukkitPlayer();

        return tmp != null && tmp.isOnline();
    }

    public boolean isInGuild() {
        return getGuild() != null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public OfflinePlayer getOfflineBukkitPlayer() {
        return Bukkit.getOfflinePlayer(getUuid());
    }

    public void save() {
        YamlUtil.saveYaml(yml, file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildPlayer)) return false;
        GuildPlayer that = (GuildPlayer) o;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}
