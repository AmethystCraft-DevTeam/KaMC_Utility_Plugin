package com.DrakGod.KaMCUP;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners extends AllUse implements Listener {
    BukkitRunnable Seconds_Update = new BukkitRunnable() {
        @Override
        public void run() {
            server.getOnlinePlayers().forEach((player) -> {
                UUID player_uuid = player.getUniqueId();
                List player_tasks = getMain().Player_Daily_Tasks.getOrDefault(player_uuid,null);
                List all_tasks = AllUse.get_All_Task_Names();
                if (player_tasks == null) {
                    player_tasks = AllUse.get_Random_Tasks();
                } else {
                    Iterator<String> iterator = player_tasks.iterator();
                    while (iterator.hasNext()) {
                        String task = iterator.next();
                        if (!all_tasks.contains(task) & !task.contains(" OK")) {
                            player_tasks = AllUse.get_Random_Tasks();
                            break;
                        }
                    }

                    int player_task_lenth = player_tasks.toArray().length;
                    if (player_task_lenth < new Integer(get_Max_Daily_Task().get(0))) {
                        if (player_task_lenth < all_tasks.toArray().length) {
                            iterator = all_tasks.iterator();
                            while (iterator.hasNext()) {
                                String task = iterator.next();
                                if (!player_tasks.contains(task)) {
                                    player_tasks.add(task);
                                }
                                player_task_lenth = player_tasks.toArray().length;
                                if (player_task_lenth == new Integer(get_Max_Daily_Task().get(0))){
                                    break;
                                }
                            }
                        }
                    }
                }
                getMain().Player_Daily_Tasks.put(player_uuid,player_tasks);
            });

            getMain().now = LocalDateTime.now();
            if (getMain().now.isAfter(getMain().midnightToday)) {
                server.getOnlinePlayers().forEach((player) -> {
                    getMain().Player_Daily_Tasks.put(player.getUniqueId(),get_Random_Tasks());
                });
                getMain().midnightToday = getMain().now.toLocalDate().atStartOfDay().plusDays(1);
            }
        }
    };
}

