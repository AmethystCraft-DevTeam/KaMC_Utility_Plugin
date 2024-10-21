package com.DrakGod.KaMCUP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler extends AllUse implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String CommandName = cmd.getName();
        if (CommandName.equalsIgnoreCase("car")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int wait_time = getMain().Car_Wait_Time.getOrDefault(player.getUniqueId(), 0);

                if (wait_time <= 0) {
                    player.addScoreboardTag("Cared");
                    player.getInventory().addItem(getMain().Train);

                    sender.sendMessage("§a已获得§d1§a个§d矿车");
                    getMain().Car_Wait_Time.put(player.getUniqueId(), 10);
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

        if (CommandName.equalsIgnoreCase("kamcup")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§6------ ======= KaMC实用插件帮助 ======= ------");
                getMain().kamcupcmds.forEach((key,value) -> {sender.sendMessage("§b/kamcup "+key+" "+value);});
                sender.sendMessage("§6------ ======= KaMC实用插件帮助 ======= ------");
                return true;
            }

            if (args[0].equalsIgnoreCase("version")) {
                sender.sendMessage("§6KaMC实用插件当前版本:"+getMain().CCheckUpdate.getNowVersion());
                sender.sendMessage("§6KaMC实用插件最新版本:"+getMain().CCheckUpdate.getNewVersion());
            }

            if (!getMain().kamcupcmds.containsKey(args[0])) {
                sender.sendMessage("§4没有此命令,请使用/kamcup help查看");}
            return true;
        }
        return false;
    }
}
