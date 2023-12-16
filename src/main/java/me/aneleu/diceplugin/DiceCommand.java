package me.aneleu.diceplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DiceCommand implements CommandExecutor {

    private final DicePlugin plugin;

    @Contract(pure = true)
    public DiceCommand(DicePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {

            Location loc = p.getLocation().add(0, -2, 0);
            loc.setYaw(0);
            loc.setPitch(0);

            BlockDisplay display = (BlockDisplay) loc.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
            display.setBlock(plugin.getServer().createBlockData(Material.FURNACE));

            Random random = new Random();
            int initialFace = random.nextInt(6) + 1;
            RotateUtil.rotateDiceToFace(display, initialFace);

            new RotateTask(display, initialFace).runTaskTimer(plugin, 0, 1);
        }


        return true;
    }
}
