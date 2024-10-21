package com.DrakGod.KaMCUP;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;

public class AllUse {
    Server server = Bukkit.getServer();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();
    Logger logger = server.getLogger();

    public Main getMain() {
        return Main.getCMain();
    }
}
