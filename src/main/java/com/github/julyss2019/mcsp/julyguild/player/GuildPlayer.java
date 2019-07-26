package com.github.julyss2019.mcsp.julyguild.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.GUI;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.request.GuildPlayerRequest;
import com.github.julyss2019.mcsp.julyguild.player.request.GuildPlayerRequestType;
import com.github.julyss2019.mcsp.julylibrary.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuildPlayer {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private String name;
    private File file;
    private YamlConfiguration yml;
    private Guild guild;
    private GUI usingGUI;
    private Map<String, GuildPlayerRequest> requestMap = new HashMap<>();

    protected GuildPlayer(String name) {
        this.name = name;
    }

    /**
     * 得到当前使用的GUI
     * @return
     */
    public GUI getUsingGUI() {
        return usingGUI;
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
    public void addRequest(GuildPlayerRequest request) {
        requestMap.put(request.getUUID().toString(), request);
    }

    /**
     * 得到请求
     * @return
     */
    public Collection<GuildPlayerRequest> getRequests() {
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
    public boolean hasRequest(GuildPlayerRequestType type) {
        for (GuildPlayerRequest request : getRequests()) {
            if (request.getType() == type) {
                return true;
            }
        }

        return false;
    }

    public GuildPlayerRequest getOnlyOneRequest(GuildPlayerRequestType type) {
        for (GuildPlayerRequest request : getRequests()) {
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

        GUI usingGUI = this.usingGUI;

        if (usingGUI != null) {
            for (GUIType guiType : guiTypes) {
                if (usingGUI.getType() == guiType) {
                    usingGUI.close();
                    usingGUI.build();
                    usingGUI.open();
                }
            }
        }
    }

    /**
     * 初始化
     * @return
     */
    public GuildPlayer load() {
        this.file = new File(plugin.getDataFolder(), "players" + File.separator + name + ".yml");

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.yml = YamlConfiguration.loadConfiguration(file);
        this.guild = guildManager.getGuild(yml.getString("guild"));
        return this;
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        yml.set("guild", guild == null ? null : guild.getUUID().toString());
        save();
        this.guild = guildManager.getGuild(yml.getString("guild"));
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public boolean isOnline() {
        Player tmp = getBukkitPlayer();

        return tmp != null && tmp.isOnline();
    }

    public GuildPlayer getGuildPlayer() {
        return !isOnline() ? null : guildPlayerManager.getGuildPlayer(getBukkitPlayer());
    }

    public boolean isInGuild() {
        return guild != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildPlayer)) return false;
        GuildPlayer that = (GuildPlayer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void save() {
        YamlUtil.saveYaml(yml, file);
    }
}
