package me.aneleu.diceplugin;

import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RotateUtil {

    public static final Map<Integer, double[]> facePos = Map.of(
            1, new double[]{-0.5, 0, 0},
            2, new double[]{0, 0.5, 0},
            3, new double[]{0, 0, 0.5},
            4, new double[]{0, 0, -0.5},
            5, new double[]{0, -0.5, 0},
            6, new double[]{0.5, 0, 0}
    );

    public static final Map<Integer, double[]> faceAxis = Map.of(
            1, new double[]{0, 0, 1},
            2, new double[]{1, 0, 0},
            3, new double[]{1, 0, 0},
            4, new double[]{1, 0, 0},
            5, new double[]{1, 0, 0},
            6, new double[]{0, 0, 1}
    );

    public static final Map<Integer, Double> faceAngle = Map.of(
            1, Math.toRadians(-90),
            2, Math.toRadians(0),
            3, Math.toRadians(-90),
            4, Math.toRadians(90),
            5, Math.toRadians(180),
            6, Math.toRadians(90)
    );

    public static void rotateDiceToFace(BlockDisplay dice, int face) {
        double angle = faceAngle.get(face);
        double[] axis = faceAxis.get(face);

        double[] translation = RotateUtil.rotateDotByVector(new double[]{0, 0, 0}, new double[]{-0.5, -0.5, -0.5}, axis, angle);

        Transformation transformation = dice.getTransformation();
        transformation.getRightRotation().setAngleAxis(angle, axis[0], axis[1], axis[2]);
        transformation.getLeftRotation().set(0, 0, 0, 1);
        transformation.getTranslation().set(-translation[0], -translation[1], -translation[2]);
        dice.setTransformation(transformation);
    }

    public static double @NotNull [] rotateDotByVector(double[] point, double[] origin, double[] axis, double theta) {

        double u = axis[0];
        double v = axis[1];
        double w = axis[2];

        double magnitude = Math.sqrt(u * u + v * v + w * w);
        if (magnitude != 0) {
            u /= magnitude;
            v /= magnitude;
            w /= magnitude;
        } else {
            w = 1;
        }

        double x = point[0] - origin[0];
        double y = point[1] - origin[1];
        double z = point[2] - origin[2];

        double[] result = new double[3];
        double v1 = u * x + v * y + w * z;
        result[0] = (u * v1 * (1 - Math.cos(theta)) + x * Math.cos(theta) + (-w * y + v * z) * Math.sin(theta)) + origin[0];
        result[1] = (v * v1 * (1 - Math.cos(theta)) + y * Math.cos(theta) + (w * x - u * z) * Math.sin(theta)) + origin[1];
        result[2] = (w * v1 * (1 - Math.cos(theta)) + z * Math.cos(theta) + (-v * x + u * y) * Math.sin(theta)) + origin[2];

        return result;
    }

}
