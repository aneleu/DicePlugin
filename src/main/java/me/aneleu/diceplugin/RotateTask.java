package me.aneleu.diceplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;

public class RotateTask extends BukkitRunnable {

    int tick = 0;
    BlockDisplay display;

    double x, y, z, axisX, axisY, axisZ, speed;
    double[] point, origin, axis;


    public RotateTask(BlockDisplay display) {

        // 최대 속도 설정
        int maxSpeed = 15;


        this.display = display;

        double x = display.getLocation().getX() + 0.5;
        double y = display.getLocation().getY() + 0.5;
        double z = display.getLocation().getZ() + 0.5;
        double a = Math.random();
        double b = Math.random();
        double c = Math.random();
        double magnitude = Math.sqrt(a*a + b*b + c*c);

        point = new double[]{x, y, z};
        origin = new double[]{x - 0.5, y - 0.5, z - 0.5};
        axis = new double[]{a / magnitude, b / magnitude, c / magnitude};
        speed = Math.toRadians(Math.random() * maxSpeed * 2 - maxSpeed);
    }

    @Override
    public void run() {

        double[] rotatedPos = RotateUtil.rotateDotByVector(
                point,
                origin,
                axis,
                speed * tick
        );

        double dx = x - rotatedPos[0];
        double dy = y - rotatedPos[1];
        double dz = z - rotatedPos[2];

        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(dx, dy, dz);

        transformation.getLeftRotation().setAngleAxis( (float) speed * tick, (float) axisX, (float) axisY, (float) axisZ);
        display.setTransformation(transformation);

        tick++;
        if (tick >= 40) {
            cancel();
        }

    }
}
