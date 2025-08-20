package su.trident.cenchnats.github;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheck
{
    private final JavaPlugin plugin;
    private final String gitHubRepo;

    public UpdateCheck(JavaPlugin plugin, String gitHubRepo)
    {
        this.plugin = plugin;
        this.gitHubRepo = gitHubRepo;
    }

    public void checkLast()
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try {
                final URL url = new URL("https://api.github.com/repos/" + gitHubRepo + "/releases/latest");
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.addRequestProperty("User-Agent", "Mozilla/5.0");

                if (conn.getResponseCode() == 200) {
                    final JSONObject json = (JSONObject)  new JSONParser().parse(new InputStreamReader(conn.getInputStream()));

                    final String latestVersion = (String) json.get("tag_name");
                    final String currentVersion = this.plugin.getDescription().getVersion();

                    versionInfoMessages(currentVersion, latestVersion);

                } else {
                    this.plugin.getLogger().warning("Не удалось проверить обновление. Код: " + conn.getResponseCode());
                }

                conn.disconnect();

            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void versionInfoMessages(String currentVersion, String latestVersion)
    {
        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            this.plugin.getLogger().info(ChatColor.GREEN + "Найдено обновление: " + latestVersion + " (текущая: " + currentVersion + ")");
            this.plugin.getLogger().info(ChatColor.GREEN + "Ссылка на скачивание: https://github.com/" + gitHubRepo + "/releases/tag/" + latestVersion);
        } else {
            this.plugin.getLogger().info(ChatColor.AQUA + "Установленна последняя версия: " + currentVersion);
        }
    }

}
