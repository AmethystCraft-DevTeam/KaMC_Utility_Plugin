package com.DrakGod.KaMCUP;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleters extends AllUse implements TabCompleter{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("kamcup")) {
            if (args.length == 0) {
                getMain().kamcupcmds.forEach((key,value) -> {completions.add(key);});
            }
        }
        
        return completions;
    }
}
