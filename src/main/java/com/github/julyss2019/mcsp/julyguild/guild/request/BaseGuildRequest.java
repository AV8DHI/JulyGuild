package com.github.julyss2019.mcsp.julyguild.guild.request;

import com.github.julyss2019.mcsp.julyguild.BaseRequest;

public class BaseGuildRequest extends BaseRequest implements GuildRequest {
    private GuildRequestType guildRequestType;

    public BaseGuildRequest(GuildRequestType guildRequestType) {
        this.guildRequestType = guildRequestType;
    }


    @Override
    public GuildRequestType getType() {
        return guildRequestType;
    }

    @Override
    public boolean isOnlyOne() {
        return false;
    }
}
