package com.DrakGod.KaMCUP;

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
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    Server server = Bukkit.getServer();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();
    Logger logger = server.getLogger();

    HashMap<UUID,Integer> Car_Wait_Time = new HashMap<UUID,Integer>();
    ItemStack Train = new ItemStack(Material.MINECART);

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
        ItemMeta Train_Name = Train.getItemMeta();
        Train_Name.setItemName("列车");
        Train.setItemMeta(Train_Name);
    
        pluginmanager.registerEvents(new Listeners(),this);
        CWTremove.runTaskTimer(this, 20, 20);

        PluginCommand car = this.getCommand("car");
        PluginCommand uuid = this.getCommand("uuid");

        car.setExecutor(this);
        
    
        uuid.setExecutor(this);
        

        logger.info("[KaMC实用插件] 插件成功启用"); }

    @Override
    public void onDisable() {
        CWTremove.cancel();
        Car_Wait_Time.clear();

        logger.info("[KaMC实用插件] 插件已被禁用"); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String CommandName = cmd.getName();
        if (CommandName.equalsIgnoreCase("car")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int wait_time = Car_Wait_Time.getOrDefault(player.getUniqueId(), 0);

                if (wait_time <= 0) {
                    player.addScoreboardTag("Cared");
                    player.getInventory().addItem(Train);

                    sender.sendMessage("§a已获得§d1§a个§d矿车");
                    Car_Wait_Time.put(player.getUniqueId(), 10);
                } else {
                    sender.sendMessage("§c冷却中，剩余§d"+Integer.toString(wait_time)+"秒");
                }
            } else {
                sender.sendMessage("§4此命令仅可由玩家执行");
            }
            return true;
        }

        if (CommandName.equalsIgnoreCase("uuid")) {
            if (args.length == 1) {
                Player player = server.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage("§4玩家 "+args[0]+" 不存在");
                    return true;
                }

                sender.sendMessage("玩家 "+player.getName()+" 的UUID是"+player.getUniqueId().toString());
            } else {
                sender.sendMessage("§4命令用法:/uuid <Player>");
            }
            return true;
        }
        return false;
    }
}