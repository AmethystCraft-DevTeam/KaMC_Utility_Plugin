package com.DrakGod.KaMCUP;

import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.scheduler.BukkitRunnable;

public class CheckUpdate extends AllUse {
    BukkitRunnable CheckUpdateMain = new BukkitRunnable() {
        @Override
        public void run() {
            String NowVersion = getNowVersion();
            String NewVersion = getNewVersion();
            if (NewVersion == null) {return;}
            if (!(NowVersion.equalsIgnoreCase(NewVersion))) {
                logger.warning("[KaMC实用插件] 有新版本可用:"+NewVersion+" 当前版本:"+NowVersion);
            }
        }
    };

    public String getNowVersion() {
        return "v"+getMain().getDescription().getVersion().toString();
    }

    public String getNewVersion() {
        try {
            URL url = new URL("https://api.github.com/repos/Drak-God/KaMC_Utility_Plugin/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader out = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String outLine;
                StringBuilder response = new StringBuilder();

                while ((outLine = out.readLine()) != null) {
                    response.append(outLine);
                }
                out.close();

                return JsonParser.parseString(response.toString()).getAsJsonObject().get("name").getAsString();
            } else {
                logger.warning("[KaMC实用插件] §c无法获取最新版本");
                return null;
            }
        } catch (Exception error) {
            logger.warning("[KaMC实用插件] §c无法获取最新版本");
            return null;
        }
    }
}