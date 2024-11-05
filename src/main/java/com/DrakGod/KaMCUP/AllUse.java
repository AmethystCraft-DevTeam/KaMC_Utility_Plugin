package com.DrakGod.KaMCUP;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

import com.earth2me.essentials.Essentials;

public class AllUse {
    Server server =  Bukkit.getServer();
    File pluginfolder =  getMain().getDataFolder();
    Logger logger = server.getLogger();
    PluginManager pluginmanager = server.getPluginManager();
    ConsoleCommandSender console = server.getConsoleSender();

    public static Main getMain() {return Main.getCMain();}
    public static CheckUpdate getCheckUpdate() {return getMain().CCheckUpdate;}
    public static CommandHandler geCommandHandler() {return getMain().CCommandHandler;}
    public static Listeners getListeners() {return getMain().CListeners;}
    public static TabCompleters getTabCompleters() {return getMain().CTabCompleters;}

    public interface Tab_to_run {
        public abstract List<String> run();
    }

    public List Load_File(File file) {
        boolean out = false;
        try {
            out = file.createNewFile();
        } catch(Exception e) {
            logger.warning("[KaMC实用插件] 无法创建配置文件:"+file.getName());
        }
        return Arrays.asList(out,YamlConfiguration.loadConfiguration(file));
    }

    public void Save_File(FileConfiguration config,File file) {
        try {
            config.save(file);
        } catch (Exception e) {
            logger.warning("[KaMC实用插件] 无法保存配置文件:"+file.getName());
        }
    }

    public static List<HashMap> get_All_Tasks() {
        List<HashMap> tasks = (List<HashMap>) getMain().Daily_Tasks.get("daily_tasks");
        if (tasks == null) {
            return new ArrayList();
        } else {
            return tasks;
        }
    }

    public static List<String> get_All_Task_Names() {
        List out = new ArrayList<String>();
        List<HashMap> tasks = (List<HashMap>) AllUse.getMain().Daily_Tasks.get("daily_tasks");
        if (tasks == null) {return out;}
        tasks.forEach((task) -> {out.add(new ArrayList<String>(task.keySet()).get(0));});
        return out;
    }

    public static List<String> get_All_Items() {
        List<String> space_name = Stream.of(Material.values())
        .filter(material -> material.isItem())
        .map(material -> material.getKey().toString())
        .collect(Collectors.toList());
        List<String> item_name = Stream.of(Material.values())
        .filter(material -> material.isItem())
        .map(Enum::name)
        .collect(Collectors.toList());
        item_name.forEach((name) -> {
            space_name.add(name.toLowerCase());
        });
        return space_name;
    }

    public static List<String> get_Max_Daily_Task() {
        Integer int_max = (int) AllUse.getMain().Daily_Tasks.get("max_daily_task");
        if (int_max == null) {
            return Arrays.asList("5");
        } else {
            return Arrays.asList(int_max.toString());
        }
    }

    public static List<String> get_Random_Tasks() {
        int max = new Integer(get_Max_Daily_Task().get(0));
        List<String> AllTasks = get_All_Task_Names();
        int AllTasks_len = AllTasks.toArray().length;
        if (AllTasks_len <= max) {
            return AllTasks;
        } else {
            List<String> out = new ArrayList<>();
            String Temp = AllTasks.get(0);
            for (int i=0;i<max;i++) {
                while (out.contains(Temp)) {
                    Temp = AllTasks.get((int) (Math.random()*AllTasks_len));
                }
                out.add(Temp);
            }
            return out;
        }
    }

    public static HashMap get_Daily_Task(String name) {
        List<HashMap> tasks = (List) getMain().Daily_Tasks.get("daily_tasks");
        HashMap out = new HashMap();
        Iterator<HashMap> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            HashMap<String,HashMap> task = iterator.next();
            String task_name = new ArrayList<String>(task.keySet()).get(0);
            if (task_name.equalsIgnoreCase(name)) {
                out = task.get(task_name);
            }
        }
        return out;
    }

    public ItemStack get_Item_At_Name(String name) {
        NamespacedKey key = NamespacedKey.fromString(name);
        Material material = Material.matchMaterial(key.getKey());
        if (material == null) {
            material = Material.matchMaterial(name.toUpperCase());
            if (material == null) {
                return null;
            }
        }
        return new ItemStack(material);
    }

    public int get_Player_Item_Count(Player player,ItemStack item) {
        int count = 0;Iterator iterator = player.getInventory().iterator();
        while (iterator.hasNext()) {
            ItemStack oitem = (ItemStack) iterator.next();
            if (oitem != null && oitem.getType() == item.getType()) {
                count = count + oitem.getAmount();
            }
        }
        return count;
    }

    public boolean remove_Player_Amount_Item(Player player,ItemStack item,int count) {
        int amount = count;PlayerInventory player_inventory = player.getInventory();
        Iterator iterator = player_inventory.iterator();
        boolean removed = false;int i = 0;
        while (iterator.hasNext() & amount > 0) {
            ItemStack oitem = (ItemStack) iterator.next();
            if (oitem != null && oitem.getType() == item.getType()) {
                Integer currentAmount = oitem.getAmount();
                logger.info(currentAmount.toString());
                if (currentAmount <= amount) {
                    amount -= currentAmount;
                    player_inventory.setItem(i,new ItemStack(Material.AIR));
                } else {
                    oitem.setAmount(currentAmount - amount);
                    amount = 0;removed = true;break;
                }
            }
            i = i + 1;
        }
        player.updateInventory();
        return removed;
    }

    public Essentials get_Essentials() {
        return getMain().essentials;
    }

    public BigDecimal get_Player_Money(Player player) {
        return get_Essentials().getUser(player).getMoney();
    }

    public void set_Player_Money(Player player,BigDecimal money) {
        try {
            get_Essentials().getUser(player).setMoney(money);
        } catch (Exception e) {}
    }
}
