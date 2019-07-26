package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGuildIcon;
import com.github.julyss2019.mcsp.julyguild.gui.BaseGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.pageable.GuildIconShopGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.GuildBank;
import com.github.julyss2019.mcsp.julyguild.guild.OwnedIcon;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class GuildIconBuyGUI extends BaseGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private ConfigGuildIcon configGuildIcon;
    private Inventory inventory;
    private Guild guild;
    private GuildBank guildBank;
    private boolean isPointsPayEnabled;
    private boolean isMoneyPayEnabled;

    public GuildIconBuyGUI(GuildPlayer guildPlayer, ConfigGuildIcon configGuildIcon) {
        super(GUIType.ICON_BUY, guildPlayer);

        this.guild = guildPlayer.getGuild();
        this.configGuildIcon = configGuildIcon;
        this.guildBank = guild.getGuildBank();
        this.isMoneyPayEnabled = configGuildIcon.isMoneyPayEnabled();
        this.isPointsPayEnabled = configGuildIcon.isPointsPayEnabled();
        build();
    }

    @Override
    public void build() {
        super.build();

        this.inventory = new InventoryBuilder()
                .title("&e&l请选择支付方式")
                .row(3)
                .colored()
                .item(26, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new GuildIconShopGUI(guildPlayer).open();
                    }
                })

                .item(1, 2, new ItemBuilder()
                        .material(isMoneyPayEnabled ? Material.GOLD_INGOT : Material.BARRIER)
                        .displayName("&f使用 &e金币x" + configGuildIcon.getMoneyCost() + " &f支付")
                        .addLore(isMoneyPayEnabled ? "&b>> &a点击支付" : "&b> &c未启用")
                        .addLore("")
                        .colored()
                        .build(), !isMoneyPayEnabled ? null : new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        int fee = configGuildIcon.getMoneyCost();

                        if (!guildBank.has(GuildBank.BalanceType.MONEY, fee)) {
                            Util.sendColoredMessage(bukkitPlayer, "&c宗门银行金币不足.");
                            return;
                        }

                        guildBank.withdraw(GuildBank.BalanceType.MONEY, fee);
                        Util.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        OwnedIcon ownedIcon = OwnedIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());
                        UUID uuid = guild.giveIcon(ownedIcon.getMaterial(), ownedIcon.getDurability());

                        guild.setCurrentIcon(uuid);
                        close();
                    }
                })
                .item(1, 6, new ItemBuilder()
                        .material(isPointsPayEnabled ? Material.DIAMOND : Material.BARRIER)
                        .displayName("&f使用 &e点券x" + configGuildIcon.getPointsCost() + " &f支付")
                        .addLore(isPointsPayEnabled ? "&b>> &a点击支付" : "&b> &c未启用")
                        .addLore("")
                        .colored()
                        .build(), !isPointsPayEnabled ? null : new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        int fee = configGuildIcon.getPointsCost();

                        if (!guildBank.has(GuildBank.BalanceType.POINTS, fee)) {
                            Util.sendColoredMessage(bukkitPlayer, "&c宗门银行点券不足.");
                            return;
                        }

                        guildBank.withdraw(GuildBank.BalanceType.POINTS, fee);
                        Util.sendColoredMessage(bukkitPlayer, "&d购买成功!");

                        OwnedIcon ownedIcon = OwnedIcon.createNew(configGuildIcon.getMaterial(), configGuildIcon.getDurability());
                        UUID uuid = guild.giveIcon(ownedIcon.getMaterial(), ownedIcon.getDurability());

                        guild.setCurrentIcon(uuid);
                        close();
                    }
                }).build();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
