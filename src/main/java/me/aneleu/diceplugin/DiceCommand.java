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
            double initialAngle;
            double[] initialAxis;
            if (initialFace == 1) {
                initialAxis = new double[]{0, 0, 1};
                initialAngle = Math.toRadians(-90);
            } else if (initialFace == 2) {
                initialAxis = new double[]{1, 0, 0};
                initialAngle = Math.toRadians(0);
            } else if (initialFace == 3) {
                initialAxis = new double[]{1, 0, 0};
                initialAngle = Math.toRadians(-90);
            } else if (initialFace == 4) {
                initialAxis = new double[]{1, 0, 0};
                initialAngle = Math.toRadians(90);
            } else if (initialFace == 5) {
                initialAxis = new double[]{1, 0, 0};
                initialAngle = Math.toRadians(180);
            } else {
                initialAxis = new double[]{0, 0, 1};
                initialAngle = Math.toRadians(90);
            }

            double[] initialTranslation = RotateUtil.rotateDotByVector(new double[]{0, 0, 0}, new double[]{-0.5, -0.5, -0.5}, initialAxis, initialAngle);

            Transformation transformation = display.getTransformation();
            transformation.getRightRotation().setAngleAxis(initialAngle, initialAxis[0], initialAxis[1], initialAxis[2]);
            transformation.getTranslation().set(-initialTranslation[0], -initialTranslation[1], -initialTranslation[2]);
            display.setTransformation(transformation);

            new RotateTask(display, initialAxis, initialAngle).runTaskTimer(plugin, 0, 1);
        }


        return true;
    }
}
