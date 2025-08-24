package su.trident.cenchants.config;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class SoundLoader
{
    private final Sound sound;
    private final int yaw;
    private final int pitch;
    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public SoundLoader(FileConfiguration config, String configPath, JavaPlugin plugin)
    {
        this.config = config;
        this.plugin = plugin;

        this.sound = loadSound(config.getString(configPath + ".type"));
        this.yaw = config.getInt(configPath + ".yaw");
        this.pitch = config.getInt(configPath + ".pitch");
    }

    private Sound loadSound(String name)
    {
        name = name.toUpperCase();

        try {
            return Sound.valueOf(name);
        } catch (Exception e) {
            plugin.getLogger().info(ChatColor.RED + " Не найден Sound: " + name);
            return findSimilarSound(name);
        }
    }

    private Sound findSimilarSound(String name)
    {
        plugin.getLogger().info(ChatColor.GOLD + "Глубокий поиск замены для вашего звука: " + name);

        final Sound simillarSound = findSimillarSound(name);
        if (simillarSound != null) return simillarSound;

        plugin.getLogger().info(ChatColor.GOLD + "Замена не найдена, ищем минимально похожие..");

        final Sound containsSound = findContainsSound(name);
        if (containsSound != null) return containsSound;

        plugin.getLogger().info(ChatColor.RED + "Звук '" + name + "'не найдена, дефолт: BLOCK_LAVA_POP");
        return Sound.BLOCK_LAVA_POP;
    }

    private @Nullable Sound findSimillarSound(String name)
    {
        final Sound[] allSounds = Sound.values();

        for (Sound s : allSounds) {
            if (s.name().equalsIgnoreCase(name)) {
                plugin.getLogger().info(ChatColor.GREEN + "Найден звук, имеющий похожее название, но отличающаяся регистром: " + s.name() + ". Замена вашему: " + name);
                return s;
            }
        }
        return null;
    }

    private @Nullable Sound findContainsSound(String name)
    {
        final Sound[] allSounds = Sound.values();

        for (Sound s : allSounds) {
            if (s.name().contains(name) || name.contains(s.name())) {
                plugin.getLogger().info(ChatColor.GREEN + "Найдена звук, содержащий вашу: " + s.name() + " ваша: " + name);
                return s;
            }
        }
        return null;
    }

    public Sound getSound()
    {
        return sound;
    }

    public int getYaw()
    {
        return yaw;
    }

    public int getPitch()
    {
        return pitch;
    }
}
