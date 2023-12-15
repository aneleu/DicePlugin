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

    Map<Integer, double[]> faces = Map.of(
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
        int maxSpeed = 40;
        // 최저 속도 설정
        int minSpeed = 20;


        this.display = display;

        x = display.getLocation().getX() + 0.5;
        y = display.getLocation().getY() + 0.5;
        z = display.getLocation().getZ() + 0.5;
        axisX = Math.random();
        axisY = Math.random();
        axisZ = Math.random();
        double magnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        axisX /= magnitude;
        axisY /= magnitude;
        axisZ /= magnitude;

        point = new double[]{x, y, z};
        origin = new double[]{x - 0.5, y - 0.5, z - 0.5};
        axis = new double[]{axisX, axisY, axisZ};
        speed = Math.toRadians(Math.random() * (maxSpeed - minSpeed) + minSpeed);
        if (Math.random() > 0.5) speed *= -1;
    }

    @Override
    public void run() {

        angle += speed;

        double[] rotatedPos = RotateUtil.rotateDotByVector(point, origin, axis, angle);

        double dx = x - rotatedPos[0];
        double dy = y - rotatedPos[1];
        double dz = z - rotatedPos[2];

        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(dx, dy, dz);
        transformation.getLeftRotation().setAngleAxis((float) angle, (float) axisX, (float) axisY, (float) axisZ);
        display.setTransformation(transformation);

        tick++;
        if (tick >= 40) {

            int resultFace = -1;
            double maxY = -64;
            for (Map.Entry<Integer, double[]> entry : faces.entrySet()) {
                double[] facePos = new double[3];
                for (int i = 0; i < 3; i++) {
                    facePos[i] = entry.getValue()[i] + point[i];
                }
                double rotatedFaceY = RotateUtil.rotateDotByVector(facePos, origin, axis, angle)[1];
                if (rotatedFaceY > maxY) {
                    maxY = rotatedFaceY;
                    resultFace = entry.getKey();
                }
            }

            Bukkit.broadcast(Component.text("결과: " + resultFace));

            cancel();
        }

    }
}
