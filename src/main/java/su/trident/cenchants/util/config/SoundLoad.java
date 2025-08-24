package su.trident.cenchants.util.config;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

public class SoundLoad
{
    private final Sound sound;
    private final int yaw;
    private final int pitch;
    private final FileConfiguration config;

    public SoundLoad(FileConfiguration config, String configPath)
    {
        this.config = config;

        this.sound = loadSound(config.getString(configPath + ".type"));
        this.yaw = config.getInt(configPath + ".yaw");
        this.pitch = config.getInt(configPath + ".pitch");
    }

    private Sound loadSound(String name) {
        if (name == null) {
            throw new RuntimeException("sound in config -> is not found or null");
        }

        return Sound.valueOf(name);
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
