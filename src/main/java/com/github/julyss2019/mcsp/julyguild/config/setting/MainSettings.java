package com.github.julyss2019.mcsp.julyguild.config.setting;

import com.github.julyss2019.mcsp.julylibrary.config.Config;

import java.util.ArrayList;
import java.util.List;

public class MainSettings {
    @Config(path = "guild.sign.reward.gmoney")
    private static double guildSignRewardGMoney;

    @Config(path = "guild.sign.reward.commands")
    private static List<String> guildSignRewardCommands;

    @Config(path = "metrics_enabled")
    private static boolean metricsEnabled;

    @Config(path = "guild.create.name_regex")
    private static String guildCreateNameRegex;

    @Config(path = "guild.create.price.money.amount")
    private static double guildCreatePriceMoneyAmount;

    @Config(path = "guild.create.price.points.amount")
    private static int guildCreatePricePointsAmount;

    @Config(path = "guild.announcement.split_str")
    private static String guildAnnouncementSplitStr;

    @Config(path = "guild.announcement.max_count")
    private static int guildAnnouncementMaxCount;

    @Config(path = "guild.announcement.default")
    private static List<String> guildAnnouncementDefault;

    @Config(path = "guild.announcement.input.cancel_str")
    private static String guildAnnouncementInputCancelStr;

    @Config(path = "guild.request.join.timeout")
    private static int guildRequestJoinTimeout;

    @Config(path = "guild.default_max_member_count")
    private static int guildDefaultMaxMemberCount;

    @Config(path = "guild.rank.formula")
    private static String guildRankFormula;

    @Config(path = "guild.icon.default.material")
    private static String guildIconDefaultMaterial;

    @Config(path = "guild.icon.default.durability")
    private static short guildIconDefaultDurability;

    @Config(path = "guild.icon.default.first_lore")
    private static String guildIconDefaultFirstLore;

    @Config(path = "guild.dismiss.wait")
    private static int guildDismissWait;

    @Config(path = "guild.dismiss.confirm_str")
    private static String guildDismissConfirmStr;

    @Config(path = "guild.exit.wait")
    private static int guildExitWait;

    @Config(path = "guild.exit.confirm_str")
    private static String guildExitConfirmStr;

    @Config(path = "guild.create.input.cancel_str")
    private static String guildCreateInputCancelStr;

    @Config(path = "guild.create.input.wait_sec")
    private static int guildCreateInputWaitSec;

    @Config(path = "guild.papi.non_str")
    private static String guildPapiNonStr;

    @Config(path = "guild.create.no_duplication_name")
    private static boolean guildCreateNoDuplicationName;

    @Config(path = "guild.member_damage.disabled_notice_interval")
    private static int guildMemberDamageDisableNoticeInterval;

    @Config(path = "guild.gui.default.colored")
    private static boolean guildGuiDefaultColored;

    @Config(path = "guild.gui.default.use_papi")
    private static boolean guildGuiDefaultUsePapi;

    @Config(path = "guild.gui.default.hide_all_flags")
    private static boolean guildGuiDefaultHideAllFlags;

    @Config(path = "guild.shop.launcher")
    private static String guildShopLauncher;

    @Config(path = "guild.spawn.teleport.wait")
    private static int guildSpawnTeleportWait;

    @Config(path = "guild.upgrade.max_member_count")
    private static int guildUpgradeMaxMemberCount;

    @Config(path = "guild.tp_all.timeout")
    private static int guildTpAllTimeout;

    @Config(path = "guild.tp_all.sneak_count")
    private static int guildTpAllSneakCount;

    @Config(path = "guild.tp_all.sneak_count_interval")
    private static int guildTpAllSneakCountInterval;

    @Config(path = "guild.tp_all.send_worlds")
    private static List<String> guildTpAllSendWorlds;

    @Config(path = "guild.tp_all.receive_worlds")
    private static List<String> guildTpAllReceiveWorlds;

