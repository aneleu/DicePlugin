package me.aneleu.diceplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;

import java.util.Map;

public class RotateTask extends BukkitRunnable {

    int tick = 0;
    BlockDisplay display;

    double x, y, z, axisX, axisY, axisZ, speed;
    double[] point, origin, axis;

    Map<Integer, double[]> sides = Map.of(
            1, new double[]{-0.5, 0, 0},
            2, new double[]{0, 0.5, 0},
            3, new double[]{0, 0, 0.5},
            4, new double[]{0, 0, -0.5},
            5, new double[]{0, -0.5, 0},
            6, new double[]{0.5, 0, 0}
    );



    double angle = 0;


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

        angle += speed;

        double[] rotatedPos = RotateUtil.rotateDotByVector(point, origin, axis, speed * tick);

        double dx = x - rotatedPos[0];
        double dy = y - rotatedPos[1];
        double dz = z - rotatedPos[2];

        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(dx, dy, dz);
        transformation.getLeftRotation().setAngleAxis( (float) angle, (float) axisX, (float) axisY, (float) axisZ);
        display.setTransformation(transformation);

        tick++;
        if (tick >= 40) {

            int resultSide = -1;
            double maxY = -64;
            for (Map.Entry<Integer, double[]> entry: sides.entrySet()) {
                double[] sidePos = new double[3];
                for (int i = 0; i < 3; i ++) {
                    sidePos[i] = entry.getValue()[i] + point[i];
                }
                double rotatedSideY = RotateUtil.rotateDotByVector(sidePos, origin, axis, angle)[1];
                if (rotatedSideY > maxY) {
                    maxY = rotatedSideY;
                    resultSide = entry.getKey();
                }
            }

            Bukkit.broadcast(Component.text("결과: " + resultSide));

            cancel();
        }

    }
}
