package com.DrakGod.KaMCUP;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    Server server = Bukkit.getServer();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();
    Logger logger = server.getLogger();

    HashMap<String,String> kamcupcmds = new HashMap<String,String>();
    HashMap<UUID,Integer> Car_Wait_Time = new HashMap<UUID,Integer>();
    ItemStack Train = new ItemStack(Material.MINECART);

    CheckUpdate CCheckUpdate = new CheckUpdate();
    CommandHandler CCommandHandler = new CommandHandler();
    Listeners CListeners = new Listeners();
    TabCompleters CTabCompleters = new TabCompleters();

    BukkitRunnable CWTremove = new BukkitRunnable() {
        @Override
        public void run() {
            Car_Wait_Time.forEach((key,value) -> {
                if (Car_Wait_Time.get(key) > 0) {
                    Car_Wait_Time.put(key, value-1);
                } 
            }); 
        }
    };
    
    @Override
    public void onEnable() {
        kamcupcmds.put("help","获取帮助");
        kamcupcmds.put("version","获取当前版本");

        ItemMeta Train_Name = Train.getItemMeta();
        Train_Name.setItemName("列车");
        Train.setItemMeta(Train_Name);
    
        pluginmanager.registerEvents(CListeners,this);
        CWTremove.runTaskTimer(this, 20, 20);
        CCheckUpdate.CheckUpdateMain.runTaskTimer(this, 20, 72000);

        PluginCommand kamcup = this.getCommand("kamcup");
        PluginCommand car = this.getCommand("car");
        PluginCommand uuid = this.getCommand("uuid");

        kamcup.setExecutor(CCommandHandler);
        kamcup.setTabCompleter(CTabCompleters);
        car.setExecutor(CCommandHandler);
        uuid.setExecutor(CCommandHandler);

        logger.info("[KaMC实用插件] 插件成功启用"); }

    @Override
    public void onDisable() {
        CCheckUpdate.CheckUpdateMain.cancel();
        CWTremove.cancel();
        Car_Wait_Time.clear();

        logger.info("[KaMC实用插件] 插件已被禁用"); }
}