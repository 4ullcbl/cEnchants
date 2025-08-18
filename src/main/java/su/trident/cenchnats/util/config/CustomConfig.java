package su.trident.cenchnats.util.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig
{
    private final FileConfiguration configuration;
    private final File configFile;
    private final JavaPlugin plugin;

    public CustomConfig(JavaPlugin plugin, String configName)
    {
        this.plugin = plugin;
        this.configFile = new File(this.plugin.getDataFolder(), configName);
        this.configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getFile()
    {
        return configuration;
    }

    public void saveSettings() {
        try {
            configuration.save(configFile);
        } catch (IOException e) {
            this.plugin.getLogger().severe("[#] Could not save config to " + configFile);
        }
    }
}
