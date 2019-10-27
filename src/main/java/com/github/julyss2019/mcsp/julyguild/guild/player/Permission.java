package com.github.julyss2019.mcsp.julyguild.guild.player;

import com.github.julyss2019.mcsp.julyguild.config.Lang;

public enum Permission {
    MEMBER(0), ADMIN(1), OWNER(2);

    int level;

    Permission(int level) {
        this.level = level;
    }

    public static String getChineseName(Permission permission) {
        return Lang.getString("Permission." + permission.name().toLowerCase());
    }

    public int getLevel() {
        return level;
    }
}
