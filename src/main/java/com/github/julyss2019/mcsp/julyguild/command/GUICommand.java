package com.github.julyss2019.mcsp.julyguild.command;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.entities.MainGUI;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.commandv2.JulyCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.SenderType;
import com.github.julyss2019.mcsp.julylibrary.commandv2.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GUICommand implements JulyCommand {
    private JulyGuild plugin = JulyGuild.getInstance();
    private GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @SubCommandHandler(firstArg = "main", description = "打开主界面", length = 0, senders = SenderType.PLAYER)
    public void onReload(CommandSender cs, String[] args) {
        new MainGUI(guildPlayerManager.getGuildPlayer((Player) cs)).open();
    }

    @Override
    public String getFirstArg() {
        return "gui";
    }

    @Override
    public String getDescription() {
        return "界面相关";
    }
}
