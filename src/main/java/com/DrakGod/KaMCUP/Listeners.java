package com.DrakGod.KaMCUP;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEnterVehicle(VehicleEnterEvent event) {
        if (event.getVehicle().getType() == EntityType.MINECART) {
            Minecart minecart = (Minecart) event.getVehicle();
            Player player = (Player) event.getEntered();
            if (player == null || !player.getScoreboardTags().contains("Cared")) {return;}

            minecart.setMaxSpeed(65536);
            player.removeScoreboardTag("Cared");
            player.sendTitle("§a欢迎乘坐§bKaMC§e快速列车","§4如果被清除,请重新执行/car");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinecartMove(VehicleMoveEvent event) {
        if (!(event.getVehicle() instanceof Minecart)) {return;}

        Minecart minecart = (Minecart) event.getVehicle();
        Player player = (Player) minecart.getPassenger();
        if (player == null) {return;}

        Location from = event.getFrom();
        Location to = event.getTo();

        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        double deltaZ = to.getZ() - from.getZ();

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        if (distance <= 0) {return;}
        
        Integer ispeed = (int) (distance * 72.0);
        StringBuilder out = new StringBuilder(ispeed.toString());
        while (out.length() < 5) {
            out.insert(0, " ");
        }

        player.sendActionBar("当前速度:" + out + " km/h");
    }
}
