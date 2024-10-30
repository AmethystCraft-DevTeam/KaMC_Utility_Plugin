package com.DrakGod.KaMCUP;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.Essentials;

import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    Server server = getServer();
    File pluginfolder = getDataFolder();
    Logger logger = server.getLogger();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();
    Essentials essentials = (Essentials) pluginmanager.getPlugin("Essentials");

    HashMap<String,List> kamcupcmds = new HashMap<String,List>();
    HashMap<UUID,List> Player_Daily_Tasks = new HashMap<UUID,List>();
    ItemStack Train = new ItemStack(Material.MINECART);
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime midnightToday = now.toLocalDate().atStartOfDay().plusDays(1);

    public FileConfiguration Daily_Tasks;
    public File Daily_Tasks_File;
    public static Main CMain;
    public AllUse CAllUse;
    public CheckUpdate CCheckUpdate;
    public CommandHandler CCommandHandler;
    public Listeners CListeners;
    public TabCompleters CTabCompleters;

    BukkitRunnable UCMain = new BukkitRunnable() {@Override public void run() {CMain = getThis();}};
    public Main getThis() {return this;}
    public static Main getCMain() {return CMain;}

    @Override
    public void onEnable() {
        AllUse.Tab_to_run get_alltasks = new AllUse.Tab_to_run() {
            @Override
            public List<String> run() {return AllUse.get_All_Task_Names();}
        };

        AllUse.Tab_to_run get_allitems = new AllUse.Tab_to_run() {
            @Override
            public List<String> run() {return AllUse.get_All_Items();}
        };

        AllUse.Tab_to_run get_tasksmax = new AllUse.Tab_to_run() {
            @Override
            public List<String> run() {return AllUse.get_Max_Daily_Task();}
        };

        HashMap<String,List> addset_hashmap = new HashMap<String,List>() {{
            put("|to_run|",Arrays.asList("",new HashMap<String,List>() {{
                put("|to_run|",Arrays.asList("",new HashMap<String,List>() {{
                    put("1", Arrays.asList("",new HashMap<String,List>() {{
                        put("1", Arrays.asList());
                    }}));
                }},get_allitems));
            }},get_alltasks));
        }};

        kamcupcmds.put("help",Arrays.asList("- 获取帮助",new HashMap<>()));
        kamcupcmds.put("version",Arrays.asList("- 获取当前版本",new HashMap<>()));
        
        if (essentials != null) {
            kamcupcmds.put("dailytask",Arrays.asList("- 查看每日任务系统",new HashMap<String,List>() {{
                put("add",Arrays.asList("<name> <item> <count> <give_money> - 添加任务",addset_hashmap));
                put("set",Arrays.asList("<name> <item> <count> <give_money> - 设置任务",addset_hashmap));
                put("remove",Arrays.asList("<name> - 删除任务",new HashMap<String,List>() {{
                    put("|to_run|",Arrays.asList("",new HashMap<>(),get_alltasks));
                }}));
                put("max",Arrays.asList("<max> - 设置每日最大任务数",new HashMap<String,List>() {{
                    put("|to_run|",Arrays.asList("",new HashMap<>(),get_tasksmax));
                }}));
                put("list",Arrays.asList("- 列出任务",new HashMap<>()));
            }}));
        }

        CMain = getThis();
        UCMain.runTaskTimer(this, 0, 1);
        CAllUse = new AllUse();
        CCheckUpdate = new CheckUpdate();
        CCommandHandler = new CommandHandler();
        CListeners = new Listeners();
        CTabCompleters = new TabCompleters();

        if (!pluginfolder.exists()) {
            pluginfolder.mkdir();
        }
        Daily_Tasks_File = new File(pluginfolder,"Daily_Tasks.yml");
        List Daily_Tasks_Load_Out = CAllUse.Load_File(Daily_Tasks_File);
        if ((boolean) Daily_Tasks_Load_Out.get(0)) {
            Daily_Tasks = YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getResourceAsStream("/Daily_Tasks.yml")));
        } else {
            Daily_Tasks = (FileConfiguration) Daily_Tasks_Load_Out.get(1);
        }
        if (Daily_Tasks != null) {
            CAllUse.Save_File(Daily_Tasks,Daily_Tasks_File);
        } else {
            logger.warning("无法加载配置文件:Daily_Tasks.yml");
        }
        
        pluginmanager.registerEvents(CListeners,this);
        CListeners.Seconds_Update.runTaskTimer(this, 20, 20);
        CCheckUpdate.CheckUpdateMain.runTaskTimer(this, 20, 864000);

        PluginCommand kamcup = this.getCommand("kamcup");
        PluginCommand uuid = this.getCommand("uuid");
        PluginCommand dailytask = this.getCommand("dailytask");
        if (essentials != null) {
            dailytask.setExecutor(CCommandHandler);
            dailytask.setTabCompleter(CTabCompleters);
        }

        kamcup.setExecutor(CCommandHandler);
        kamcup.setTabCompleter(CTabCompleters);
        uuid.setExecutor(CCommandHandler);

        if (essentials == null) {
            dailytask.setPermission("kamcup.null");
            logger.warning("[KaMC实用插件] 未加载essentials服务,无法使用/dailytask指令");
        } else {
            dailytask.setPermission("kamcup.commands.dailytask");
        }

        logger.info("[KaMC实用插件] 插件成功启用"); }

    @Override
    public void onDisable() {
        try {
            CCheckUpdate.CheckUpdateMain.cancel();
            CListeners.Seconds_Update.cancel();
        } catch (Exception e) {
            logger.warning("[KaMC实用插件] 禁用时出现错误:"+e.toString());
        }
        

        logger.info("[KaMC实用插件] 插件已被禁用"); }
}