    @Config(path = "guild.ess_chat.non_str")
    private static String guildEssChatNotStr;

    public static String getGuildEssChatNotStr() {
        return guildEssChatNotStr;
    }

    public static String getGuildAnnouncementInputCancelStr() {
        return guildAnnouncementInputCancelStr;
    }

    public static double getGuildSignRewardGMoney() {
        return guildSignRewardGMoney;
    }

    public static List<String> getGuildSignRewardCommands() {
        return new ArrayList<>(guildSignRewardCommands);
    }

    public static List<String> getGuildTpAllSendWorlds() {
        return new ArrayList<>(guildTpAllSendWorlds);
    }

    public static List<String> getGuildTpAllReceiveWorlds() {
        return new ArrayList<>(guildTpAllReceiveWorlds);
    }

    public static int getGuildTpAllTimeout() {
        return guildTpAllTimeout;
    }

    public static int getGuildTpAllSneakCount() {
        return guildTpAllSneakCount;
    }

    public static int getGuildTpAllSneakCountInterval() {
        return guildTpAllSneakCountInterval;
    }

    public static int getGuildUpgradeMaxMemberCount() {
        return guildUpgradeMaxMemberCount;
    }

    public static int getGuildSpawnTeleportWait() {
        return guildSpawnTeleportWait;
    }

    public static boolean isGuildGuiDefaultHideAllFlags() {
        return guildGuiDefaultHideAllFlags;
    }

    public static String getGuildShopLauncher() {
        return guildShopLauncher;
    }

    public static boolean isGuildGuiDefaultUsePapi() {
        return guildGuiDefaultUsePapi;
    }

    public static boolean isGuildGuiDefaultColored() {
        return guildGuiDefaultColored;
    }

    public static int getGuildMemberDamageDisableNoticeInterval() {
        return guildMemberDamageDisableNoticeInterval;
    }

    public static boolean isGuildCreateNoDuplicationName() {
        return guildCreateNoDuplicationName;
    }

    public static String getGuildPapiNonStr() {
        return guildPapiNonStr;
    }

    public static String getGuildCreateInputCancelStr() {
        return guildCreateInputCancelStr;
    }

    public static int getGuildCreateInputWaitSec() {
        return guildCreateInputWaitSec;
    }

    public static int getGuildDismissWait() {
        return guildDismissWait;
    }

    public static String getGuildDismissConfirmStr() {
        return guildDismissConfirmStr;
    }

    public static String getGuildIconDefaultMaterial() {
        return guildIconDefaultMaterial;
    }

    public static short getGuildIconDefaultDurability() {
        return guildIconDefaultDurability;
    }

    public static String getGuildIconDefaultFirstLore() {
        return guildIconDefaultFirstLore;
    }

    public static boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public static String getGuildCreateNameRegex() {
        return guildCreateNameRegex;
    }

    public static double getGuildCreatePriceMoneyAmount() {
        return guildCreatePriceMoneyAmount;
    }

    public static int getGuildCreatePricePointsAmount() {
        return guildCreatePricePointsAmount;
    }

    public static String getGuildAnnouncementSplitStr() {
        return guildAnnouncementSplitStr;
    }

    public static int getGuildAnnouncementMaxCount() {
        return guildAnnouncementMaxCount;
    }

    public static int getGuildDefaultMaxMemberCount() {
        return guildDefaultMaxMemberCount;
    }

    public static int getGuildRequestJoinTimeout() {
        return guildRequestJoinTimeout;
    }

    public static List<String> getGuildAnnouncementDefault() {
        return new ArrayList<>(guildAnnouncementDefault);
    }

    public static String getGuildRankFormula() {
        return guildRankFormula;
    }

    public static int getGuildExitWait() {
        return guildExitWait;
    }

    public static String getGuildExitConfirmStr() {
        return guildExitConfirmStr;
    }
}
