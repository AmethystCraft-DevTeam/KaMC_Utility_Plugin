package com.DrakGod.KaMCUP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompleters extends AllUse implements TabCompleter{
    public int i;
    public List Temp1;
    public HashMap<String,List> Temp2;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("kamcup")) {
            if (args.length == 1) {
                getMain().kamcupcmds.forEach((key,value) -> {completions.add(key);});
            } else {
                Temp1 = getMain().kamcupcmds.getOrDefault(args[0],Arrays.asList("",new HashMap()));
                Temp2 = (HashMap<String,List>) Temp1.get(1);

                try {
                    for (i=2;i<args.length;i++) {
                        Temp1 = Temp2.getOrDefault(args[i-1],Arrays.asList("",new HashMap()));
                        Temp2.forEach((key,value) -> {if (key.contains("|to_run|")) {Temp1 = value;}});
                        Temp2 = (HashMap<String,List>) Temp1.get(1);
                    }
                } catch (Exception e) {
                    return completions;
                }
    
                Temp2.forEach((key,value) -> {
                    if (key.contains("|to_run|")) {
                        AllUse.Tab_to_run Temp3 = (AllUse.Tab_to_run) value.get(2);
                        Temp3.run().forEach((str) -> {completions.add(str);});
                    } else {
                        completions.add(key);
                    }
                });
            }
        }
        if (cmd.getName().equalsIgnoreCase("dailytask")) {
            if (args.length == 1) {
                Arrays.asList("put","list").forEach((value) -> {completions.add(value);});
            }

            if (args[0].equalsIgnoreCase("put") & args.length == 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<String> tasks = getMain().Player_Daily_Tasks.getOrDefault(player.getUniqueId(),new ArrayList());
                    tasks.forEach((value) -> {
                        if (!value.contains("OK")) {
                            completions.add(value);
                        }
                    });
                }
            }
        }
        return completions;
    }
}
