package me.aneleu.diceplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RotateTask extends BukkitRunnable {

    int tick = 0;
    BlockDisplay display;

    double initialAngle, x, y, z, axisX, axisY, axisZ, speed;
    double[] initialAxis, initialCenterPos, point, origin, axis;


    double angle = 0;


    public RotateTask(@NotNull BlockDisplay display, int initialFace) {
        // 최대 속도 설정
        int maxSpeed = 60;
        // 최저 속도 설정
        int minSpeed = 30;


        this.display = display;
        this.initialAxis = RotateUtil.faceAxis.get(initialFace);
        this.initialAngle = RotateUtil.faceAngle.get(initialFace);

        x = display.getLocation().getX() + 0.5;
        y = display.getLocation().getY() + 0.5;
        z = display.getLocation().getZ() + 0.5;
        axisX = Math.random() * 2 - 1;
        axisY = Math.random() * 2 - 1;
        axisZ = Math.random() * 2 - 1;
        double magnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        axisX /= magnitude;
        axisY /= magnitude;
        axisZ /= magnitude;

        point = new double[]{x, y, z};
        origin = new double[]{x - 0.5, y - 0.5, z - 0.5};
        axis = new double[]{axisX, axisY, axisZ};
        speed = Math.toRadians(Math.random() * (maxSpeed - minSpeed) + minSpeed);
        if (Math.random() > 0.5) speed *= -1;

        initialCenterPos = RotateUtil.rotateDotByVector(point, origin, initialAxis, initialAngle);
    }

    @Override
    public void run() {

        angle += speed;

        double[] rotatedPos = RotateUtil.rotateDotByVector(initialCenterPos, origin, axis, angle);

        double dx = x - rotatedPos[0];
        double dy = y - rotatedPos[1];
        double dz = z - rotatedPos[2];

        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(dx, dy, dz);
        transformation.getLeftRotation().setAngleAxis((float) angle, (float) axisX, (float) axisY, (float) axisZ);
        display.setTransformation(transformation);

        tick++;
        if (tick >= 40) {

            end();
        }

    }

    public void end() {
        int resultFace = -1;
        double maxY = -64;
        for (Map.Entry<Integer, double[]> entry : RotateUtil.facePos.entrySet()) {
            double[] facePos = new double[3];
            for (int i = 0; i < 3; i++) {
                facePos[i] = entry.getValue()[i] + point[i];
            }

            double[] rotatedFace = RotateUtil.rotateDotByVector(facePos, origin, initialAxis, initialAngle);
            double rotatedFaceY = RotateUtil.rotateDotByVector(rotatedFace, origin, axis, angle)[1];

            if (rotatedFaceY > maxY) {
                maxY = rotatedFaceY;
                resultFace = entry.getKey();
            }
        }

        RotateUtil.rotateDiceToFace(display, resultFace);

        Bukkit.broadcast(Component.text("결과: " + resultFace));

        cancel();
    }
}
