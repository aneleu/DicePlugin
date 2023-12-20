package me.aneleu.diceplugin;

import org.apache.commons.math3.complex.Quaternion;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Contract;
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

    @Contract("_, _ -> new")
    public static double @NotNull [] axisangleToQuaternion(double[] axis, double angle) {
        // axis를 정규화
        double norm = Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2]);
        double x = axis[0] / norm;
        double y = axis[1] / norm;
        double z = axis[2] / norm;

        // quaternion 계산
        double w = Math.cos(angle / 2);
        double qx = x * Math.sin(angle / 2);
        double qy = y * Math.sin(angle / 2);
        double qz = z * Math.sin(angle / 2);

        return new double[]{w, qx, qy, qz};
    }

    @Contract("_ -> new")
    public static double @NotNull [] quaternionToAxisangle(double[] quaternion) {
        // quaternion 요소 추출
        double w = quaternion[0];
        double qx = quaternion[1];
        double qy = quaternion[2];
        double qz = quaternion[3];

        // angle 계산
        double angle = 2 * Math.acos(w);

        // axis 계산
        double norm = Math.sqrt(1 - w * w);
        double x = qx / norm;
        double y = qy / norm;
        double z = qz / norm;

        return new double[]{x, y, z, angle};
    }

    public static double @NotNull [] combineAxisAngles(double[] axis1, double angle1, double[] axis2, double angle2) {
        // 각 axis-angle을 쿼터니언으로 변환
        double[] quaternion1 = axisangleToQuaternion(axis1, angle1);
        double[] quaternion2 = axisangleToQuaternion(axis2, angle2);
        Quaternion q1 = new Quaternion(quaternion1[0], quaternion1[1], quaternion1[2], quaternion1[3]);
        Quaternion q2 = new Quaternion(quaternion2[0], quaternion2[1], quaternion2[2], quaternion2[3]);

        // 두 쿼터니언을 곱하여 회전을 결합
        Quaternion combined = q1.multiply(q2);

        return quaternionToAxisangle(new double[]{combined.getQ0(), combined.getQ1(), combined.getQ2(), combined.getQ3()});
    }

}
