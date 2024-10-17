package com.kamcdrakgod.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    private HashMap<UUID,Integer> Car_Wait_Time;
    private Server server = Bukkit.getServer();
    private PluginManager pluginmanager =server.getPluginManager();
    private ConsoleCommandSender console = server.getConsoleSender();
    private ItemStack Train = new ItemStack(Material.MINECART);

    BukkitRunnable CWTremove = new BukkitRunnable() {
        @Override
        public void run() { Car_Wait_Time.forEach((key,value) -> {
        if (Car_Wait_Time.get(key) > 0) { Car_Wait_Time.put(key, value-1); } }); }};

    @Override
    public void onEnable() {
        ItemMeta Train_Name = Train.getItemMeta();
        Train_Name.setItemName("列车");Train.setItemMeta(Train_Name);
    
        pluginmanager.registerEvents(new Listeners(),this);
        CWTremove.runTaskTimer(this, 20, 20);
        Car_Wait_Time = new HashMap<UUID,Integer>();
        this.getCommand("car").setExecutor(this);
        console.sendMessage("[KaMC实用插件] 插件成功启用"); }

    @Override
    public void onDisable() {
        Car_Wait_Time.clear();CWTremove.cancel();
        console.sendMessage("[KaMC实用插件] 插件已被禁用"); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("car")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int wait_time = Car_Wait_Time.getOrDefault(
                player.getUniqueId(), 0);

                if (wait_time <= 0) {
                    player.addScoreboardTag("Cared");player.getInventory().addItem(Train);
                    sender.sendMessage("§a已获得§d1§a个§d矿车");Car_Wait_Time.put(player.getUniqueId(), 10);
                } else { sender.sendMessage("§c冷却中，剩余§d"+Integer.toString(wait_time)+"秒"); }
            } else { sender.sendMessage("§4这个命令只能在游戏内使用!"); }
            return true;}
        return false;}
}