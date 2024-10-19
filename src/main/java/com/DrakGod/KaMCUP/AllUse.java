package com.DrakGod.KaMCUP;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class AllUse {
    Server server = Bukkit.getServer();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();
    Logger logger = server.getLogger();

    //public Main getMain() {
    public Plugin getMain() {
        return Bukkit.getPluginManager().getPlugin("KaMC_UtilityPlugin");
    }
}
