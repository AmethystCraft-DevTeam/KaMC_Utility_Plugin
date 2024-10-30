package com.DrakGod.KaMCUP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler extends AllUse implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String CommandName = cmd.getName();
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

        if (CommandName.equalsIgnoreCase("dailytask")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§4此命令仅可由玩家执行");
                return true;
            }

            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage("§6------ ======= 每日任务帮助 ======= ------");
                sender.sendMessage("§b/dailytask put <name> - 提交一个任务");
                sender.sendMessage("§b/dailytask list - 获取今日任务");
                sender.sendMessage("§6------ ======= 每日任务帮助 ======= ------");
                return true;
            }

            if (args[0].equalsIgnoreCase("put")) {
                if (args.length != 2) {
                    sender.sendMessage("§4请输入正确的参数");
                    return true;
                }
                String name = args[1];
                List<String> tasks = getMain().Player_Daily_Tasks.getOrDefault(player.getUniqueId(),new ArrayList());
                if (!tasks.contains(name)) {
                    sender.sendMessage("§4该任务名称不存在");
                    return true;
                }
                HashMap task = get_Daily_Task(name);
                ItemStack item = get_Item_At_Name((String) task.get("item"));
                int count = get_Player_Item_Count(player,item);
                if (count >= (int) task.get("count")) {
                    remove_Player_Amount_Item(player,item,(int) task.get("count"));
                    BigDecimal give_money = new BigDecimal(task.get("give_money").toString());
                    try {
                        set_Player_Money(player,get_Player_Money(player).add(give_money));
                    } catch (Exception e) {}

                    Iterator<String> iterator = tasks.iterator();
                    while (iterator.hasNext()) {
                        String oname = iterator.next();
                        if (oname.equalsIgnoreCase(name)) {
                            tasks.set(tasks.indexOf(oname),oname+" OK");
                        }
                    }
                    sender.sendMessage("§6提交任务§a成功,§6已获得§e"+task.get("give_money").toString()+"金钱");
                } else {
                    sender.sendMessage("§4所需物品不足");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("§6------ ======= 今日任务列表 ======= ------");
                List<String> tasks = getMain().Player_Daily_Tasks.getOrDefault(player.getUniqueId(),new ArrayList());
                tasks.forEach((task) -> {
                    String is_OK = " - §4未完成";
                    if (task.contains(" OK")) {
                        is_OK = " - §a已完成";
                        task = task.replaceAll(" OK","");
                    }
                    HashMap one_task = (HashMap) get_Daily_Task(task);
                    sender.sendMessage("§b任务名称:"+task+" 需要物品:"+one_task.get("item")+" 需要数量:"
                    +one_task.get("count")+" 给予金钱:"+one_task.get("give_money")+is_OK);
                });
                sender.sendMessage("§6------ ======= 今日任务列表 ======= ------");
                return true;
            }
        }

        if (CommandName.equalsIgnoreCase("kamcup")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§6------ ======= KaMC实用插件帮助 ======= ------");
                getMain().kamcupcmds.forEach((key,value) -> {sender.sendMessage("§b/kamcup "+key+" "+value.get(0));});
                sender.sendMessage("§6------ ======= KaMC实用插件帮助 ======= ------");
                return true;
            }

            if (!getMain().kamcupcmds.containsKey(args[0])) {
                sender.sendMessage("§4没有此命令,请使用/kamcup help查看");
                return true;
            }

            if (args[0].equalsIgnoreCase("version")) {
                sender.sendMessage("§6KaMC实用插件当前版本:"+getCheckUpdate().getNowVersion());
                sender.sendMessage("§6KaMC实用插件最新版本:"+getCheckUpdate().getNewVersion());
            }

            if (args[0].equalsIgnoreCase("dailytask")) {
                if (args.length == 1) {
                    sender.sendMessage("§6------ ======= 每日任务帮助 ======= ------");
                    HashMap<String,List> Temp = (HashMap<String,List>) getMain().kamcupcmds.get("dailytask").get(1);
                    Temp.forEach((key,value) -> {sender.sendMessage("§b/kamcup dailytask "+key+" "+value.get(0));});
                    sender.sendMessage("§6------ ======= 每日任务帮助 ======= ------");
                    return true;
                }

                if (Arrays.asList("add","set").contains(args[1])) {
                    if (args.length != 6) {
                        sender.sendMessage("§4请输入正确的参数");
                        return true;
                    }
                    String name = args[2];String item = args[3];
                    int count;BigDecimal give_money;
                    if (args[1].equalsIgnoreCase("add")) {
                        if (get_All_Task_Names().contains(name)) {
                            sender.sendMessage("§4该任务名称已存在");
                            return true;
                        }
                    } else {
                        if (!get_All_Task_Names().contains(name)) {
                            sender.sendMessage("§4该任务名称不存在");
                            return true;
                        }
                    }

                    if (!get_All_Items().contains(item)) {
                        sender.sendMessage("§4该物品名称不存在");
                        return true;
                    }
                    try {
                        count = new Integer(args[4]);
                        give_money = new BigDecimal(args[5]);
                        int result = give_money.compareTo(new BigDecimal(0));
                        if (count <= 0 | result < 0) {int a = 1/0;}
                    } catch (Exception e) {
                        sender.sendMessage("§4数量必须为大于0的正整数,给予金钱数量必须为大于0的数");
                        return true;
                    }

                    List<HashMap> tasks = get_All_Tasks();
                    if (tasks == null) {tasks = new ArrayList();}
                    if (args[1].equalsIgnoreCase("add")) {
                        HashMap Temp1 = new HashMap();
                        HashMap Temp2 = new HashMap();
                        Temp2.put("item",item);
                        Temp2.put("count",count);
                        Temp2.put("give_money",give_money);
                        Temp1.put(name,Temp2);
                        tasks.add(Temp1);
                    } else {
                        Iterator iterator = tasks.iterator();
                        while (iterator.hasNext()) {
                            HashMap task = (HashMap) iterator.next();
                            String task_name = new ArrayList<String>(task.keySet()).get(0);
                            if (name.equalsIgnoreCase(task_name)) {
                                HashMap Temp = (HashMap) task.get(task_name);
                                Temp.put("item",item);
                                Temp.put("count",count);
                                Temp.put("give_money",give_money);
                                task.put(task_name,Temp);
                                tasks.set(tasks.indexOf(task),task);
                            }
                        }
                    }
                    getMain().Daily_Tasks.set("daily_tasks",tasks);
                    Save_File(getMain().Daily_Tasks,getMain().Daily_Tasks_File);
                    if (args[1].equalsIgnoreCase("add")) {
                        sender.sendMessage("§6添加每日任务:"+name+" 成功");
                    } else {
                        sender.sendMessage("§6修改每日任务:"+name+" 成功");
                    }
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length != 3) {
                        sender.sendMessage("§4请输入正确的参数");
                    } else {
                        String name = args[2];
                        if (!get_All_Task_Names().contains(name)) {
                            sender.sendMessage("§4该任务名称不存在");
                            return true;
                        }

                        List<HashMap> tasks = get_All_Tasks();
                        Iterator<HashMap> iterator = tasks.iterator();
                        while (iterator.hasNext()) {
                            HashMap task = iterator.next();
                            String task_name = new ArrayList<String>(task.keySet()).get(0);
                            if (task_name.equalsIgnoreCase(name)) {
                                iterator.remove();
                            }
                        }
                        getMain().Daily_Tasks.set("daily_tasks",tasks);
                        Save_File(getMain().Daily_Tasks,getMain().Daily_Tasks_File);
                        sender.sendMessage("§6删除每日任务:"+name+" 成功");
                    }
                }

                if (args[1].equalsIgnoreCase("max")) {
                    if (args.length != 3) {
                        sender.sendMessage("§6当前每日任务最大数量为:"+get_Max_Daily_Task().get(0));
                    } else {
                        try {
                            int in_max = new Integer(args[2]);
                            if (in_max <= 0) {int a = 0 / 0;}
                            int max = (int) new Integer(get_Max_Daily_Task().get(0));
                            getMain().Daily_Tasks.set("max_daily_task",in_max);
                            Save_File(getMain().Daily_Tasks,getMain().Daily_Tasks_File);
                            sender.sendMessage("§6当前每日任务最大数量为:"+get_Max_Daily_Task().get(0));
                            if (in_max < max) {sender.sendMessage("§e警告:改为更小值第二天生效!");}
                        } catch (Exception e) {
                            sender.sendMessage("§4请输入大于0的正整数类型");
                        }
                    }
                }

                if (args[1].equalsIgnoreCase("list")) {
                    sender.sendMessage("§6------ ======= 每日任务列表 ======= ------");
                    List<HashMap> tasks = get_All_Tasks();
                    tasks.forEach((task) -> {
                        String task_name = new ArrayList<String>(task.keySet()).get(0);
                        HashMap one_task = (HashMap) task.get(task_name);
                        sender.sendMessage("§b名称:"+task_name+" 物品:"+one_task.get("item"
                        )+" 数量:"+one_task.get("count")+" 给予金钱:"+one_task.get("give_money"));
                    });
                }
            }

            return true;
        }
        return false;
    }
}