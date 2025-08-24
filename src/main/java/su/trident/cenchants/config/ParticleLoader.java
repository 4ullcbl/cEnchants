package su.trident.cenchants.config;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleLoader
{
    private final FileConfiguration config;
    private final JavaPlugin plugin;

    private final Particle particle;
    private final int particleCount;
    private final double particleSpeed;
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;

    public ParticleLoader(FileConfiguration config, String configPath, JavaPlugin plugin)
    {
        this.config = config;
        this.plugin = plugin;

        this.particle = loadParticle(config.getString(configPath + ".type"));
        this.particleCount = config.getInt(configPath + ".count");
        this.particleSpeed = config.getDouble(configPath + ".speed");
        this.deltaX = config.getDouble(configPath + ".delta_x");
        this.deltaY = config.getDouble(configPath + ".delta_y");
        this.deltaZ = config.getDouble(configPath + ".delta_z");
    }

    private Particle loadParticle(String name)
    {
        if (name == null) {
            throw new RuntimeException("Particle in config is null -> " + name);
        }

        name = name.toUpperCase();

        try {
            return Particle.valueOf(name);
        } catch (Exception e) {
            plugin.getLogger().info(ChatColor.RED + " Не найден Particle: " + name);
            return findSimilarParticle(name);
        }
    }

    private Particle findSimilarParticle(String name)
    {
        final Particle[] allParticles = Particle.values();
        plugin.getLogger().info(ChatColor.GOLD + "Глубокий поиск замены для вашей частицы: " + name);

        for (Particle particle : allParticles) {
            if (particle.name().equalsIgnoreCase(name)) {
                plugin.getLogger().info(ChatColor.GREEN + "Найдена частица, имеющая похожее название, но отличающаяся регистром: " + particle.name() + ". Замена вашей: " + name);
                return particle;
            }
        }

        plugin.getLogger().info(ChatColor.GOLD + "Замена не найдена, ищем минимально похожие..");

        for (Particle particle : allParticles) {
            if (particle.name().contains(name) || name.contains(particle.name())) {
                plugin.getLogger().info(ChatColor.GREEN + "Найдена частица, содержащая вашу: " + particle.name() + " ваша: " + name);
                return particle;
            }
        }

        plugin.getLogger().info(ChatColor.RED + "Частица '" + name + "'не найдена, дефолт: FLAME");
        return Particle.FLAME;
    }

    public Particle getParticle()
    {
        return particle;
    }

    public int getParticleCount()
    {
        return particleCount;
    }

    public double getParticleSpeed()
    {
        return particleSpeed;
    }

    public double getDeltaX()
    {
        return deltaX;
    }

    public double getDeltaY()
    {
        return deltaY;
    }

    public double getDeltaZ()
    {
        return deltaZ;
    }
}
