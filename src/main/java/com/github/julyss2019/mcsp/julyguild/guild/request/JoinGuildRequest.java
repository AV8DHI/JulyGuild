package com.github.julyss2019.mcsp.julyguild.guild.request;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.MainSettings;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;

import java.util.UUID;

public class JoinGuildRequest extends BaseGuildRequest {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static MainSettings mainSettings = plugin.getMainSettings();

    public JoinGuildRequest() {
        super(GuildRequestType.JOIN);
    }

    public static JoinGuildRequest createNew(GuildPlayer guildPlayer) {
        JoinGuildRequest joinRequest = new JoinGuildRequest();

        joinRequest.setTime(System.currentTimeMillis());
        joinRequest.setRequester(guildPlayer);
        joinRequest.setUuid(UUID.randomUUID());
        return joinRequest;
    }

    @Override
    public boolean isTimeout() {
        return (System.currentTimeMillis() - getCreationTime()) / 1000 > mainSettings.getGuildRequestJoinTimeout();
    }

    @Override
    public boolean isOnlyOne() {
        return true;
    }
}
